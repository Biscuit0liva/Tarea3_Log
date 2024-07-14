import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test {
    /**
     * Reads the column on the specified CSV file according to the column number
     */
    public static List<String> readCSV(String filePath, int column_number) {
        List<String> column = new ArrayList<String>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                column.add(data[column_number]);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return column;
    }

    public static List<HashFunction> createHashFunctions(int k, int p, int m) {
        Random random = new Random();
        List<HashFunction> hashFunctions = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int a = random.nextInt(p - 1) + 1;
            int b = random.nextInt(p);
            hashFunctions.add(new UniversalHashFunction(m, p, a, b));
        }
        return hashFunctions;
    }

    public static int findNextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.err.println("Reading CSV file");
        List<String> column = readCSV("src/Popular-Baby-Names-Final.csv", 0);

        // now , we create the bloom filter
        // N will be the size of the universe that we are going to hash. Not necessarily
        // the number of elements in the set.
        double fraction = 0.7 ; // fraction of the universe that we want to use
        int N = (int) (column.size() * fraction); // size of the universe
        double desired_fp = 0.01; // desired false positive rate
        int m = (int) (-N * Math.log(desired_fp) / Math.pow(Math.log(2), 2)); // size of the bit array

        //then, we create the k-hash functions.
        int k = (int)(m/N * Math.log(2)); // number of hash functions
        int p = findNextPrime(N); // prime number greater than N
        List<HashFunction> hashFunctions = createHashFunctions(k,p,m);
        BloomFilter bloomFilter = new BloomFilter(m,hashFunctions);
        System.err.println("Bloom filter created with parameters: m = " + m + ", k = " + k + ", p = " + p  + ", N = " + N + "/" + column.size());
        List<String> auxList = new ArrayList<>(); // auxiliary list to store added elements
        Random random = new Random();
        int i = 0;

        while ( i< N ) {
            int randomIndex = random.nextInt(column.size());
            String element = column.get(randomIndex);
            if (!auxList.contains(element)) {
                bloomFilter.add(element);
                auxList.add(element);
                i++;
            }
        }
        System.err.println("Bloom filter filled with " + N + " elements");
        System.out.println("Searching elements...");
        // now we test the bloom filter
        int falsePositives = 0;
        int trueNegatives = 0;
        int truePositives = 0;
        int falseNegatives = 0;
        for (int index = 0; index < column.size(); index++) {
            String element = column.get(index);
            if (auxList.contains(element)) {
                if (!bloomFilter.contains(element)) {
                    falseNegatives++;
                } else {
                    truePositives++;
                }
            } else {
                if (bloomFilter.contains(element)) {
                    falsePositives++;
                } else {
                    trueNegatives++;
                }
            }
        }
        
        System.out.println("False positives: " + falsePositives);
        System.out.println("True negatives: " + trueNegatives);
        System.out.println("True positives: " + truePositives);
        System.out.println("False negatives: " + falseNegatives);

    }

}