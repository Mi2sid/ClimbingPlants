package particle;

import java.util.ArrayList;

import geometry.Vec3f;
import struct.Pair;

public class Strcuture {
    public ArrayList<Node> s;

    public Strcuture(){
        s = new ArrayList<Node>();
    }
    
    public void add(Node n){
        s.add(n);
    }

    public void show() {
        for(Node n : s ){
            n.show();
        }
    }

    public Pair<Vec3f, Vec3f> lastOrientation(){
        if(s.get(s.size()-1).face == null)
            return new Pair<Vec3f, Vec3f>(s.get(s.size()-1).direction, new Vec3f(0f, 1f, 0f));
        return new Pair<Vec3f, Vec3f>(s.get(s.size()-1).direction, s.get(s.size()-1).face.normal);
    }



}
