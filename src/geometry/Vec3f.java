package geometry;

import java.util.Objects;

import processing.core.PVector;

public class Vec3f extends PVector {
    
    public Vec3f(float x, float y, float z) {
        super(x, y, z);
    }
    
    public Vec3f(PVector p) {
        this(p.x, p.y, p.z);
    }

    public Vec3f(Vec3f a, Vec3f b) {
        this(b.x - a.x, b.y - a.y, b.z - a.z);
    }

    public Vec3f add(Vec3f v){
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vec3f copy() {
        return new Vec3f(this.x, this.y, this.z);
    }

    public Vec3f cross(Vec3f v){
        return new Vec3f(super.cross((PVector) v));
    }

    public Vec3f normalize(){
        return new Vec3f(super.normalize());
    }

    public static Vec3f mult(Vec3f v, float k){
        return new Vec3f(PVector.mult(v, k));
    }

    public Vec3f max(Vec3f v){
        return new Vec3f(Math.max(v.x, this.x), Math.max(v.y, this.y), Math.max(v.z, this.z));
    }

    public Vec3f min(Vec3f v){
        return new Vec3f(Math.min(v.x, this.x), Math.min(v.y, this.y), Math.min(v.z, this.z));
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vec3f other = (Vec3f) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

}
