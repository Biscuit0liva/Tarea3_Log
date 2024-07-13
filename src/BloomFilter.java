import java.util.BitSet;
import java.util.List;

/*
 * Bloom Filter
 * A Bloom filter is a data structure designed to tell you, rapidly and memory-efficiently, whether an element is present in a set.
 * In exchange with the possibility of false positives, have a space advantage over commonly hash tables, being better when the number of elements is large.
 * 
 * @var BitSet bitSet: the bitset that tells you whether an element is present or not.
 * @var int size: the size of the bloom filter.
 * @var List<HashFunction> hashFunctions: a list of hash functions that are used to hash the input.
 */
public class BloomFilter {

    private BitSet bitSet;
    private int size;
    private List<HashFunction> hashFunctions;

    /* 
     * Constructor
     * @param size: the size of the bloom filter
     * @param hashFunctions: a list of hash functions
     * @return: 
     */
    public BloomFilter(int size, List<HashFunction> hashFunctions){
        this.size = size;
        this.bitSet = new BitSet(size);
        this.hashFunctions = hashFunctions;
    }

    /* 
     * Add
     * Add an element to the bloom filter
     * @param element: the element to be added
     * @return: 
     */
    public void add(String element){
        for(HashFunction hashFunctions : hashFunctions){
            int hash = hashFunctions.hash(element);
            bitSet.set(hash);
        }
    }

    /*
     * Contain 
     * Check if the element is in the bloom filter
     * @param element: the element to be checked
     * @return: true if the element is in the bloom filter, false otherwise
     */
    public boolean contain(String element){
        for(HashFunction hashFunction : hashFunctions){
            int hash = hashFunction.hash(element);
            if(!bitSet.get(hash)){
                return false;
            }
        }
        return true;
    }
}