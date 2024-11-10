package geometry;

public class Ray {
    private Vec3f origin;
    private Vec3f direction;

    public Ray(Vec3f origin, Vec3f direction){
        this.origin = origin;
        this.direction = direction;
    }

    public Vec3f getDirection() {
        return direction;
    }

    public Vec3f getOrigin() {
        return origin;
    }


}
