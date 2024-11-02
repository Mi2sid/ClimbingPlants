package struct;

public class Triple<T> {
    private final T A;
    private final T B;
    private final T C;

    public Triple(T A, T B, T C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    public T getA() { return A; }
    public T getB() { return B; }
    public T getC() { return C; }
}
