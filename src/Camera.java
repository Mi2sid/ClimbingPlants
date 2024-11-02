import java.util.HashMap;

import geometry.Vec3f;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Camera {

    protected class Quatior {
        protected Vec3f position;
        protected Vec3f front;
        protected Vec3f up;
        protected Vec3f right;

        Quatior(float distance) {
            this.position = new Vec3f(distance, 0f, 0f);

            this.up = new Vec3f(0f, 1f, 0f);
            this.front = Vec3f.mult(position, -1f).normalize();

            updateRight();
        }

        public void updatePosition(float distance) {
            position = Vec3f.mult(front, -distance);
        }

        public void updateRight(){
            right = up.cross(front).normalize();
        }

        public void updateUp(){
            up = front.cross(right).normalize(); 
        }

        public void rotateAroundUp(float angle) {
            up.normalize();
            float cosAngle = PApplet.cos(angle);
            float sinAngle = PApplet.sin(angle);
        
            front = new Vec3f(
                (cosAngle + (1 - cosAngle) * up.x * up.x) * front.x +
                ((1 - cosAngle) * up.x * up.y - up.z * sinAngle) * front.y +
                ((1 - cosAngle) * up.x * up.z + up.y * sinAngle) * front.z,
        
                ((1 - cosAngle) * up.x * up.y + up.z * sinAngle) * front.x +
                (cosAngle + (1 - cosAngle) * up.y * up.y) * front.y +
                ((1 - cosAngle) * up.y * up.z - up.x * sinAngle) * front.z,
        
                ((1 - cosAngle) * up.x * up.z - up.y * sinAngle) * front.x +
                ((1 - cosAngle) * up.y * up.z + up.x * sinAngle) * front.y +
                (cosAngle + (1 - cosAngle) * up.z * up.z) * front.z
            );
        
            updateRight();
        }

        public void rotateAroundRight(float angle) {
            right.normalize();
            float cosAngle = PApplet.cos(angle);
            float sinAngle = PApplet.sin(angle);
        
            front = new Vec3f(
                (cosAngle + (1 - cosAngle) * right.x * right.x) * front.x +
                ((1 - cosAngle) * right.x * right.y - right.z * sinAngle) * front.y +
                ((1 - cosAngle) * right.x * right.z + right.y * sinAngle) * front.z,
        
                ((1 - cosAngle) * right.x * right.y + right.z * sinAngle) * front.x +
                (cosAngle + (1 - cosAngle) * right.y * right.y) * front.y +
                ((1 - cosAngle) * right.y * right.z - right.x * sinAngle) * front.z,
        
                ((1 - cosAngle) * right.x * right.z - right.y * sinAngle) * front.x +
                ((1 - cosAngle) * right.y * right.z + right.x * sinAngle) * front.y +
                (cosAngle + (1 - cosAngle) * right.z * right.z) * front.z
            );
        
            updateUp();
        }
    }

    protected Quatior data;
    protected float rotationSpeed = 4f;
    protected float mouseSpeed = 0.7f;
    protected float zoomSpeed = 10f;

    protected float distance = 500f;
    protected PApplet p;
    protected HashMap<Character, Byte> pressedKeys = new HashMap<Character, Byte>();
    
    protected boolean isDragged = false;
    protected PVector oldPosition;
    
    Camera(PApplet p){
        this.p = p;
        this.data = new Quatior(distance);
        resetPressedKeys();
    }
    
    public void update() {
        if(isDragged) {
            PVector currentPosition = new PVector(p.mouseX, p.mouseY);
            data.rotateAroundRight( PApplet.radians( oldPosition.y - currentPosition.y ) * mouseSpeed);
            data.rotateAroundUp( PApplet.radians( oldPosition.x - currentPosition.x ) * mouseSpeed);

            oldPosition = currentPosition;
            data.updatePosition(distance);
            return;
        }


        if(pressedKeys.containsValue((byte) 1)){
            int zs = -((int) pressedKeys.get('s')) + (int) pressedKeys.get('z'); 
            data.rotateAroundRight(PApplet.radians(rotationSpeed) * zs);
            
            int qd = -((int) pressedKeys.get('d')) + (int) pressedKeys.get('q'); 
            data.rotateAroundUp(PApplet.radians(rotationSpeed) * qd);

            int ae = -((int) pressedKeys.get('a')) + (int) pressedKeys.get('e'); 
            distance = PApplet.max(0f, distance + zoomSpeed * ae);
        
            data.updatePosition(distance);
        }
    }

    public void resetPressedKeys(){
        char[] allUsedKeys = { 'z', 'q', 's', 'd', 'a', 'e' };
        for(char key : allUsedKeys)
            pressedKeys.put(key,(byte) 0);
    }

    public void use(){
        p.camera(data.position.x, data.position.y, data.position.z,
            0f,0f,0f,
            data.up.x, data.up.y, data.up.z);
    }
    
    public void keyReleased() {
        char key = Character.toLowerCase(p.key);
        pressedKeys.put(key, (byte) 0);
    }

    public void keyPressed() {
        char key = Character.toLowerCase(p.key);
        pressedKeys.put(key, (byte) 1);
    }

    public void mousePressed() {


        switch (p.mouseButton) {
            case PConstants.LEFT:
                
                break;
            case PConstants.CENTER:

                break;
            case PConstants.RIGHT:
                isDragged = true;
                oldPosition = new PVector(p.mouseX, p.mouseY);
                p.cursor(PConstants.MOVE);
                break;
            default:
                break;
        }

    } 

    public void mouseReleased() {
        switch (p.mouseButton) {
            case PConstants.LEFT:
                
                break;
            case PConstants.CENTER:

                break;
            case PConstants.RIGHT:
                isDragged = false;
                p.cursor(PConstants.ARROW);
                break;
            default:
                break;
        }
    } 

    public void mouseWheel(MouseEvent e){
        distance = PApplet.max(0f, distance + zoomSpeed * e.getCount());
        data.updatePosition(distance);
    }

    public void mouseReset() {
        p.cursor(PConstants.ARROW);
        isDragged = false;
    }

}
