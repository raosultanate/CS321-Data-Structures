/**
 * The class is use to represent a CPU process. Each object has a priority
 * level, time required to process and arrival time. Processes are compared
 * based on their priority levels. If those are equal then the process with the
 * earlier arrival time is "greater".
 * 
 * @author Daniel Rao
 */
public class Process implements Comparable<Process> {

	private int arrivalTime;
	private int timeNotProcess;
	private int priorityLevel;
	private int timeRemainingToFinish; 

	/**
	 * Process: Create a new Process with the specified parameters.
	 * 
	 * @param arrivalTime         the arrival time of this process.
	 * @param requiredProcessTime the total time needed for this Process to
	 *                            complete.
	 * @param priorityLevel       the priority level for the process.
	 */
	public Process(int arrivalTime, int requiredProcessTime, int priorityLevel) {
		this.priorityLevel = priorityLevel;
		this.arrivalTime = arrivalTime;
		timeRemainingToFinish = requiredProcessTime;
		timeNotProcess = 0;
	}

	/**
	 * Get the arrival time of this Process.
	 * 
	 * @return this process's arrival time
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}


	/**
	 * Get the time remaining before this process is complete.
	 * 
	 * @return the time remaining before this process is complete.
	 */
	public int getTimeRemaining() {
		return timeRemainingToFinish;
	}

	@Override
	public int compareTo(Process operation) {
		if (this.priorityLevel < operation.priorityLevel) {
			return -1;
		} else if (this.priorityLevel > operation.priorityLevel) {
			return 1;
		}

		/*
		 * If priority levels are equal, look at arrival time and return the object with
		 * the earliest arrival time
		 */
		else if (this.arrivalTime < operation.arrivalTime) {
			return 1;
		}
		return 0;
	}

	/**
	 * Decrease the time remaining by one and reset the time not process.
	 */
	public void reduceTimeRemaining() {
		timeRemainingToFinish--;
		resetTimeNotProcessed();
	}
	
	/**
	 * Get the priority level of this Process.
	 * 
	 * @return this process's priority level.
	 */
	public int getPriority() {
		return priorityLevel;
	}


	/**
	 * Increment this process's priority level by 1.
	 */
	public void incrementPriority() {
		priorityLevel++;
	}

	/**
	 * Reset this process's time not process value to 0.
	 */
	public void resetTimeNotProcessed() {
		timeNotProcess = 0;
	}

	/**
	 * Increment the time not process value by one 1.
	 */
	public void incrementTimeNotProcess() {
		timeNotProcess++;
	}

	/**
	 * Returns true if this process is complete.
	 * 
	 * @return true if the process is complete.
	 */
	public boolean finish() {
		return getTimeRemaining() == 0;
	}

	/**
	 * Get the time not processed for this object.
	 * 
	 * @return the total number of time this Process has not received any work.
	 */
	public int getTimeNotProcess() {
		return timeNotProcess;
	}

}