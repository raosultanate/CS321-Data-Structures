import java.util.LinkedList;
/*
 * @author Daniel Rao
 */

//getObject, addObject, removeObject, clearCache, getSize, Hit, clearCache, cacheSize

public class Cache<T> {
	private LinkedList<T> list;
	private int cacheSize; // cache seize

	public Cache(int CacheSize) {
		list = new LinkedList<>();
		this.cacheSize = CacheSize;

	}

	/**
	 * 
	 * @param obj of generic type.
	 * @return true or false if the object exist in the cache memory.
	 */
	public boolean Hit(T obj) {
		return list.contains(obj);
	}

	/*
	 * returns the size of the cache.
	 */
	public int getSize() {
		return list.size();
	}

	/**
	 * 
	 * @param obj adds obects to the cache and reupdates cache once there is an
	 *            overflow.
	 */
	public void addObject(T obj) {
		if (list.size() <= cacheSize) {

			list.addFirst(obj);
			// check for overflow of data.
			if (list.size() > cacheSize) {

				list.removeLast();
			}

		}
	}

	/*
	 * deletes everything in the cache memory.
	 */
	public void clearCache() {

		list.clear();

	}

	// return the limit of Cache
	public int cacheSize() {
		return cacheSize;
	}

	/**
	 * 
	 * @param obj
	 * @return the object if the object already exist in the cache.
	 */
	public T getObject(T obj) {
		int targetIndex = list.indexOf(obj);
		T target = list.get(targetIndex);
		return target;

	}

	/*
	 * removes the specified object from the cache.
	 */
	public void removeObject(T obj) {
		list.remove(obj);
	}

}