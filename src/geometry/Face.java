package geometry;

public class Face {
    public HalfEdge he;
    public boolean color;
    
    public Face(HalfEdge h) {
        he = h;
        color = false;
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

} 