import java.io.IOException;

/**
 * Create a BTree with a given degree and key sequence length.
 */
public class BTree {

	private Storage file;
	private BTreeNode root;
	private BTreeNode currentNode;
	private Cache cache;
	private int degree;
	private BTreeNode child1;
	private BTreeNode child2;

	/**
	 * The constructor that creates a new BTree and BTree file.
	 *
	 * @param useCache       whether to use a cache
	 * @param degree         the degree of each node
	 * @param gbkFilename    the name of the file to build the tree from
	 * @param sequenceLength the DNA sequence length
	 * @param cacheSize      the cache size (0 if no cache)
	 */
	public BTree(int useCache, int degree, String gbkFilename, int sequenceLength, int cacheSize) {

		this.degree = degree;

		try {
			// name of BTree file is "xyz.gbk.btree.data.k.t" - k is sequence length and t
			// is degree
			file = new Storage(gbkFilename + ".btree.data." + sequenceLength + "." + degree, degree, sequenceLength);
		} catch (IOException e) {
			System.out.println("Error: I/O Exception - BTree Constructor");
		}

		root = file.createNode();
		root.setLeaf(true);
		file.writeNode(root);
		file.setRoot(root.getMyAddr());

		if (useCache == 1) {
			initCache(cacheSize);
		}
	}

	/**
	 * The secondary constructor that creates a new BTree from a given BTree file.
	 *
	 * @param useCache  whether to use a cache
	 * @param file      the given BTree file
	 * @param cacheSize the cache size (0 if no cache)
	 */
	public BTree(int useCache, Storage file, int cacheSize) {

		this.file = file;
		degree = file.getDegree();
		root = file.getRoot();

		if (useCache == 1) {
			initCache(cacheSize);
		}
	}

	/**
	 * Inserts a new key into the array.
	 * 
	 * @param newKey the new key
	 */
	public void insert(long newKey) {

		// first check node if key exists; if exists, increase
		// value by one and do not execute rest of method
		for (int i = 0; i < root.getNumKeys(); i++) {
			
			if (newKey == root.getKeyAt(i)) {
				root.setDupAt(i, root.getDupAt(i) + 1);
				file.writeNode(root);
				return;
			}
			
		}

		// if root is full, split
		if (root.getNumKeys() == (2 * degree) - 1) {
			currentNode = file.createNode();
			file.setRoot(currentNode.getMyAddr());
			currentNode.setLeaf(false);
			currentNode.setNumKeys(0);
			currentNode.setChildAt(0, root);
			currentNode.setNumChildren(1);
			file.writeNode(currentNode);
			splitChild(currentNode, 0);
			insertNonfull(currentNode, newKey);
			root = currentNode; // need to reassign the root pointer, otherwise it will always point to the same
								// node
		}
		// if not full, insert
		else {
			insertNonfull(root, newKey);
		}
	}

	/**
	 * Recursively inserts a new key into a non-full node.
	 *
	 * @param currentNode the current, non-full node
	 * @param newKey      the new key
	 */
	
	public void insertNonfull(BTreeNode currentNode, long newKey) {

		// first check node if key exists; if exists, increase
		// value by one and do not execute rest of method
		for (int i = 0; i < currentNode.getNumKeys(); i++) {
			if (newKey == currentNode.getKeyAt(i)) {
				currentNode.setDupAt(i, currentNode.getDupAt(i) + 1);
				file.writeNode(currentNode);
				return; // stop method from continuing
			}
		}

		int i = currentNode.getNumKeys();

		// if a leaf node, insert
		if (currentNode.isLeaf()) {

			// move over keys so the new key can be inserted
			while (i > 0 && newKey < currentNode.getKeyAt(i - 1)) {
				currentNode.setKeyAt(i, currentNode.getKeyAt(i - 1));
				currentNode.setDupAt(i, currentNode.getDupAt(i - 1));
				i--;
			}

			// insert the new key, change instance value, and increase number of keys
			currentNode.setKeyAt(i, newKey);
			currentNode.setDupAt(i, 1);
			currentNode.setNumKeys(currentNode.getNumKeys() + 1);
			file.writeNode(currentNode);
			return; // write to file
		}
		// if not a leaf node, need to find one to insert into
		else {

			// find the index of the child node to be inserted into
			while (i > 0 && newKey < currentNode.getKeyAt(i - 1)) {
				i--;
			}

			// get the child node
			if (cache == null) {
				child1 = file.getNode(currentNode.getChildAt(i)); // read from file
			} else {
				child1 = cache.cacheQuery(currentNode.getChildAt(i)); // read from cache
			}

			// if child1 is full, split it
			if (child1.getNumKeys() == (2 * degree) - 1) {
				splitChild(currentNode, i);
				// if the new key is greater than the value brought up into the current node
				// when splitting the child node
				if (newKey > currentNode.getKeyAt(i)) {
					i++;
				}
			}

			if (cache == null) {
				currentNode = file.getNode(currentNode.getChildAt(i)); // read from file
			} else {
				currentNode = cache.cacheQuery(currentNode.getChildAt(i)); // read from cache
			}

			insertNonfull(currentNode, newKey);
		}
	}

	/**
	 * Splits node into two children.
	 *
	 * @param currentNode the current node
	 * @param i           the index of the child to be split
	 */
	
	public void splitChild(BTreeNode currentNode, int i) {

		if (cache == null) {
			child1 = file.getNode(currentNode.getChildAt(i)); // read from file
		} else {
			child1 = cache.cacheQuery(currentNode.getChildAt(i)); // read from cache
		}

		child2 = file.createNode();
		child2.setLeaf(child1.isLeaf());
		child2.setNumKeys(degree - 1); // child1 is full, so set the new child2 to half full
		child2.setNumChildren(child1.getNumChildren() / 2); // set child2 to have half the children child1 has

		// create new child and insert half of the keys into it
		// also copy over the number of instances for each key
		for (int j = 0; j < degree - 1; j++) {
			child2.setKeyAt(j, child1.getKeyAt(j + degree));
			child2.setDupAt(j, child1.getDupAt(j + degree));
		}

		// if child1 has children, remap the second half of those to the second child
		if (!child1.isLeaf()) {
			for (int j = 0; j < degree; j++) {
				if (cache == null) {
					child2.setChildAt(j, file.getNode(child1.getChildAt(j + degree)));
				} else {
					child2.setChildAt(j, cache.cacheQuery(child1.getChildAt(j + degree)));
				}
			}

			// reduce number of children in child1 by half
			child1.setNumChildren(degree);
		}

		// reduce number of keys in child1 by half
		child1.setNumKeys(degree - 1);

		// moves values in current node over to make space for a pointer to child2
		// if currentNode is empty and only points to child1, the for loop will not be
		// performed
		for (int j = currentNode.getNumKeys(); j > i; j--) {
			if (cache == null) {
				currentNode.setChildAt(j + 1, file.getNode(currentNode.getChildAt(j))); // read from file
			} else {
				currentNode.setChildAt(j + 1, cache.cacheQuery(currentNode.getChildAt(j))); // read from cache
			}
		}

		// point currentNode to child2
		currentNode.setChildAt(i + 1, child2);

		// move over keys in currentNode to make space for node from child1
		// also move over number of instances for each key
		for (int j = currentNode.getNumKeys() - 1; j >= i; j--) {
			currentNode.setKeyAt(j + 1, currentNode.getKeyAt(j));
			currentNode.setDupAt(j + 1, currentNode.getDupAt(j));
		}

		currentNode.setKeyAt(i, child1.getKeyAt(degree - 1));
		currentNode.setDupAt(i, child1.getDupAt(degree - 1));
		currentNode.setNumKeys(currentNode.getNumKeys() + 1);
		currentNode.setNumChildren(currentNode.getNumChildren() + 1);
		file.writeNode(child1); // write to file
		file.writeNode(child2); // write to file
		file.writeNode(currentNode); // write to file
	}

	/**
	 * Recursively search for a key, starting from the root and moving down.
	 *
	 * @param currentNode the current node, starting from the root
	 * @param searchKey   the key to be searched for
	 * @return the number of instances of the key
	 */
	public int search(BTreeNode currentNode, long searchKey) {

		int i = 0;

		while (i < currentNode.getNumKeys() && searchKey > currentNode.getKeyAt(i)) {
			i++;
		}

		// if the searched-for key matches the key at the index, return the number of
		// instances at that index
		if (i < currentNode.getNumKeys() && searchKey == currentNode.getKeyAt(i)) {
			return currentNode.getDupAt(i);
		}
		// if current node is a leaf node and the value is not found, end search and
		// return 0
		else if (currentNode.isLeaf()) {
			return 0;
		}
		// go to the next node down and start recursion
		else {

			if (cache == null) {
				currentNode = file.getNode(currentNode.getChildAt(i)); // read from file
			} else {
				currentNode = cache.cacheQuery(currentNode.getChildAt(i)); // read from cache
			}

			return search(currentNode, searchKey);
		}
	}

	/**
	 * Initializes the cache.
	 * 
	 * @param size the size of the cache
	 */
	
	public void initCache(int size) {
		cache = new Cache(size, file);
	}

	/**
	 * 
	 * Gets the root of the BTree.
	 * @return the root
	 */
	
	public BTreeNode getRoot() {
		return root;
	}
	
	/** 
	 * Gets the Storage file.
	 * @return the Storage file
	 */
	
	public Storage getDataFile() {
		return file;
	}
}