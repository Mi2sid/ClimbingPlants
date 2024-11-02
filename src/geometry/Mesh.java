package geometry;

import java.util.ArrayList;
import java.util.HashMap;

import enums.Colors;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;
import struct.Triple;

public class Mesh {
    public ArrayList<Face> faces = new ArrayList<Face>();
    private HashMap<HalfEdge, HalfEdge> halfedgesMap;
    private ArrayList<Triple<PVector>> triangles = new ArrayList<Triple<PVector>>();
    private PApplet p;
    private final static float SCALE = 1000f; 

    public Mesh(PApplet p, PShape shape) {
        halfedgesMap = new HashMap<HalfEdge, HalfEdge>();
        this.p = p;

        // all triangles
        for (int i = 0; i < shape.getChildCount(); i++) {
            PShape child = shape.getChild(i);
            for (int j = 0; j < child.getVertexCount() - 2; j++) {
                PVector v1 = child.getVertex(j).mult(SCALE);
                PVector v2 = child.getVertex(j + 1).mult(SCALE);
                PVector v3 = child.getVertex(j + 2).mult(SCALE);

                triangles.add(new Triple<PVector>(v1, v2, v3));
            }
        }
    }


    public void show() {
        p.noStroke();
        p.fill(Colors.RED.argb);
        p.beginShape(PConstants.TRIANGLES);
        for(Triple<PVector> t : triangles){
            p.vertex(t.getA().x, t.getA().y, t.getA().z);
            p.vertex(t.getB().x, t.getB().y, t.getB().z);
            p.vertex(t.getC().x, t.getC().y, t.getC().z);
        }
        p.endShape();
    }





}
