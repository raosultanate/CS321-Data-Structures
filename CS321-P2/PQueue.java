
/**
 * PQueue is a Process priority queue implemented using a maxheap data
 * structure.
 * 
 * @author Daniel Rao
 */
public class PQueue {
	private MaxHeap<Process> queue;

	/**
	 * Creates a new, empty priority queue.
	 */
	public PQueue() {
		queue = new MaxHeap<Process>();
	}

	/**
	 * Removes the highest priority value item from the queue. the item with the
	 * highest priority value.
	 */

	public Process dePQueue() {
		return (Process) queue.extractMax();
	}

	/**
	 * Update all processes in the queue.
	 * 
	 * @param timeToIncrementPriority the time unit during which a process has not
	 *                                received any work.
	 * @param maxLevel                the highest priority level allowed.
	 */
	public void update(int timeToIncrementPriority, int maxLevel) {
		for (int i = MaxHeap.ROOT; i <= queue.size(); i++) {
			Process p = (Process) queue.get(i);
			p.incrementTimeNotProcess();
			if (timeToIncrementPriority <= p.getTimeNotProcess()) {
				if (p.getPriority() < maxLevel) {
					p.incrementPriority();
				}
				p.resetTimeNotProcessed();
			}
		}
		// Rebuild the heap in case any of the new updated processes have move up.
		queue.buildMaxHeap();
	}

	/**
	 * Clear the queue of all processes.
	 */
	public void clearQueue() {
		queue.clearHeap();
	}

	/**
	 * Add a Process to the queue.
	 * 
	 * @param p the Process to be added to the queue.
	 */
	public void enPQueue(Process p) {
		queue.insert(p);
	}

	/**
	 * Returns true is the queue is empty.
	 * 
	 * @return true if queue contains no objects.
	 */
	public boolean isEmpty() {
		return queue.size() < MaxHeap.ROOT;
	}

}