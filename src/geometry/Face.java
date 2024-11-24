package geometry;

import java.util.Arrays;

import enums.Colors;
import processing.core.PApplet;
import processing.core.PConstants;

public class Face {
    public HalfEdge he;
    public boolean color;
    private PApplet p;
    public Vec3f normal;
    
    public Face(PApplet p, HalfEdge h) {
        this.p = p;
        he = h;
        color = false;

        Vec3f AB = new Vec3f(he.vertex, he.next.vertex);
        Vec3f AC = new Vec3f(he.vertex, he.next.next.vertex);
    
        normal = AB.cross(AC);
        normal.normalize();
    }

    public boolean intersectRayTriangle(Ray ray, Vec3f tuv) {
        Vec3f A = he.vertex;
        Vec3f B = he.next.vertex;
        Vec3f C = he.next.next.vertex;

        Vec3f e1 = new Vec3f(A, B);
        Vec3f e2 = new Vec3f(A, C);
        
        Vec3f h = ray.getDirection().cross(e2);
        
        float a = e1.dot(h);
        
        if (Math.abs(a) < 1e-6) return false;
        
        float f = 1.0f / a;
        Vec3f s = new Vec3f(A, ray.getOrigin());
        
        tuv.y = f * s.dot(h);    
        if (tuv.y < 0.0f || tuv.y > 1.0f) return false;
        
        Vec3f q = s.cross(e1);
        tuv.z = f * ray.getDirection().dot(q);
        
        if (tuv.z < 0.0f || tuv.y + tuv.z > 1.0f) return false;
        
        tuv.x = f * e2.dot(q);
        
        return tuv.x > 1f;
    }

    public int getColor() {

        int r = (int) ((normal.x + 1) * 127.5);
        int g = (int) ((normal.y + 1) * 127.5);
        int b = (int) ((normal.z + 1) * 127.5);
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    public Vec3f getPointInTriangle(float u, float v) {
        Vec3f U = new Vec3f(he.vertex, he.next.vertex);
        Vec3f V = new Vec3f(he.vertex, he.next.next.vertex);

        Vec3f res = he.vertex.copy();
        res.add(Vec3f.mult(U, u));
        res.add(Vec3f.mult(V, v));
        return res;
    }


    public void show() {
        int color = getColor();
        HalfEdge current = he;
        p.fill(color);
        if(this.color)
            p.fill(Colors.FOCUS_FACE.argb);
        p.beginShape(PConstants.TRIANGLES);
        do {
            p.vertex(current.vertex.x, current.vertex.y, current.vertex.z);
            current = current.next;
        } while (!he.equals(current));
        p.endShape();

        Vec3f m = Vec3f.mult(he.vertex.copy().add(he.next.vertex).add(he.next.next.vertex), 1f/3f);
        //p.line(m.x, m.y, m.z, m.x + normal.x * 5, m.y + normal.y * 5, m.z + normal.z * 5);
    }

    public boolean isIn(Vec3f position){
        // projection
        Vec3f N = this.normal;  // Normale du plan du triangle
        Vec3f XminusP1 = new Vec3f(this.he.vertex, position.copy());
        float projectionScale = XminusP1.dot(N) / N.dot(N);  // Calcul du facteur de projection
        Vec3f projectionX = new Vec3f(Vec3f.mult(N.copy(), projectionScale), position.copy());  // Projection du point X sur le plan


        // test si intérieur
        Vec3f v0 = new Vec3f(this.he.vertex, this.he.next.vertex);  // P1 -> P2
        Vec3f v1 = new Vec3f(this.he.vertex, this.he.next.next.vertex);  // P2 -> P3
        Vec3f v2 = new Vec3f(this.he.vertex, projectionX);  // P3 -> P1
        
        // calcul des coordonnées barycentriques
        float d00 = v0.dot(v0);
        float d01 = v0.dot(v1);
        float d11 = v1.dot(v1);
        float d20 = v2.dot(v0);
        float d21 = v2.dot(v1);

        float denom = d00 * d11 - d01 * d01;
        float v = (d11 * d20 - d01 * d21) / denom;
        float w = (d00 * d21 - d01 * d20) / denom;
        float u = 1.0f - v - w;

        return u >= 0 && v >= 0 && w >= 0;
    }
    public Face findOtherFace(Vec3f position, Vec3f direction){
        Vec3f c1 = new Vec3f(position, he.vertex);
        Vec3f c2 = new Vec3f(position, he.next.vertex);
        Vec3f c3 = new Vec3f(position, he.next.next.vertex);
        Vec3f[] vectors = { c1, c2, c3, direction};

        Vec3f[] sortedVectors = sortVectorsByAngle(vectors, normal);
        Vec3f borne1 = null;
        Vec3f borne2 = null;
        for(int i = 0; i < sortedVectors.length; i++){
            if(sortedVectors[i].equals(direction)){
                borne1 = sortedVectors[(i - 1 + sortedVectors.length) % sortedVectors.length];
                borne2 = sortedVectors[(i + 1) % sortedVectors.length];
                break;
            }
        }

        Vec3f first = null;
        Vec3f last = null;

        if(borne1.equals(c1))
            first = he.vertex;
        else if(borne1.equals(c2))
            first = he.next.vertex;
        else
            first = he.next.next.vertex;
    
        if(borne2.equals(c1))
            last = he.vertex;
        else if(borne2.equals(c2))
            last = he.next.vertex;
        else
            last = he.next.next.vertex;

        HalfEdge cote = new HalfEdge(first);
        HalfEdge coteInv = new HalfEdge(last);
        cote.setNext(coteInv);

        if(cote.equals(he))
            cote = he;
        else if(cote.equals(he.next))
            cote = he.next;
        else if(cote.equals(he.next.next))
            cote = he.next.next;
        else
            return null;
        if(cote.twin == null)  
            return null; 
        return cote.twin.face;
    }

    static Vec3f[] sortVectorsByAngle(Vec3f[] vectors, Vec3f normal) {
        Vec3f u = new Vec3f(1, 0, 0);
        Vec3f v = normal.cross(u).normalize();

        return Arrays.stream(vectors).sorted((a, b) -> {
            float angleA = (float) Math.atan2(a.dot(v), a.dot(u));
            float angleB = (float) Math.atan2(b.dot(v), b.dot(u));
            return Float.compare(angleA, angleB);
        }).toArray(Vec3f[]::new);
    }
} 