package particle;

import geometry.Vec3f;
import processing.core.PApplet;

public class Node {
    private Vec3f position;
    private Vec3f size;
    private Vec3f direction;
    private PApplet p;

    public Node(PApplet p, Vec3f position, Vec3f direction) {
        this.p = p;
        this.position = position;
        this.direction = direction;
        size = new Vec3f(0.2f, 0.5f, 0.2f);
    }

    public void show() {
        p.pushMatrix();
        
        p.translate(position.x, position.y, position.z);

        Vec3f up = new Vec3f(0f, 1f, 0f);
        Vec3f axis = up.cross(direction);
        float angle = PApplet.acos(up.dot(direction) / (up.mag() * direction.mag())); 
        p.rotate(angle, axis.x, axis.y, axis.z);
        
        p.scale(size.x, size.y, size.z);
        p.fill(0, 255, 255, 128); 
        p.sphere(10);
        
        p.popMatrix();

    }

}