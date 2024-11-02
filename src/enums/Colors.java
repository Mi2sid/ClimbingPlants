package enums;

public enum Colors {
    BACKGROUND(75),
    RED(200, 0, 0);

    public final int argb;

    Colors(int r, int g, int b, int a){
        argb = (a << 24) | (r << 16) | (g << 8) | b;
    }

    Colors(int r, int g, int b){
        this(r, g, b, 255);
    }

    Colors(int rgb){
        this(rgb, rgb, rgb);
    }

    int get() {
        return argb;
    }
}  