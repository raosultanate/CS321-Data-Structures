import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * @author Daniel Rao
 */
public class Test {

	private enum ERROR_CASES {
		INCORR_NUM_ARGS, NO_ARGS, FILE_NOT_SPECIFIED, CACHE_SIZE_MISMATCH, INCORR_ARGS
	}

	private static int cacheOneSize;
	private static int cacheTwoSize;
	private static Cache<String> cacheOne, cacheTwo;
	private static String file_Name;
	private static boolean cacheTwoinUse = false; // use to indicate if a 2 level cache is being use.

	public static void main(String[] args) throws FileNotFoundException {

		if (args.length < 1) {
			printErrorMessages(ERROR_CASES.NO_ARGS); //if user provides no arguments

		}
		//user selecting level one cache
		if (args[0].equals("1")) {
			if (args.length != 3) {
				printErrorMessages(ERROR_CASES.INCORR_NUM_ARGS);

			}

			cacheOneSize = Integer.parseInt(args[1]);
			cacheOne = new Cache<String>(cacheOneSize);
			file_Name = args[2]; //obtains the file name of the user if user passed 1 as a parameter.
			//reads the file and process it into cache using user cache system selection.
			fileReader(file_Name);

		} else if (args[0].equals("2")) { //user selecting level two cache.

			// if this 'elseif' condition runs than the user probably selected 2, for 2
			// caches.
			if (args.length != 4) {
				printErrorMessages(ERROR_CASES.INCORR_NUM_ARGS);

			}
			//obtating the size depending on user section
			cacheOneSize = Integer.parseInt(args[1]);
			cacheTwoSize = Integer.parseInt(args[2]);
			//constructing two types of caches.
			cacheOne = new Cache<String>(cacheOneSize);
			cacheTwo = new Cache<String>(cacheTwoSize);
			cacheTwoinUse = true;
			file_Name = args[3]; //obtains the file name of the user if user passed 2 as a parameter.
			//reads the file and process it into cache using user cache system selection.
			fileReader(file_Name);

		}
		// if the first argument is neither '1' or '2'
		else {
			printErrorMessages(ERROR_CASES.INCORR_ARGS);
		}

	}
	/**
	 * 
	 * @param option enum of all the error options provided by the user.
	 */

	private static void printErrorMessages(ERROR_CASES option) {
		String message = "\nUsage: [1] [cache_one_size]  <input_file> \nOR" + "\nUsage: [2] [cache_one_size] [cache_two_size] <input_file> " +"\n Wherer 1 is one level cache and 2 is two level cache system";;

		if (option == ERROR_CASES.NO_ARGS) {
			System.err.println(message);
		}
		if (option == ERROR_CASES.INCORR_ARGS) {
			System.err.println(message);
		}
		if (option == ERROR_CASES.INCORR_NUM_ARGS) {
			System.err.println("Incomplete arguments." + message);
		}
		if (option == ERROR_CASES.FILE_NOT_SPECIFIED) {
			System.err.println("File is not provided." + message);
		}
		if (option == ERROR_CASES.CACHE_SIZE_MISMATCH) {
			System.err.println("Size of 2nd-Level cache is less than size of 1st-Level cache.");
		} else {
			System.err.println(message);
		}

	}
	
	/**
	 * 
	 * @param fileName passed from the arguments, for the file to be constructed and processed.
	 * @throws FileNotFoundException if file is not found.
	 */

	@SuppressWarnings("unused")
	private static void fileReader(String fileName) throws FileNotFoundException {

		int cacheOneMiss = 0;
		int levelTwoRef = 0;
		int levelOneRef = 0;
		double totalNumRef = 0, levelOneNH = 0, levelTwoNH = 0;

		File file = new File(fileName);  //constructs file object based on user input.
		Scanner fileScanner = new Scanner(file); //file Scanner

		
		
		while (fileScanner.hasNextLine()) {

			String line = fileScanner.nextLine(); //contains a line at each iteration.
			@SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(line); //Scans the line to be able to scan word.

			while (lineScanner.hasNext()) {

				String word = lineScanner.next(); //scans the words and puts it into the string.
				totalNumRef++;
				// hit logic

				// user probably selected first level cache by selecting '1' as a parameter.
				if (!cacheTwoinUse) {
					levelOneRef++;
					if (cacheOne.Hit(word)) {
						levelOneNH++;
						cacheOne.removeObject(word);
						cacheOne.addObject(word);
					} else {
						cacheOneMiss++;
						//adds regardless of hit.
						cacheOne.addObject(word);
					}

				}
				// user probably selected second level cache by selecting '2' as a parameter.
				else {

					levelTwoRef++;
					if (cacheOne.Hit(word)) {
						
						levelOneNH++;
						
						// Check for double and remove it
						cacheOne.removeObject(word);
						cacheOne.addObject(word);
						cacheTwo.removeObject(word);
						cacheTwo.addObject(word);
					} else {
						
						cacheOneMiss++;
						if (cacheTwo.Hit(word)) {
							levelTwoNH++;
							
							
							// Check for double and remove it
							cacheTwo.removeObject(word);
							cacheTwo.addObject(word);
							cacheOne.addObject(word);
							

						}

						else {
							
							//adds regardless of hit.
							cacheOne.addObject(word);
							cacheTwo.addObject(word);
							levelTwoRef++;


						}
					}

				}

			}

		}
		fileScanner.close();
		//printing the output.
		if (!cacheTwoinUse) {
			System.out.println("First level cache with " + cacheOneSize + " entries has been created");
			System.out.println("");
			System.out.println("..............................");
			System.out.println("");
			System.out.println("The number of global references: " + totalNumRef);
			System.out.println("The number of global cache hits: " + levelOneNH / totalNumRef);
			System.out.println("The number of 1st-level refernces: " + levelOneRef);
			System.out.println("The number of 1st-Level cache hits: " + levelOneNH);

		} else {
			System.out.println("First level cache with " + cacheOneSize + " entries has been created");
			System.out.println("Second level cache with " + cacheTwoSize + " entries has been created");
			System.out.println("");
			System.out.println("..............................");
			System.out.println("");

			System.out.println("The number of global references: " + totalNumRef);
			System.out.println("The number of global cache hits: " + (levelOneNH + levelTwoNH)
					+ "\nThe global hit ratio: " + ((levelOneNH + levelTwoNH) / totalNumRef));
			System.out.println("");

			System.out.println("The number of 1st-level refernces: " + levelOneRef);
			System.out.println("The number of 1st-Level cache hits: " + (int) levelOneNH + "\n1st-Level cache ratio: "
					+ (levelOneNH / totalNumRef));
			System.out.println("");

			System.out.println("The number of 2nd-level refernces: " + levelTwoRef);
			System.out.println("Number of 2nd-Level cache hits: " + (int) levelTwoNH + "\n2nd-Level cache ratio: "
					+ (levelTwoNH / cacheOneMiss));
		}
		

	}

}
