/*
 * @author Daniel Rao
 */
public class HashObject<T> implements Comparable<T> {
	private T Value;
	private int duplicateCount;
	private int probeCount;

	/**
	 * 
	 * @param object is genralized as hashObject.
	 */
	public HashObject(T object) {
		duplicateCount = probeCount = 0;
		this.setValue(object);
	}

	/**
	 * 
	 * @return returns the exact value the HashObject contains.
	 */
	public T getValue() {
		return Value;
	}

	/**
	 * 
	 * @param val sets as Value.
	 */
	public void setValue(T val) {
		this.Value = val;
	}

	/**
	 * keeps track of duplicate of that specific HashObject
	 */
	public void duplicateCounter() {
		duplicateCount++;
	}

	/**
	 * 
	 * @return returns duplicate count of that specific HashObject.
	 */
	public int getduplicateCount() {

		return duplicateCount;
	}

	/**
	 * keeps track of number of Probes of that specific HashObject
	 */
	public void probeCounter() {
		probeCount++;
	}

	/**
	 * 
	 * @return returns probe count of that specific HashObjecy
	 */
	public int getProbeCount() {
		return probeCount;

	}

	/**
	 * o compares two object if they are the same.
	 */
	@Override
	public int compareTo(T o) {
		if (o.equals(Value)) {
			return 0;
		}
		return -1;
	}

	/**
	 * compares if the two Objects are equal.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		HashObject<T> b = (HashObject<T>) o;

		if (this.Value.equals(b.getValue())) {
			return true;
		}

		return false;

	}

	/**
	 * Retrieves the hashCode Value.
	 */
	@Override
	public int hashCode() {
		return Value.hashCode();
	}
}
