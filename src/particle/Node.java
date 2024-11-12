package particle;

import enums.Colors;
import geometry.Face;
import geometry.Vec3f;
import processing.core.PApplet;

public class Node {
    private Vec3f position;
    private Vec3f size;
    public Vec3f direction;
    public Face face;
    private PApplet p;

    public Node(PApplet p, Face f, Vec3f position, Vec3f direction) {
        this.p = p;
        this.face = f;
        this.position = position;
        this.direction = direction.normalize();
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
        p.fill(Colors.PARTICLE.argb); 
        p.sphere(10);
        
        p.popMatrix();

    }

}