import java.util.BitSet;
import java.util.List;

/*
 * Bloom Filter
 * A Bloom filter is a data structure designed to tell you, rapidly and memory-efficiently, whether an element is present in a set.
 * In exchange with the possibility of false positives, have a space advantage over commonly hash tables, being better when the number of elements is large.
 * 
 * @var BitSet bitSet: the bitset that tells you whether an element is present or not.
 * @var int m: the m of the bloom filter.
 * @var List<HashFunction> hashFunctions: a list of hash functions that are used to hash the input.
 */
public class BloomFilter {

    private BitSet bitSet; // the bitset that tells you whether an element is present or not.
    private int m; // size of the M array.
    private List<HashFunction> hashFunctions; // there are k hashing funcs.

    /*
     * Constructor
     * 
     * @param m: the size of the bitset of the bloom filter
     * 
     * @param hashFunctions: a list of hash functions
     * 
     * @return:
     */
    public BloomFilter(int m, List<HashFunction> hashFunctions) {
        this.m = m; // set the size of the bitset
        this.bitSet = new BitSet(m); // initialize the bitset with m bits
        this.hashFunctions = hashFunctions; // initialize the hash functions
    }

    /*
     * Add
     * Add an element to the bloom filter
     * 
     * @param element: the element to be added
     * 
     * @return:
     */
    public void add(String element) {
        for (HashFunction hashFunction : hashFunctions) {
            int hash = hashFunction.hash(element);
            if (hash < 0) {
                throw new IllegalArgumentException("Hash value cannot be negative" + hash);
            }
            bitSet.set(hash);
        }
    }

    /*
     * Contain
     * Check if the element is in the bloom filter
     * 
     * @param element: the element to be checked
     * 
     * @return: true if the element is in the bloom filter, false otherwise
     */
    public boolean contains(String element) {
        for (HashFunction hashFunction : hashFunctions) {
            int hash = hashFunction.hash(element);
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }


    // Método para obtener la representación de cadena del BitSet
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.size(); i++) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }
}