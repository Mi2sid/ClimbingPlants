package geometry;

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
    }

} 