import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class fileReader {

	private static String fileName;
	private static int sequenceLength;
	private static int debugLevel;
	private static String origin = "origin";
	private static String end = "//";
	private static String masterString = "";
	private static boolean found;
	private static BTree tree;
	private static Storage traversalFile;
	private static int degree;
	private static int cacheSize;
	private static int useCache;

	public fileReader(int useCache, int degree, String fileName, int sequenceLength, int cacheSize, int debugLevel) {

		fileReader.useCache = useCache;
		fileReader.degree = degree;
		fileReader.fileName = fileName;
		fileReader.sequenceLength = sequenceLength;
		fileReader.cacheSize = cacheSize;
		fileReader.debugLevel = debugLevel;
		found = false;
		createTree(); // creates the tree based on using cache or not.

	}

	public void parse(String fileName) {

		File file = new File(fileName);
		try {
			@SuppressWarnings("resource")
			Scanner lineScanner = new Scanner(file);
			Scanner tokenScanner;
			String line;
			String token;
			@SuppressWarnings("unused")
			int tokenCounter = 0;
			while (lineScanner.hasNextLine()) {

				line = lineScanner.nextLine();
				tokenScanner = new Scanner(line);

				while (tokenScanner.hasNext()) {
					token = tokenScanner.next().toLowerCase();

					if (token.equals(origin)) {
						found = true;
					}
					if (token.equals(end)) {
						found = false;
					}

					if (found == true) {
						if (token.equals(origin)) {

						} else {

							tokenCounter++;
							masterString += token;

						}
					}

				}

			}

			stringProcessor(integerRemover(masterString));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static String integerRemover(String preProcess) {

		String postProcess = "";

		for (int i = 0; i < preProcess.length(); i++) {
			;
			char c = masterString.charAt(i);

			if ((c == 'a') || (c == 't') || (c == 'c') || (c == 'g') || c == 'n') {
				postProcess = postProcess + c;
			} else {
				// do nothing here
			}

		}
		return postProcess;

	}

	public static void stringProcessor(String str) {

		String temp = "";
		long longKey = 0;

		int pointerCounter = 0;
		for (int i = 0; i < str.length(); i++) {

			if (pointerCounter <= (str.length() - sequenceLength)) {

				for (int j = pointerCounter; j < (pointerCounter + sequenceLength); j++) { // 0,1,2
					char c = str.charAt(j);
					temp += c;

				}
				if (validSubSeq(temp) == true) {

					// here is where we will make an insert.
					longKey = binaryToLong(convertToBinary(temp)); // each KeyObject will know its frequency.
					tree.insert(longKey);
					System.out.println(temp + ": " + longKey);

				} else {
					temp = "";
				}

				temp = "";
			}
			pointerCounter++;
		}

	}

	public static boolean validSubSeq(String s) {

		@SuppressWarnings("resource")
		Scanner tempScanner = new Scanner(s);
		while (tempScanner.hasNext()) {
			String passfailString = tempScanner.next();

			for (int i = 0; i < passfailString.length(); i++) {

				if (passfailString.charAt(i) == 'n') {
					return false;
				}

			}

		}

		return true;
	}

	/**
	 * Converts a string containing a subString of a DNA sequence into a long
	 * binary.
	 * 
	 * @param sequence of DNA characters
	 * @return sequence in binary
	 */
	public static String convertToBinary(String sequence) {

		String binStr = "";

		char c;
		for (int i = 0; i < sequence.length(); i++) {
			c = sequence.charAt(i);
			if (c == 'a' || c == 'A') {
				binStr += "00";
			} else if (c == 't' || c == 'T') {
				binStr += "11";
			} else if (c == 'c' || c == 'C') {
				binStr += "01";
			} else if (c == 'g' || c == 'G') {
				binStr += "10";
			}
		}

		return binStr; // returns a binary in String format.
	}

	/**
	 * 
	 * @param binStr binary version of string.
	 * @return returns the long version of the binary (which is the key).
	 */

	// convert our binary string and convert it to a long base 10
	public static long binaryToLong(String binStr) {

		char[] numbers = binStr.toCharArray();
		long result = 0;

		for (int i = numbers.length - 1; i >= 0; i--) {

			if (numbers[i] == '1') {
				result += (long) Math.pow(2, (numbers.length - i - 1));
			}
		}

		return result; // returns a long type
	}

	// debugLevel '1' problem begins here.

	/**
	 * Decides the current DNA character
	 * 
	 * @param appendStr  current string to append to
	 * @param firstChar  first binary number
	 * @param secondChar second binary number
	 * @return The DNA character matching the given binary numbers
	 */
	public static String decideChar(String appendStr, int firstChar, int secondChar) {
		String retVal = appendStr;

		if (firstChar == 0 && secondChar == 0) {
			retVal += "A";
		} else if (firstChar == 0 && secondChar == 1) {
			retVal += "C";
		} else if (firstChar == 1 && secondChar == 0) {
			retVal += "G";
		} else if (firstChar == 1 && secondChar == 1) {
			retVal += "T";
		}

		return retVal;
	}

	/**
	 * Add preceding 0's to a string binary sequence if missing them
	 * 
	 * @param binaryIn sequence
	 * @return modified sequence
	 */
	// because when converting from long to binary it could be missing zero's
	// and we know that sequence length of binary will be the double the

	private static String checkMissingZeros(String binStr) {

		String preCompleteBin = binStr;

		if (preCompleteBin.length() != (sequenceLength * 2)) {

			String postCompleteBin = "";

			int difference = (sequenceLength * 2) - preCompleteBin.length();

			for (int i = 0; i < difference; i++) {
				preCompleteBin += "0";
			}

			postCompleteBin += preCompleteBin;

			return postCompleteBin;
		}

		return preCompleteBin;
	}

	/**
	 * Converts a given binary sequence of type String to its DNA character
	 * representation.
	 * 
	 * @param binString The binary sequence in form of a String.
	 * @return The DNA character representation of the binary sequence
	 */
	public static String convertCharSeqence(String binString) {
		String retVal = "";
		int firstC = 2; // to represent binary numbers 1 & 0
		int secondC = 2;// 2 is default or error
		int cCount = 0;
		// loop through the binary string and grab the
		// the characters in "pairs". This is done with the
		// cCount variable, a properly formatted binString
		// will always be an even number of characters
		for (int i = 0; i < binString.length(); i++) {
			// if both characters found decide the character
			// and reset values
			if (cCount == 2) {
				retVal = decideChar(retVal, firstC, secondC);
				firstC = 2;
				secondC = 2;
				cCount = 0;
			}
			// grab the first and second characters and increment count
			if (cCount == 1) {
				String temp1 = "" + binString.charAt(i);
				secondC = Integer.parseInt(temp1);
				cCount++;
			}

			else if (cCount == 0) {
				String temp0 = "" + binString.charAt(i);
				firstC = Integer.parseInt(temp0);
				cCount++;
			}

			else {
				if (debugLevel == 0) {
					System.err.println("Error occured");
				}
			}

		}
		// now decide the last character in the substring
		retVal = decideChar(retVal, firstC, secondC);
		// and return the DNA subString
		return retVal.toLowerCase();
	}

	/**
	 * In-order traversal provided the root.
	 * 
	 * @param root
	 */
	public void treeTraversal(BTreeNode root) {
		traversalFile = tree.getDataFile();
		if (root != null) {

			Path file = Paths.get("./dump");
			for (int i = 0; i < root.getNumKeys(); i++) {
				if (i < root.getNumChildren()) {
					treeTraversal(traversalFile.getNode(root.getChildAt(i)));
				}
				List<String> str = Arrays
						.asList(convertCharSeqence(checkMissingZeros(Long.toBinaryString(root.getKeyAt(i)))) + ": "
								+ root.getDupAt(i));

				try {
					Files.write(file, str, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (root.getNumKeys() < root.getNumChildren()) {
				treeTraversal(traversalFile.getNode(root.getChildAt(root.getNumKeys())));
			}
		}
	}

	/**
	 * creating the BTree object.
	 */

	public static void createTree() {
		if (useCache == 1) {
			tree = new BTree(useCache, degree, fileName, sequenceLength, cacheSize);
		} else {
			tree = new BTree(useCache, degree, fileName, sequenceLength, 0);
		}
	}

	public BTreeNode obtainRoot() {
		return tree.getRoot();
	}

	public void initCache(int cacheS) {
		tree.initCache(cacheS);
	}

}
