package geometry;

import processing.core.PVector;

public class HalfEdge {
    public Vec3f vertex;
    public Face face = null;
    public HalfEdge next = null, twin = null;

    public HalfEdge(Vec3f v) {
        vertex = v;
    }

    public HalfEdge(PVector v) {
        this(new Vec3f(v));
    }

    public HalfEdge setFace(Face f) {
        face = f;
        return this;
    }

    public HalfEdge setNext(HalfEdge h) {
        next = h;
        return this;
    }

    public HalfEdge setTwin(HalfEdge h) {
        twin = h;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        HalfEdge other = (HalfEdge) obj;
        return (vertex.equals(other.vertex) && next.vertex.equals(other.next.vertex)) 
            || (next.vertex.equals(other.vertex) && vertex.equals(other.next.vertex));
    }

    @Override
    public int hashCode() {
        return vertex.hashCode() ^ next.vertex.hashCode();
    }
}
