
public class UniversalHashFunction implements HashFunction {
    private final int a, b, p, m;

    public UniversalHashFunction(int m, int p, int a, int b) {
        this.m = m;
        this.p = p;
        this.a = a;
        this.b = b;
    }

    @Override
    public int hash(String input) {

        return (Math.abs(a * input.hashCode() + b) % p) % m;
    }

}