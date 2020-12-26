import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Reads the string sequences in a given query file and searches a given BTree
 * file to see if there are any matches.
 */
public class GeneBankSearch {

	static int useCache;
	static int cacheSize;
	static int debugLevel;
	static String btreeFilename;
	static String queryFilename;
	static BTree tree;
	static Storage file;
	static File query;

	public static void main(String[] args) throws IOException {

		// checks for too many or too few arguments
		if (args.length < 3 || args.length > 5) {
			printUsage();
			System.exit(1);
		}

		useCache = Integer.parseInt(args[0]);
		btreeFilename = args[1];
		queryFilename = args[2];
		cacheSize = 0;

		// if fourth argument is debug level
		if (args.length == 4 && useCache == 0) {

			debugLevel = Integer.parseInt(args[3]);

			if (debugLevel != 0) {
				System.out.println("GeneBankSearch only supports a debug level of 0.");
				System.exit(1);
			}
		}
		// if fourth argument is cache size
		else if (args.length == 4 && useCache == 1) {
			cacheSize = Integer.parseInt(args[3]);
			debugLevel = 0;
		}
		// if both debug level and cache size are specified
		else if (args.length == 5) {

			cacheSize = Integer.parseInt(args[3]);
			debugLevel = Integer.parseInt(args[4]);

			if (debugLevel != 0) {
				System.out.println("GeneBankSearch only supports a debug level of 0.");
				System.exit(1);
			}
		}

		file = new Storage(btreeFilename); // access to the BTree file
		query = new File(queryFilename); // create new File object so the query file can be accessed
		tree = new BTree(useCache, file, cacheSize); // create the BTree in order to perform search

		checkQuery();
	}

	/**
	 * Goes through the query file line by line, and prints output along the way
	 * (such as the query line number, the query itself, and its frequency.
	 *
	 * @throws FileNotFoundException
	 */
	private static void checkQuery() throws FileNotFoundException {

		Scanner qScan = new Scanner(query);
		String qLine = qScan.nextLine();

		// check for compatible sequence lengths
		if (qLine.length() != file.getSequenceLength()) {
			System.out.println("Error: Sequence lengths of the files do not match.");
			System.exit(1);
		}

		// search for sequence and print output
		while (qScan.hasNextLine()) {

			System.out.print(qLine + " ");
			int frequency = checkBTree(qLine);

			if (frequency == 0) {
				System.out.println("[not found]");
			} else {
				System.out.println(": " + frequency);
			}

			qLine = qScan.nextLine();

			// check again for compatible sequence lengths
			if (qLine.length() != file.getSequenceLength()) {
				System.out.println("Error: Sequence length of the line is incorrect.");
			}
		}

		// getting last line of query file
		System.out.print(qLine + " ");

		int frequency = checkBTree(qLine);

		if (frequency == 0) {
			System.out.println("[not found]");
		} else {
			System.out.println(": " + frequency);
		}

		qScan.close();
	}

	/**
	 * Searches through the BTree file to find a string and returns its frequency.
	 *
	 * @param qLine the string to be searched for
	 */
	private static int checkBTree(String qLine) {
		long key = fileReader.binaryToLong(fileReader.convertToBinary(qLine));
		return tree.search(file.getRoot(), key);
	}

	/**
	 * Prints usage to the console.
	 */
	private static void printUsage() {
		System.out.println("USAGE: java GeneBankSearch <0/1(no/with cache)> <btree file>"
				+ " <query file> [<cache size>] [<debug level>]");
	}
}