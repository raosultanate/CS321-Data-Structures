
public class TreeObject<T> {

	private int freqCounter;
	private long key;

	public TreeObject(long key) {

		setKey(key);
		this.freqCounter = 1;

	}

	public void setKey(long key) {
		this.key = key;

	}

	public long getKey() {
		return key;
	}

	public void frequencyUpdate() {
		freqCounter++;
	}

	public int compareTo(TreeObject<T> obj) {
		if (key < obj.getKey())
			return 1;
		if (key > obj.getKey())
			return -1;
		else
			return 0;
	}

}
