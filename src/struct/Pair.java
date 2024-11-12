package struct;

public class Pair<T, U> {
    public final T G;
    public final U D;

    public Pair(T gauche, U droit) {
        this.G = gauche;
        this.D = droit;
    }
}
