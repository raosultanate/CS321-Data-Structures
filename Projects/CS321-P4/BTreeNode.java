/**
 * Represents a BTree node, containing keys, values, and children.
 * 
 */
public class BTreeNode {

	private long[] keys;
	private int[] values;
	private int[] children;
	private int parentAddr;
	private final int myAddr;
	private int numChildren;
	private int numKeys;
	private boolean leaf;

	public BTreeNode(int myAddr, int degree) {
		this.myAddr = myAddr;
		keys = new long[(degree * 2) - 1];
		values = new int[(degree * 2) - 1];
		children = new int[(degree * 2)];
		parentAddr = 0;
		numChildren = 0;
		numKeys = 0;
	}

	/**
	 * Gets the keys array.
	 * 
	 * @return the keys array
	 */
	public long[] getKeys() {
		return keys;
	}

	/**
	 * Sets the keys array.
	 * 
	 * @param keys the new keys array
	 */
	public void setKeys(long[] keys) {
		this.keys = keys;
	}

	/**
	 * Gets the values array.
	 * 
	 * @return the values array
	 */
	public int[] getValues() {
		return values;
	}

	/**
	 * Sets the values array.
	 * 
	 * @param values the new values array
	 */
	public void setValues(int[] values) {
		this.values = values;
	}

	/**
	 * Gets the children array.
	 * 
	 * @return the children array
	 */
	public int[] getChildren() {
		return children;
	}

	/**
	 * Sets the children array.
	 * 
	 * @param children the new children array
	 */
	public void setChildren(int[] children) {
		this.children = children;
	}

	/**
	 * Returns the key at a given index.
	 * 
	 * @param i the index
	 * @return the key at the given index
	 */
	public long getKeyAt(int i) {
		return keys[i];
	}

	/**
	 * Sets a new key at a given index.
	 * 
	 * @param i      the index
	 * @param newVal the new key
	 */
	public void setKeyAt(int i, long newVal) {
		keys[i] = newVal;
	}

	/**
	 * Returns the number of duplicates at a given index.
	 * 
	 * @param i the index
	 * @return the number of duplicates at the given index
	 */
	public int getDupAt(int i) {
		return values[i];
	}

	/**
	 * Sets a new number of duplicates at a given index.
	 * 
	 * @param i             the index
	 * @param numDuplicates the new number of duplicates
	 */
	public void setDupAt(int i, int numDuplicates) {
		values[i] = numDuplicates;
	}

	/**
	 * Gets the address of a child at a given index
	 * 
	 * @param i the index
	 * @return the address of the child
	 */
	public int getChildAt(int i) {
		return children[i];
	}

	/**
	 * Sets a new child at a given index.
	 * 
	 * @param i        the index
	 * @param newChild the new child to set
	 */
	public void setChildAt(int i, BTreeNode newChild) {
		children[i] = newChild.getMyAddr();
		newChild.setParentAddr(myAddr);
	}

	/**
	 * Gets the address of the parent node.
	 * 
	 * @return the parent's address
	 */
	public int getParentAddr() {
		return parentAddr;
	}

	/**
	 * Sets a new address for the parent node.
	 * 
	 * @param parentAddr the new parent address
	 */
	public void setParentAddr(int parentAddr) {
		this.parentAddr = parentAddr;
	}

	/**
	 * Gets the address of the current node.
	 * 
	 * @return the current node's address
	 */
	public int getMyAddr() {
		return myAddr;
	}

	/**
	 * Gets the number of children.
	 * 
	 * @return the number of children
	 */
	public int getNumChildren() {
		return numChildren;
	}

	/**
	 * Sets the number of children.
	 * 
	 * @param numChildren the new number of children
	 */
	public void setNumChildren(int numChildren) {
		this.numChildren = numChildren;
	}

	/**
	 * Gets the number of keys.
	 * 
	 * @return the number of keys
	 */
	public int getNumKeys() {
		return numKeys;
	}

	/**
	 * Sets the number of keys.
	 * 
	 * @param numKeys the number of keys
	 */
	public void setNumKeys(int numKeys) {
		this.numKeys = numKeys;
	}

	/**
	 * Gets whether or not the node is a leaf node.
	 * 
	 * @return whether the node is a leaf node
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * Sets the leaf value.
	 * 
	 * @param bool the new boolean leaf value
	 */
	public void setLeaf(boolean bool) {
		leaf = bool;
	}
}