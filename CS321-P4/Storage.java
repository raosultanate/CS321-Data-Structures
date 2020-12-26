import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Represents a GBK B Tree binary file.
 *
 */
public class Storage {
	RandomAccessFile dataFile;
	int endOfFile;
	int recordLength;
	int degree;

	/**
	 * Constructor: Open an existing file. Throws an error if the file does not
	 * exist or a read error occurs.
	 * 
	 * @param filename the filename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Storage(String filename) throws FileNotFoundException, IOException {
		dataFile = new RandomAccessFile(filename, "r"); // Open existing file. We open read-only.
		dataFile.seek(0);
		degree = dataFile.readInt(); // Read in the degree from the file.
		recordLength = (2 * degree - 1) * 8 + (2 * degree - 1) * 4 + (2 * degree * 4) + 3; // Record Size Formula.
	}

	/**
	 * Constructor: Create a new file. Throws an error if the file already exists or
	 * a read/write error occurs.
	 * 
	 * @param filename        the filename
	 * @param degree          the degree
	 * @param subStringLength the sequence length
	 * @throws IOException
	 */
	public Storage(String filename, int degree, int subStringLength) throws IOException {
		dataFile = new RandomAccessFile(filename, "rw"); // Open a new file as read/write with synchronized IO.
		this.degree = degree; // Degree is given.
		endOfFile = 12;
		dataFile.setLength(endOfFile + 1); // Give the file a size, otherwise it's empty.
		dataFile.seek(0);
		dataFile.writeInt(degree); // Degree is at offset 0
		dataFile.seek(4);
		dataFile.writeInt(subStringLength); // Substring Length is at offset 4
		dataFile.seek(8);
		dataFile.writeInt(12); // Root location is at offset 8
		recordLength = (2 * degree - 1) * 8 + (2 * degree - 1) * 4 + (2 * degree * 4) + 12; // Record Size
	}

	/**
	 * Returns the root node of the BTree.
	 * 
	 * @return the root
	 */
	public BTreeNode getRoot() {
		try {
			dataFile.seek(8); // Seek to the root address field.
			return getNode(dataFile.readInt()); // Run getNode on the address stored there.
		} catch (IOException e) { // Fail on IOException.
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Sets the address of the new Root node.
	 * 
	 * @param addr the new address
	 */
	public void setRoot(int addr) {
		if (addr > endOfFile - recordLength)
			throw new IndexOutOfBoundsException();
		try {
			dataFile.seek(8); // Seek to the root address field.
			dataFile.writeInt(addr); // Write the new address.
		} catch (IOException e) { // Fail on IOException.
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Returns the node at an address.
	 * 
	 * @param addr the address
	 * @return the node
	 */
	public BTreeNode getNode(int addr) {
		try {
			BTreeNode retNode = new BTreeNode(addr, degree); // The myAddr field is filled by the constructor.
			dataFile.seek(addr); // Seek to the start of the record.

			retNode.setParentAddr(dataFile.readInt()); // Get the parent address.
			retNode.setNumChildren(dataFile.readInt()); // Get the number of children.
			retNode.setNumKeys(dataFile.readInt()); // Get the number of keys.
			int numKeys = retNode.getNumKeys();
			int numChildren = retNode.getNumChildren();

			if (retNode.getNumChildren() == 0) {
				retNode.setLeaf(true);
			} else {
				retNode.setLeaf(false);
			}

			long[] keys = new long[(2 * degree) - 1];
			for (int i = 0; i < numKeys; i++) { // Get the keys stored in the record.
				keys[i] = dataFile.readLong();
			}
			retNode.setKeys(keys);

			int[] values = new int[(2 * degree) - 1]; // Get the occurrence values in the record.
			for (int i = 0; i < numKeys; i++) {
				values[i] = dataFile.readInt();
			}
			retNode.setValues(values);
			int[] children = new int[(2 * degree)]; // Get the child addresses.
			for (int i = 0; i < numChildren; i++) {
				children[i] = dataFile.readInt();
			}
			retNode.setChildren(children);
			return retNode;
		} catch (IOException e) { // Fail on an IOException.
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Returns a new empty node.
	 * 
	 * @return the new node
	 */
	public BTreeNode createNode() {
		try {
			dataFile.seek(endOfFile); // Go to the last byte of the file.
			BTreeNode newNode = new BTreeNode(endOfFile, degree); // Create a new Node that starts there.
			endOfFile = (endOfFile + recordLength); // Increase the size of the file by the length of a record.
			dataFile.setLength(dataFile.length() + recordLength); // Write size change to file.
			return newNode;
		} catch (IOException e) { // Fail on IOException.
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Write a node's changes to the file. You need to write changes to a node to
	 * disk any time a node is modified. This includes the first time you give a
	 * node any properties. If you don't, the changes will not persist on the next
	 * lookup.
	 * 
	 * @param node the node
	 */
	public void writeNode(BTreeNode node) {
		// System.out.println("Writing Node to disk");
		try {
			dataFile.seek(node.getMyAddr()); // Seek to the record to be updated.

			dataFile.writeInt(node.getParentAddr()); // Write Parent address.
			dataFile.writeInt(node.getNumChildren()); // Write number of children.
			dataFile.writeInt(node.getNumKeys()); // Write the number of keys.

			for (int i = 0; i < node.getNumKeys(); i++) { // Write keys to file.
				dataFile.writeLong(node.getKeyAt(i));
			}

			for (int i = 0; i < node.getNumKeys(); i++) { // Write values to file.
				dataFile.writeInt(node.getDupAt(i));
			}

			for (int i = 0; i < node.getNumChildren(); i++) { // Write children to file.
				dataFile.writeInt(node.getChildAt(i));
			}

			// Note that we do not write myAddr to the file, as we must already
			// know that if we are reading it.

		} catch (IOException e) { // Fail on IOException.
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Gets the sequence length.
	 * 
	 * @return the sequence length
	 */
	public int getSequenceLength() {
		try {
			dataFile.seek(4);
			return dataFile.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return 0;
		}
	}

	/**
	 * Gets the degree.
	 * 
	 * @return the degree
	 */
	public int getDegree() {
		return degree;
	}
}