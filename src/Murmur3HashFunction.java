import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Murmur3HashFunction implements HashFunction1 {

    private final HashFunction guavaHashFunction;
    private final int m;
    private final int seed;

    public Murmur3HashFunction(int m, int seed) {
        this.m = m;
        this.seed = seed;
        // Usa murmur3_32_fixed() para evitar problemas con caracteres no BMP
        this.guavaHashFunction = Hashing.murmur3_32_fixed(seed);
    }

    @Override
    public int hash(String input) {
        // Aplica el hash y asegura que el valor esté en el rango adecuado para el bitset
        return Math.abs(guavaHashFunction.hashUnencodedChars(input).asInt()) % m;
    }

    @Override
    public String toString() {
        return "Murmur3 32-bit fixed hash function with bitset size: " + m;
    }
}
