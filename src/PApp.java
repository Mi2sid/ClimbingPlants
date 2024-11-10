import enums.Colors;
import geometry.Face;
import geometry.Mesh;
import geometry.Ray;
import geometry.Vec3f;
import particle.Node;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class PApp extends PApplet {
    public static PApp applet;
    public Camera camera;
    public Mesh mesh;
    public Node n;

    public static final String objFolder = "./3d_model/";
    public static final String objFile = "house.obj";

    public void settings() {
        size(800, 600, P3D);
    }

    public void setup() {
        background(255);
        PApp.applet = this;
        camera = new Camera(this);
        mesh = new Mesh(this, loadShape(objFolder + objFile));
        n = new Node(this, new Vec3f(50f, 10f, 50f), new Vec3f(1f, 0f, 0f));
    }

    public void draw(){
        background(Colors.BACKGROUND.argb);

        lights();

        camera.update();
        camera.use();

        directionalLight(255, 255, 255, 0, 0.3f, 1);

        mesh.show();
        drawPlane(mesh.aabbmax.y);

        showFocusTriangle();
        n.show();
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


    private void showFocusTriangle() {
        Ray ray = camera.getRay(mouseX, mouseY);
        Face minFace = null;
        float minDist = Float.MAX_VALUE;
        for (Face f : mesh.faces) {
            Vec3f tuv = new Vec3f(0f, 0f, 0f);
            f.color = false;
            if(f.intersectRayTriangle(ray, tuv)){
                if(minFace == null || minDist > tuv.x) {
                    minFace = f;
                    minDist = tuv.x;
                }
            }
        }
        if(minFace != null)
            minFace.color = true;
    }

    public void keyPressed() {
        camera.keyPressed();
    }
    public void keyReleased(){
        camera.keyReleased();
    }

    public void mousePressed() {
        if(mouseButton == LEFT){
            
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

