import java.util.ArrayList;

/**
 * @param <T> the types of objects to be store at each node in this heap.
 * @author Daniel Rao
 */

public class MaxHeap<T extends Comparable<T>> {
	public static final int ROOT = 1;
	private int size;
	private ArrayList<T> heap;

	/**
	 * Creates a new MaxHeap with index starting at 1.
	 */
	public MaxHeap() {
		heap = new ArrayList<>();
		heap.add(null);
	}

	/**
	 * Heapify starting at the specified index to maintain the maxheap property.
	 * Both the left and right subtree of the starting index must be maxheaps.
	 * 
	 * @param startingIndex the index of the node violating the maxheap property.
	 */
	public void maxHeapify(int startingIndex) {
		int largest;
		int leftChild = (startingIndex * 2);
		int rightChild = (startingIndex * 2) + 1;

		if (leftChild <= size && heap.get(leftChild).compareTo(heap.get(startingIndex)) == 1) {
			largest = leftChild;
		} else
			largest = startingIndex;

		if (rightChild <= size && heap.get(rightChild).compareTo(heap.get(largest)) == 1) {
			largest = rightChild;
		}

		if (largest != startingIndex) {
			swap(startingIndex, largest);
			maxHeapify(largest);
		}
	}

	/**
	 * Maxheapify the heap from the bottom up.
	 */
	public void buildMaxHeap() {
		size = heap.size() - 1;
		for (int i = (heap.size()) / 2; i > 0; i--) {
			maxHeapify(i);
		}
	}

	/**
	 * Get the highest value node in the heap.
	 * 
	 * @return the highest value node in the heap.
	 */
	public T maximum() {
		return heap.get(ROOT);
	}

	/**
	 * Sort the heap using the object's natural ordering in place.
	 */
	public void heapSort() {
		buildMaxHeap();
		int heapSize = size;
		for (int i = heapSize; i > ROOT; i--) {
			swap(ROOT, i);
			size--;
			maxHeapify(ROOT);
		}

		size = heap.size() - 1;
	}

	/**
	 * Insert a node into the heap.
	 * 
	 * @param key the node to be inserted.
	 */
	public void insert(T key) {
		heap.add(ROOT, key);
		size = heap.size();
		buildMaxHeap();
	}

	/**
	 * Get a node at the specified index.
	 * 
	 * @param index the index of the node.
	 * @return the node at the specified index.
	 */
	public T get(int index) {
		if (index < ROOT) {
			throw new IndexOutOfBoundsException("Index must < ROOT (1)");
		}
		return heap.get(index);
	}

	/**
	 * Removes the node containing the highest value out of the heap.
	 * 
	 * @return the node with the biggest value. prints error message if the total
	 *         number of elements in the heap is less 1.
	 */
	public T extractMax() {
		if (size < ROOT) {
			System.err.println("Size must < ROOT (1)");
		}

		T max = heap.remove(ROOT);
		size--;
		buildMaxHeap();
		return max;
	}

	/**
	 * Clear the heap of all data nodes.
	 */
	public void clearHeap() {
		heap.clear();
		heap.add(null);
	}

	/**
	 * Exchange the objects at index p and j.
	 * 
	 * @param p the index of the first object
	 * @param j the index of the second object.
	 */
	private void swap(int i, int j) {
		T temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}

	/**
	 * Returns the MaxHeap object with all it's node.
	 * 
	 * @return the MaxHeap as an ArrayList. important info: Indexing in a max-heap
	 *         starts at one 1 so index 0 of the returned ArrayList contains a null
	 *         node.
	 */
	public ArrayList<T> getHeap() {
		return heap;
	}

	/**
	 * Returns the total number of objects in the heap.
	 * 
	 * @return the number of objects in the heap.
	 */
	public int size() {
		return size;
	}
}