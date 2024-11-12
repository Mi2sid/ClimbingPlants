package geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import enums.Colors;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;
import struct.Triple;

public class Mesh {
    public ArrayList<Face> faces = new ArrayList<Face>();
    private HashMap<HalfEdge, HalfEdge> halfedgesMap;
    private PApplet p;
    public Vec3f aabbmin = new Vec3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
    public Vec3f aabbmax= new Vec3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

    public static float SCALE = 1f;
    public static boolean reverseYaxis = true;
    public static boolean stroke = true;

    public Mesh(PApplet p, PShape shape) {
        ArrayList<Triple<PVector>> triangles = new ArrayList<Triple<PVector>>();
        halfedgesMap = new HashMap<HalfEdge, HalfEdge>();
        this.p = p;

        // all triangles
        for (int i = 0; i < shape.getChildCount(); i++) {
            PShape child = shape.getChild(i);
            for (int j = 0; j < child.getVertexCount() - 2; j++) {
                PVector v1 = child.getVertex(j).mult(SCALE);
                PVector v2 = child.getVertex(j + 1).mult(SCALE);
                PVector v3 = child.getVertex(j + 2).mult(SCALE);

                if(reverseYaxis) {
                    v1.y *= -1;
                    v2.y *= -1;
                    v3.y *= -1;
                }

                aabbmax = aabbmax.max(new Vec3f(v1));
                aabbmin = aabbmin.min(new Vec3f(v1));

                aabbmax = aabbmax.max(new Vec3f(v2));
                aabbmin = aabbmin.min(new Vec3f(v2));

                aabbmax = aabbmax.max(new Vec3f(v3));
                aabbmin = aabbmin.min(new Vec3f(v3));

                triangles.add(new Triple<PVector>(v1, v2, v3));
            }
        }
        System.out.println("Min : x"+ aabbmin.x + "  y" + aabbmin.y + " z" + aabbmin.y + "\nMax : x"+ aabbmax.x + "  y" + aabbmax.y + " z" + aabbmax.y);


        Vec3f padding = Vec3f.mult(new Vec3f(aabbmin, aabbmax), -0.5f);
        //padding.y = 0f;
        padding.add(Vec3f.mult(aabbmin, -1));
        for (Triple<PVector> t : triangles) {
            t = new Triple<PVector>(
                t.getA().add(padding),
                t.getB().add(padding),
                t.getC().add(padding)
            );
        }
        aabbmax.add(padding);
        aabbmin.add(padding);

        // pour chaque triangle

        // ajout du premier avec un sens choisi arbitrairement
        Triple<PVector> first = triangles.get(0);


        HalfEdge h1 = new HalfEdge(first.getA());
        HalfEdge h2 = new HalfEdge(first.getB());
        HalfEdge h3 = new HalfEdge(first.getC());

        h1.setNext(h2);
        h2.setNext(h3);
        h3.setNext(h1);



        Face f = new Face(p, h1);

        h1.setFace(f);
        h2.setFace(f);
        h3.setFace(f);
        

        halfedgesMap.put(h1, h1);
        halfedgesMap.put(h2, h2);
        halfedgesMap.put(h3, h3);
        faces.add(f);

        triangles.remove(0);

        int old = -1;
        int a = 0;
        while (!triangles.isEmpty()) {
            if(old == triangles.size()){
                System.err.println("Le chargement du mesh n'as pas pu s'effectuer correctement.\n" + a + " triangles ont été chargés correctement.");  
                break;
            }
            old = triangles.size();
            Iterator<Triple<PVector>> iterator = triangles.iterator();
            while (iterator.hasNext()) {
                Triple<PVector> triangle = iterator.next();
                HalfEdge existingHalfEdge = getExistingHalfEdge(triangle); 
                if ( existingHalfEdge != null) {
                    a ++;
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

                    f = new Face(p, h1);
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
        halfedgesMap.clear();
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
    
    public void show() {
        if(stroke)
            p.stroke(0);
        else
            p.noStroke();

        for(Face f : faces){
            f.show();
        }
    }





}
