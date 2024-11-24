package particle;

import java.util.ArrayList;
import java.util.Arrays;

import geometry.Face;
import geometry.HalfEdge;
import geometry.Vec3f;
import processing.core.PApplet;
import processing.core.PConstants;
import struct.Triple;

public class Strcuture {
    public ArrayList<Node> s;
    public ArrayList<Node> top;
    public ArrayList<Node> tmp;
    private PApplet p;

    public Strcuture(PApplet p){
        s = new ArrayList<Node>();
        top = new ArrayList<Node>();
        tmp = new ArrayList<Node>();
        this.p = p;
    }
    
    public void add(Node n){
        top.add(n);
        s.add(n);

    }

    public void show() {
        for(Node n : s ){
            n.show();
        }
    }

    public void updatePlant(){
        tmp.clear();
        ArrayList<Node> delete = new ArrayList<Node>();
        for (Node n : top) {
            newNode(n);
            delete.add(n);
        }
        top.removeAll(delete);
        top.addAll(tmp);
        s.addAll(tmp);
    }


    public void newNode(Node n){
        Vec3f position = n.position.copy();
        
        Face newFace = n.face;
        Triple<Vec3f> last = getData(s.indexOf(n));
        Vec3f orientation;
        if(newFace == null){
            return;
        }

        orientation = Vec3f.getOrientation(last, newFace, position);
        float random = p.random(-Node.varProb, Node.varProb);
        orientation = Vec3f.rotateAroundAxis(orientation, newFace.normal, PApplet.radians(random));
        System.out.println(random);

        int energyImportance = energyUpdate(n, 0);
        for(int i=0; i<80; i++){
            position.add(Vec3f.mult(orientation, 0.1f));
            if (newFace.isIn(position)) {
                energyImportance = energyUpdate(n, energyImportance);
                //System.out.println("Le point X' est à l'intérieur du triangle.");
            } else {
                System.out.println("On change de face.");
                energyImportance = processEnergy(n, energyImportance);
                if(energyImportance > 20)
                    return;
                newFace = newFace.findOtherFace(last.getC(), orientation);

                if(newFace == null) break;

                orientation = Vec3f.getOrientation(last, newFace, position);
                last = new Triple<Vec3f>(orientation, newFace.normal, position);
            }
        }

        random = p.random(1f);
        if(random < Node.spawnProb){
            int sens = 1;
            if(random < Node.spawnProb / 2f)
                sens = -1;
            Vec3f newdir = Vec3f.rotateAroundAxis(n.direction, n.face.normal, sens * PApplet.radians(Node.varProb));
            Node ne = new Node(p, n.face, n.position, newdir, n.parent);
            tmp.add(ne);
        }


        Node ne = new Node(p, newFace, position, orientation, n);
        tmp.add(ne);
    }

    public Node getLast(){
        if(s.isEmpty())
            return null;
        return s.get(s.size()-1);
    }

    public Triple<Vec3f> lastOrientation(){
        if(s.get(s.size()-1).face == null)
            return new Triple<Vec3f>(s.get(s.size()-1).direction, new Vec3f(0f, 0f, 0f), s.get(s.size()-1).position);
        return new Triple<Vec3f>(s.get(s.size()-1).direction, s.get(s.size()-1).face.normal, s.get(s.size()-1).position);
    }

    public Triple<Vec3f> getData(int i){
        if(s.get(i).face == null)
            return new Triple<Vec3f>(s.get(i).direction, new Vec3f(0f, 0f, 0f), s.get(i).position);
        return new Triple<Vec3f>(s.get(i).direction, s.get(i).face.normal, s.get(i).position);
    }

    private int processEnergy(Node n, int x){
        return x+1;
    }
    private int energyUpdate(Node n, int x) {
        return 0;
    }
}
