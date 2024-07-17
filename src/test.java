import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import utilities.utilities;
import static utilities.utilities.grepSearch;
import static utilities.utilities.readCSV;



public class test {
    
    

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
        List<String> DATA = readCSV("src/Popular-Baby-Names-Final.csv",";" );

        String babiesCSV = "src/Popular-Baby-Names-Final.csv";
        String moviesCSV = "src/Film-Names.csv";
        
        List<Integer> Ns = Arrays.asList(
            (int) Math.pow(2, 10),
            (int) Math.pow(2, 12),
            (int) Math.pow(2, 14),
            (int) Math.pow(2, 16)
        );

        List<Double> Ps = Arrays.asList(0.0,0.25,0.5,0.75,1.0);

        for (int N : Ns) {
            for (double P : Ps) {
                int foundCount = 0;
                int totalCount = 0;
                System.err.println("N = " + N + ", P = " + P);
                List<String> partitionedData = utilities.partitionCSV(babiesCSV, moviesCSV, P, N);
                if (partitionedData.size() != N) {
                    System.err.println("Error: partitioned data size is not N");
                    return;
                }
                for (String element : partitionedData) {
                    System.err.print("\rProgress: " + totalCount + "/" + N);
                    


                    Boolean found = grepSearch(element, "src/Popular-Baby-Names-Final.csv");

                    if (found ){
                        foundCount++;
                    }
                    totalCount++;
                }
                System.err.println("\n");
                System.err.println("Elements found: " + foundCount+ "/" + N);
                
                
            }
            
        }



    }

    public static void simpleTest (List<String> DATA) {

        double fraction = 0.7 ; // fraction of the universe that we want to use
        int N = (int) (DATA.size() * fraction); // size of the universe
        double desired_fp = 0.01; // desired false positive rate
        int m = (int) (-N * Math.log(desired_fp) / Math.pow(Math.log(2), 2)); // size of the bit array

        //then, we create the k-hash functions.
        int k = (int)(m/N * Math.log(2)); // number of hash functions
        int p = findNextPrime(N); // prime number greater than N
        List<HashFunction> hashFunctions = createHashFunctions(k,p,m);
        BloomFilter bloomFilter = new BloomFilter(m,hashFunctions);
        System.err.println("Bloom filter created with parameters: m = " + m + ", k = " + k + ", p = " + p  + ", N = " + N + "/" + DATA.size());
        List<String> auxList = new ArrayList<>(); // auxiliary list to store added elements
        Random random = new Random();
        int i = 0;

        while ( i< N ) {
            int randomIndex = random.nextInt(DATA.size());
            String element = DATA.get(randomIndex);
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
        for (int index = 0; index < DATA.size(); index++) {
            String element = DATA.get(index);
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
    //Function to search for an element in the csv. It can be setted to use the bloom filter or not.

    public static boolean searchTest(String input,Boolean useBloomFilter, List<String> DATA){
        if (useBloomFilter){

            
        } else {
            if (grepSearch(input, "src/Popular-Baby-Names-Final.csv")){
                return true;
            }
            else {
                return false;
            }

        }
        return false;
    }
}