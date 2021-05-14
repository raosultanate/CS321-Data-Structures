import java.math.*;
import java.util.Random;

/*
 * @author Daniel Rao
 */
public class HashTable<T> {

	protected enum PROBING_TYPE {
		LINEAR_HASHING, DOUBLE_HASHING
	}

	private PROBING_TYPE probingType;
	private HashObject<T>[] table;
	private int tableSizem;
	private double loadFactor;
	private int totalnokeysExpected;
	private int hashObjectKey;
	private int primaryHashValue;
	private int secondaryHashValue;
	private int successfulInsertion;
	private int insertionAttempt;
	private int totalDuplicateCount;
	private int totalProbes;

	@SuppressWarnings("unchecked")
	public HashTable(HashTable.PROBING_TYPE probe, double loadFactor) {

		primeGenerator();
		this.probingType = probe;
		this.loadFactor = loadFactor;
		successfulInsertion = insertionAttempt = totalDuplicateCount = totalProbes = 0;
		this.totalnokeysExpected = (int) (this.loadFactor * tableSizem); // tableSizem
		table = (HashObject<T>[]) (new HashObject<?>[tableSizem]);

	}

	// this is important for linear-dump and double-dump
	/**
	 * 
	 * @param i refers to the each item in the table. --index
	 * @return the duplicate count for the each item in the table.
	 */
	public int getDups(int i) {
		return table[i].getduplicateCount();
	}

	/**
	 * @param i refers to the each item in the table. --index
	 * @return the probe count for the each item in the table.
	 */
	public int getProbes(int i) {
		return table[i].getProbeCount();

	}

	/**
	 * 
	 * @param i refers to the each item in the table. --index
	 * @return the object to the certain index of the table.
	 */
	public Object getObject(int i) {
		return table[i];
	}

	/**
	 * 
	 * @param i refers to the each item in the table. --index
	 * @return returns the Value of the HashObject.
	 */
	public Object getItem(int i) {
		return table[i].getValue();
	}

	/**
	 * 
	 * @return returns total number of probes.
	 */
	public double getTotalProbes() {

		return totalProbes;
	}

	/**
	 * 
	 * @return returns total number of duplicates.
	 */
	public int getTotalDuplicates() {
		return totalDuplicateCount;
	}

	/**
	 * 
	 * @return returns successful insertion.
	 */
	public double getCountofsuccessfulInsertion() {
		return successfulInsertion;
	}

	/**
	 * 
	 * @return returns total number of expected keys.
	 */
	public int getTotalExpectedNumberKeys() {
		return totalnokeysExpected;
	}

	/**
	 * 
	 * @return returns insertion attempt.
	 */
	public int getInsertionAttempt() {
		return insertionAttempt;
	}

	/**
	 * 
	 * @return returns the table size.
	 */
	public int getTableSize() {
		return table.length;
	}

	/**
	 * 
	 * @return returns the probingType of the HashTable.
	 */
	public PROBING_TYPE getProbingType() {
		return probingType;
	}

	/**
	 * 
	 * @param k is the h1.
	 */
	public void primaryHashFunction(int k) { // this one evaluates h1.

		primaryHashValue = k % tableSizem;
		if (primaryHashValue < 0) {
			primaryHashValue += tableSizem;

		}

	}

	/**
	 * 
	 * @param k is the h2
	 */
	public void secondaryHashFunction(int k) { // this one evaluates h2.

		secondaryHashValue = (k % (tableSizem - 2));
		if (secondaryHashValue < 0) {
			secondaryHashValue += (tableSizem - 2);

		}
		secondaryHashValue++;

	}

	/**
	 * 
	 * @param h1 key 1
	 * @param i  helps in iteration.
	 * @return
	 */
	public int linearProbingIndex(int h1, int i) { // this one for evaluating linearProbing hash function.

		int linearProbingIndex = (h1 + i) % tableSizem;

		return linearProbingIndex;

	}

	/**
	 * 
	 * @param h1 key1
	 * @param h2 key2
	 * @param i  helps in iteration
	 * @return
	 */
	public int doubleProbingIndex(int h1, int h2, int i) { // this one for evaluating doubleProbing hash function.

		int doubleProbingIndex = (h1 + (i * h2)) % tableSizem;

		return doubleProbingIndex;

	}

	/**
	 * 
	 * @param Value is insered into the HashTable, as linear and double hashing.
	 * @throws Exception
	 */
	public void insert(T Value) throws Exception {

		int j = 0;
		boolean hashFound = false; // works like a switch.
		HashObject<T> addObject = new HashObject<T>(Value);
		hashObjectKey = addObject.hashCode(); // before inserting the object into the table the hashCode is calculated.

		primaryHashFunction(hashObjectKey); // ==>primaryHashValue h1(k)
		secondaryHashFunction(hashObjectKey);

		while (hashFound == false) {
			int hashValue = 0;
			if (probingType == HashTable.PROBING_TYPE.LINEAR_HASHING) {
				hashValue = linearProbingIndex(primaryHashValue, j);
			} else {
				hashValue = doubleProbingIndex(primaryHashValue, secondaryHashValue, j);
			}
			if (table[hashValue] != null) {
				if (table[hashValue].equals(addObject)) {
					table[hashValue].duplicateCounter();
					totalDuplicateCount++;
					hashFound = true;
					// j = 0;
				}
				j++;
				addObject.probeCounter();

			} else {
				hashFound = true;
				addObject.probeCounter();
				table[hashValue] = addObject;
				totalProbes += addObject.getProbeCount();
				successfulInsertion++;
				j = 0;
			}
		}

		insertionAttempt++;

	}

	// this method determines if the number passed in is a primeNumber.
	/**
	 * 
	 * @param randomNumberp checks if the given number provided is a prime or not.
	 * @return true if is a prime else returns false.
	 */
	public boolean isprime(int randomNumberp) {
		String randomNumberpStr = Integer.toString(randomNumberp);
		BigInteger randomNumberpBig = new BigInteger(randomNumberpStr);

		Random rand = new Random();

		// randomA between 1 < a < p
		int randomA = rand.nextInt(randomNumberp - 2) + 2;
		String randomAStr = Integer.toString(randomA);
		BigInteger randomABig = new BigInteger(randomAStr);

		// After generating computing the function.
		int exponent = randomNumberp - 1;
		String exponentStr = Integer.toString(exponent);
		BigInteger exponentStrBig = new BigInteger(exponentStr);

		BigInteger resultAfterComputation = randomABig.modPow(exponentStrBig, randomNumberpBig);

		if (resultAfterComputation.compareTo(BigInteger.ONE) == 0) {

			int randomA2 = rand.nextInt(randomNumberp - 2) + 2;
			String randomAStr2 = Integer.toString(randomA2);
			BigInteger randomABig2 = new BigInteger(randomAStr2);
			BigInteger resultAfterComputation2 = randomABig2.modPow(exponentStrBig, randomNumberpBig);

			if (resultAfterComputation2.compareTo(BigInteger.ONE) == 0) {
				return true;
			}

		}
		return false;

	}

	/**
	 * 
	 * @param i checks if i is a twin prime.
	 * @return return 1 if is a prime number if not than returns a -1.
	 */
	public int twinprimePicker(int i) {

		if ((i - 2) > 95500) {

			if ((isprime(i) == true) && ((isprime(i - 2) == true))) {
				return i;
			}
		}

		return -1;

	}

	/**
	 * generates prime number within the given range,
	 */
	public void primeGenerator() {

		int i = 95500;
		while (i != 96000) {
			int j = twinprimePicker(i);
			if (j != -1) {
				tableSizem = j;
				break;
			}
			i++;

		}
	}

}
