import java.util.LinkedList;
import java.util.ListIterator;

public class Cache {
	private int size;
	private Storage store;
	private LinkedList<BTreeNode> list;

	/**
	 * Constructor: Creates a new Cache.
	 * 
	 * @param size  The size of the cache in nodes.
	 * @param store The DataStore to lookup from.
	 */
	public Cache(int size, Storage store) {
		this.size = size;
		this.store = store;
		this.list = new LinkedList<BTreeNode>();
	}

	/**
	 * cacheQuery checks the cache for a node by address. If not found, it will
	 * query the DataStore for the node and add it to the cache. It will trim the
	 * LinkedList to keep its size from growing beyond the given bound.
	 * 
	 * @param address The address of the node to look up.
	 * @return The BTreeNode
	 */
	public BTreeNode cacheQuery(int address) {
		boolean cacheHit = false;
		BTreeNode retNode = null;
		ListIterator<BTreeNode> iterator = list.listIterator();
		while (iterator.hasNext() && !cacheHit) {
			retNode = iterator.next();
			if (retNode.getMyAddr() == address) {
				cacheHit = true;
				iterator.remove();
			}
		}
		if (cacheHit) {
			list.addFirst(retNode);
			return retNode;
		} else {
			retNode = store.getNode(address);
			list.addFirst(retNode);
			if (list.size() > size) {
				list.removeLast();
			}
			return retNode;
		}
	}

	/**
	 * Clears the cache by creating a new LinkedList.
	 */
	public void clearCache() {
		list = new LinkedList<BTreeNode>();
	}

}
