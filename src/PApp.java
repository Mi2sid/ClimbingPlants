import enums.Colors;
import geometry.Face;
import geometry.Mesh;
import geometry.Ray;
import geometry.Vec3f;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class PApp extends PApplet {
    public static PApp applet;
    public Camera camera;
    public Mesh mesh;
    public static final String objFolder = "./3d_model/";
    public static final String objFile = "bunny.obj";
    public Ray lastRay = null;

    public void settings() {
        size(800, 600, P3D);
    }

    public void setup() {
        background(255);
        PApp.applet = this;
        camera = new Camera(this);
        mesh = new Mesh(this, loadShape(objFolder + objFile));
    }

    public void draw(){
        background(Colors.BACKGROUND.argb);

        lights();

        camera.update();
        camera.use();

        directionalLight(255, 255, 255, 0, 0.3f, 1);
        if(lastRay != null){
            stroke(255);
            line(lastRay.getOrigin().x, lastRay.getOrigin().y, lastRay.getOrigin().z, 
                lastRay.getOrigin().x + 100000 * lastRay.getDirection().x,
                lastRay.getOrigin().y + 100000 * lastRay.getDirection().y,
                lastRay.getOrigin().z + 100000 * lastRay.getDirection().z);
        }

        mesh.show();
        drawPlane(mesh.aabbmax.y);
    }

    public void drawPlane(float y){

        noStroke();
        int size = 10;
        int tileSize = 200;
        for(int i=-size; i<size; i++){
            for(int j=-size; j<size; j ++){
                if((i + j) % 2 == 0)
                    fill(Colors.PLANE2.argb);
                else
                    fill(Colors.PLANE1.argb);
                
                beginShape(QUADS);
                    vertex(i * tileSize, y, j * tileSize);
                    vertex((i + 1) * tileSize, y, j * tileSize);
                    vertex((i + 1) * tileSize, y, (j + 1) * tileSize);
                    vertex(i * tileSize, y, (j + 1) * tileSize);
                endShape();

            }
        }
    }

    public void keyPressed() {
        camera.keyPressed();
    }
    public void keyReleased(){
        camera.keyReleased();
    }

    public void mousePressed() {
        if(mouseButton == LEFT){
            Ray ray = camera.getRay(mouseX, mouseY);
            lastRay = ray;
            System.out.println("A");
            System.out.println(ray.getDirection().x + " : " + ray.getDirection().y + " : " +ray.getDirection().z);
            for (Face f : mesh.faces) {
                Vec3f tuv = new Vec3f(0f, 0f, 0f);
                if(f.intersectRayTriangle(ray, tuv)){
                    f.color = true;
                    System.out.println("B");
                }
            }
        }
        camera.mousePressed();
    }
    
    public void mouseReleased() {
        camera.mouseReleased();
    }

    public void focusLost() {
        camera.resetPressedKeys();
        camera.mouseReset();
    }
      
    public void mouseWheel(MouseEvent event) {
        camera.mouseWheel(event);
    }

    public static void main(String[] args) {
        PApp processingApp = new PApp();
        PApplet.main(processingApp.getClass());
    }
}

