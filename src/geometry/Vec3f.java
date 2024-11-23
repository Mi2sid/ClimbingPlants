package geometry;

import java.util.Objects;

import processing.core.PVector;
import struct.Triple;

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

    public boolean isPresqueNull(){
        double epsilon = 1e-2;
        if(x < epsilon && x > -epsilon)
            if(y < epsilon && y > -epsilon)
                if(z < epsilon && z > -epsilon)
                    return true;
        return false;
    }

    public Vec3f projOnto(Vec3f normal) {
        float dotProduct = this.dot(normal);
        float normSquared = normal.magnitude() * normal.magnitude();
        return new Vec3f(new Vec3f(normal.x * dotProduct / normSquared, normal.y * dotProduct / normSquared, normal.z * dotProduct / normSquared), this);
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float squareMagnitude() {
        return x * x + y * y + z * z;
    }

    public static Vec3f intersection(Vec3f X, Vec3f U_direction, Vec3f Y, Vec3f V_direction) {
        // Résoudre pour t1 et t2 les équations paramétriques des deux vecteurs
        Vec3f diff = new Vec3f(X, Y); // Différence entre les points de départ

        // Calcul du déterminant (pour savoir si les vecteurs sont parallèles ou non)
        float denom = U_direction.x * V_direction.y - U_direction.y * V_direction.x;
        if (denom == 0) {
            return null; // Pas d'intersection, les vecteurs sont parallèles.
        }

        // Paramètres t1 et t2
        float t1 = (diff.x * V_direction.y - diff.y * V_direction.x) / denom;
        //float t2 = (diff.x * U_direction.y - diff.y * U_direction.x) / denom;

        // Calcul du point d'intersection
        
        return X.add(Vec3f.mult(U_direction, t1));
    }



    public static Vec3f getOrientation(Triple<Vec3f> dirNormPos, Face focusFace, Vec3f point){
        Vec3f dir = dirNormPos.getA().copy().normalize();
            
            float d = dir.dot(focusFace.normal);
            Vec3f projectedDir = new Vec3f(Vec3f.mult(focusFace.normal, d), dir);
            Vec3f newNorm = focusFace.normal.normalize();

            if(projectedDir.isPresqueNull() && !dirNormPos.getB().equals(new Vec3f(0, 0, 0))) {
                
                Vec3f oldNorm = dirNormPos.getB().copy().normalize();

                Vec3f oldPoint = dirNormPos.getC().copy();

                Vec3f vectorXP = new Vec3f(point, oldPoint);
                float dotProduct = vectorXP.dot(newNorm);
                System.out.println("par ici : " + dotProduct);
                if (dotProduct > 0) {
                    projectedDir = Vec3f.mult(oldNorm.copy(), 1);
                } else {
                    projectedDir = Vec3f.mult(oldNorm.copy(), -1);
                }
            }

            return projectedDir.projOnto(newNorm).normalize();
    }

    @Override
    public String toString(){
        return "(" + x + ";" + y + ";" + z +")";
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
