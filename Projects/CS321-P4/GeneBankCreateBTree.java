import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Parses a given .gbk file and creates subSequences of a given length. It then
 * sends these subSequences to a BTree in form of a binary sequence.
 */
public class GeneBankCreateBTree {
	private static int degree;
	private static int useCache;
	private static int sequenceLength;
	private static int cacheSize;
	private static int debugLevel;
	private static String fileName;

	// Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file>
	// <sequence length> [<cache size>] [<debug level>]
	public static void main(String[] args) throws FileNotFoundException {

		if (args.length < 4) {
			printUsage();
		}

		if (Integer.parseInt(args[0]) > 2) {
			printUsage();
		}

		useCache = Integer.parseInt(args[0]);

		if (Integer.parseInt(args[1]) < 0) {
			printUsage();
		}

		else if (Integer.parseInt(args[1]) == 0) {
			degree = 128;
		}

		else {
			degree = Integer.parseInt(args[1]);
		}

		fileName = args[2];

		if (Integer.parseInt(args[3]) < 1 || Integer.parseInt(args[3]) > 31) {
			printUsage();
		}

		sequenceLength = Integer.parseInt(args[3]);

		if (args.length == 5) {

			if (useCache == 0) {
				printUsage();
			}

			debugLevel = 0;
			cacheSize = Integer.parseInt(args[4]);

		}

		else if (args.length == 6) {

			debugLevel = Integer.parseInt(args[5]);
			cacheSize = Integer.parseInt(args[4]);

		}

		fileReader fileRead = new fileReader(useCache, degree, fileName, sequenceLength, cacheSize, debugLevel);

		if (useCache == 1) {
			fileRead.initCache(cacheSize);
		}

		System.out.println("Parsing: ");

		fileRead.parse(fileName);

		// taking care of debugLevel == 1
		if (debugLevel == 1) {

			System.out.println("Dumping Inorder Traversal...");
			File f = new File("./dump");

			try {

				f.delete();
				f.createNewFile();

			}

			catch (IOException e) {
				e.printStackTrace();
			}

			fileRead.treeTraversal(fileRead.obtainRoot());
		}
	}

	/**
	 * Prints usage to console
	 */
	private static void printUsage() {
		System.out.println(
				"Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
		System.exit(1);
	}

}