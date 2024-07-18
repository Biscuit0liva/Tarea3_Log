import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import utilities.utilities;
import static utilities.utilities.grepSearch;
import static utilities.utilities.readCSV;



public class test {
    
    

    public static List<HashFunction1> createHashFunctions(int k, int p, int m) {
        Random random = new Random();
        List<HashFunction1> hashFunctions = new ArrayList<>();
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

        List<String> DATA = readCSV("src/Popular-Baby-Names-Final.csv",";" );

        String babiesCSV = "src/Popular-Baby-Names-Final.csv";
        String moviesCSV = "src/Film-Names.csv";
        
        List<Integer> Ns = Arrays.asList(
            (int) Math.pow(2, 10),
            (int) Math.pow(2, 12),
            (int) Math.pow(2, 14),
            (int) Math.pow(2, 16)
        );

        List<Double> Ps = Arrays.asList(0.0, 0.25, 0.5, 0.75, 1.0);
        List<String> babieStrings = utilities.readCSV(babiesCSV, ",");
        int n = babieStrings.size();            // numero de elementos que se agregaran al filtro
        try(PrintWriter writer = new PrintWriter("src/results.txt", "UTF-8")) {
            for (int N : Ns) {
                for (double P : Ps) {
                    writer.println("______________________________________");
                    int foundCount = 0;
                    int totalCount = 0;
                    writer.println("N = " + N + ", P = " + P);
                    List<String> partitionedData = utilities.partitionCSV(babiesCSV, moviesCSV, P, N);  // lista de elementos que se buscaran
                    
                    // Inicializar el filtro
                    double desired_fp = 0.01; // desired false positive rate
                    // Calculate the size of the bit array (m)
                    double bitArraySize = -((n * Math.log(desired_fp)) / Math.pow(Math.log(2), 2));
                    int m = (int) Math.ceil(bitArraySize);
                        
                    // Calculate the number of hash functions (k)
                    double numHashFunctions = (int) (m / n) * Math.log(2);
                    int k = (int) Math.round(numHashFunctions);
                    int p = findNextPrime(n); // prime number greater than n
                    //List<HashFunction1> hashFunctions = createHashFunctions(k, p, m);
                    List<HashFunction1> hashFunctions = new ArrayList<>();
                    for(int i = 1; i<k+1; i++){
                        // va creando las funciones de hash con murmur
                        hashFunctions.add(new Murmur3HashFunction(m, i));

                    }

                    BloomFilter bloomFilter = new BloomFilter(m, hashFunctions);
                    // se agregan los elementos al filtro
                    for (String element : babieStrings){
                        bloomFilter.add(element);
                    }
                    
                    if (partitionedData.size() != N) {
                        writer.println("Error: partitioned data size is not N");
                        return;
                    }
                    // Se realiza la busqueda sin filtro
                    writer.println("Searching elements using GREP");
                    long startTime = System.nanoTime();
                    for (String element : partitionedData) {
                        
                        
                        Boolean found = grepSearch(element, "src/Popular-Baby-Names-Final.csv");

                        if (found ){
                            foundCount++;
                        }
                        totalCount++;
                    }
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime);

                    writer.println("// Elements found using GREP: " + foundCount+ "/" + N +" with time:" + duration/1000000 + "ms");
                    writer.println("\n");

                    // Usar el filtro para buscar los elementos
                    foundCount = 0;
                    totalCount = 0;
                    int falsePositives = 0;
                    long startTimeBloom = System.nanoTime();
                    writer.println("Searching elements using BLOOM FILTER");
                    for(String element : partitionedData){
                        

                        Boolean foundBloom = bloomFilter.contains(element);
                        if (foundBloom){    // si esta en el filtro realiza la busqueda grep
                            if(grepSearch(element, "src/Popular-Baby-Names-Final.csv")){
                                foundCount++;
                            } else {    // caso falso positivo, grep no encontro el elemento
                                falsePositives++; 
                            }
                        }
                        totalCount++;
                    }
                    long endTimeBloom = System.nanoTime();
                    long durationBloom = (endTimeBloom - startTimeBloom);
                    writer.println("//Elements found using BLOOM FILTER: " + foundCount + "/" + N+ " with time:" + durationBloom/1000000 + "ms"+ "And parameters P = " + P + ", m = " + m + ", k = " + k);
                    writer.println("False positives: " + falsePositives);

                    
                }   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}