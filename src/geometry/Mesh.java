package geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

        // pour chaque triangle

        // ajout du premier avec un sens choisi arbitrairement
        Triple<PVector> first = triangles.get(0);

        HalfEdge h1 = new HalfEdge(first.getA());
        HalfEdge h2 = new HalfEdge(first.getB());
        HalfEdge h3 = new HalfEdge(first.getC());

        h1.setNext(h2);
        h2.setNext(h3);
        h3.setNext(h1);

        Face f = new Face(h1);

        h1.setFace(f);
        h2.setFace(f);
        h3.setFace(f);
        

        halfedgesMap.put(h1, h1);
        halfedgesMap.put(h2, h2);
        halfedgesMap.put(h3, h3);
        faces.add(f);

        triangles.remove(0);

        while (!triangles.isEmpty()) {
            Iterator<Triple<PVector>> iterator = triangles.iterator();
            while (iterator.hasNext()) {
                Triple<PVector> triangle = iterator.next();
                HalfEdge existingHalfEdge = getExistingHalfEdge(triangle); 
                if ( existingHalfEdge != null) {
                    PVector lastVertex;
                    if(!triangle.getA().equals(existingHalfEdge.vertex) && !triangle.getA().equals(existingHalfEdge.next.vertex))
                        lastVertex = triangle.getA();
                    else if (!triangle.getB().equals(existingHalfEdge.vertex) && !triangle.getB().equals(existingHalfEdge.next.vertex)) 
                        lastVertex = triangle.getB();
                    else
                        lastVertex = triangle.getC();

                    // On inverse la rotation
                    h1 = new HalfEdge(existingHalfEdge.vertex);
                    h2 = new HalfEdge(existingHalfEdge.next.vertex);
                    h3 = new HalfEdge(lastVertex);
            
                    h1.setNext(h3);
                    h2.setNext(h1);
                    h3.setNext(h2);

                    existingHalfEdge.setTwin(h2);
                    h2.setTwin(existingHalfEdge);

                    f = new Face(h1);
                    h1.setFace(f);
                    h2.setFace(f);
                    h3.setFace(f);


                    halfedgesMap.put(h1, h1);
                    halfedgesMap.put(h2, h2);
                    halfedgesMap.put(h3, h3);
                    faces.add(f);
                    iterator.remove();

                }
            }
        }
    }

    private HalfEdge getExistingHalfEdge(Triple<PVector> triangle){
        HalfEdge h1 = new HalfEdge(triangle.getA());
        HalfEdge h2 = new HalfEdge(triangle.getB());
        HalfEdge h3 = new HalfEdge(triangle.getC());

        h1.setNext(h2);
        h2.setNext(h3);
        h3.setNext(h1);

        HalfEdge exist = halfedgesMap.get(h1);
        if(exist != null)
            return exist;
        exist = halfedgesMap.get(h2);
        if(exist != null)
            return exist;
        return halfedgesMap.get(h3);
    }


    public int getColorBasedOnOrientation(Vec3f A, Vec3f B, Vec3f C) {
        Vec3f AB = new Vec3f(A, B);
        Vec3f AC = new Vec3f(A, C);
    
        Vec3f crossProduct = AB.cross(AC);
        crossProduct.normalize();
        int r = (int) ((crossProduct.x + 1) * 127.5);
        int g = (int) ((crossProduct.y + 1) * 127.5);
        int b = (int) ((crossProduct.z + 1) * 127.5);
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }
    
    public void show() {
        p.noStroke();

        for(Face f : faces){
            int color = getColorBasedOnOrientation(f.he.next.vertex, f.he.vertex, f.he.next.next.vertex);
            

            HalfEdge current = f.he;
            p.fill(color);
            p.beginShape(PConstants.TRIANGLES);
            do {
                p.vertex(current.vertex.x, current.vertex.y, current.vertex.z);
                current = current.next;
            } while (!f.he.equals(current));

            p.endShape();
        }
        
    }





}
