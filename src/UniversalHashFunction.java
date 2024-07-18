
public class UniversalHashFunction implements HashFunction1 {
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


    @Override 
    public String toString() {
        return "a: " + a + ", b: " + b + ", p: " + p + ", m: " + m;
    }

}