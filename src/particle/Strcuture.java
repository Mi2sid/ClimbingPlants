package particle;

import java.util.ArrayList;
import java.util.Arrays;

import geometry.Face;
import geometry.HalfEdge;
import geometry.Vec3f;
import processing.core.PApplet;
import struct.Triple;

public class Strcuture {
    public ArrayList<Node> s;
    private PApplet p;

    public Strcuture(PApplet p){
        s = new ArrayList<Node>();
        this.p = p;
    }
    
    public void add(Node n){
        s.add(n);
    }

    public void show() {
        for(Node n : s ){
            n.show();
        }
    }

    public void newNode(Node n){
        Vec3f position = n.position.copy();
        
        Face newFace = n.face;
        Triple<Vec3f> last = lastOrientation();
        Vec3f orientation = Vec3f.getOrientation(last, newFace, position);
        for(int i=0; i<80; i++){
            position.add(Vec3f.mult(orientation, 0.1f));
            if (newFace.isIn(position)) {
                //System.out.println("Le point X' est à l'intérieur du triangle.");
            } else {
                System.out.println("On change de face.");

                newFace = newFace.findOtherFace(last.getC(), orientation);

                orientation = Vec3f.getOrientation(last, newFace, position);
                last = new Triple<Vec3f>(orientation, newFace.normal, position);

                System.out.println(newFace.normal);
            }
        }
        Node ne = new Node(p, newFace, position, orientation);
        s.add(ne);
    }

    public Node getLast(){
        if(s.isEmpty())
            return null;
        return s.get(s.size()-1);
    }

    public Triple<Vec3f> lastOrientation(){
        if(s.get(s.size()-1).face == null)
            return new Triple<Vec3f>(s.get(s.size()-1).direction, new Vec3f(0f, 0f, 0f), null);
        return new Triple<Vec3f>(s.get(s.size()-1).direction, s.get(s.size()-1).face.normal, s.get(s.size()-1).position);
    }
}
