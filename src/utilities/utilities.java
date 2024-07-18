package utilities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class utilities {

    /*
     * Searchs an elements using the grep command. It's NOT case sensitive.
     * @param input: the element to search.
     * @param DATA: the list of elements to search in.
     * @return: the element if it is found, null otherwise.
     * 
     */
    public static Boolean grepSearch(String input, String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            
            while ((line = br.readLine()) != null) {
                if (input.equalsIgnoreCase(line.trim())) {
                    
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }


    /**
     * Retuns a list of N elements, filled accordingly to the partitionNumber . 
     * For example, if we have a list of N elements and we want to partition it in 3/4, 
     * 3/4 of the elements will be elements of the first csv, and the rest will be elements of the second csv.
     *  
     * @return a list of strings with the elements of the first csv.
     */
    public static List<String> partitionCSV(String filepath1, String filepath2, double partitionNumber, int N) {
        List<String> result = new ArrayList<>(N);
        List<String> elements1 = readCSV(filepath1, ",");
        List<String> elements2 = readCSV(filepath2, ";");

        // Calcular la cantidad de elementos para cada partición
        int partitionSize1 = (int) Math.round(N * partitionNumber);
        int partitionSize2 = N - partitionSize1;
        System.out.println("Partition size 1: " + partitionSize1 + ", Partition size 2: " + partitionSize2);

        // Seleccionar elementos aleatoriamente de la primera lista
        Collections.shuffle(elements1, new Random());
        for (int i = 0; i < partitionSize1 && i < elements1.size(); i++) {
            result.add(elements1.get(i));
        }

        // Crear un conjunto para verificar duplicados
        Set<String> elements1Set = new HashSet<>(elements1);

        // Seleccionar elementos aleatoriamente de la segunda lista
        Collections.shuffle(elements2, new Random());
        int i = 0;
        int addedCount = 0;
        while (addedCount < partitionSize2 ) {

            String element = elements2.get(i);
            result.add(element);
            addedCount++;
            if (i == elements2.size()-1 ) {
                i=0;
            }else {
                i++;
            }
        }

        return result;
    }
    /**
     * Reads the DATA on the specified CSV file according to the DATA number
     */
    public static List<String> readCSV(String filePath, String separator) {
        List<String> DATA = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                // Saltar líneas en blanco
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(separator);
                for (String cell : data) {
                    // Dividir cada celda en palabras, manteniendo apóstrofes y otros caracteres especiales
                    String[] words = cell.split("\\s+");
                    for (String word : words) {
                        DATA.add(word);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return DATA;
    }
    
}
