import enums.Colors;
import geometry.Face;
import geometry.Mesh;
import geometry.Ray;
import geometry.Vec3f;
import particle.Node;
import particle.Strcuture;
import processing.core.PApplet;
import processing.event.MouseEvent;

import struct.Triple;

public class PApp extends PApplet {
    public static PApp applet;
    public Camera camera;
    public Mesh mesh;
    public Strcuture s;
    public Vec3f focusTri;
    public Face focusFace;

    public static final String objFolder = "./3d_model/";
    public static final String objFile = "house.obj";
    public static final int start = 2;

    public void settings() {
        size(1080, 720, P3D);
    }

    public void setup() {
        background(255);
        PApp.applet = this;
        camera = new Camera(this);
        mesh = new Mesh(this, loadShape(objFolder + objFile));
        s = new Strcuture(this);
        s.add(new Node(this, null ,new Vec3f(0f, 0f, 0f), new Vec3f(0f, -1f, 0f), null));
        focusTri = null;
        focusFace = null;
    }

    public void draw(){
        background(Colors.BACKGROUND.argb);

        lights();

        camera.update();
        camera.use();

        directionalLight(255, 255, 255, 0, 0.3f, 1);

        showFocusTriangle();
        mesh.show();

        drawPlane(mesh.aabbmax.y);

        s.show();
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
        focusFace = null;
        focusTri = null;
        float minDist = Float.MAX_VALUE;

        for (Face f : mesh.faces) {
            Vec3f tuv = new Vec3f(0f, 0f, 0f);
            f.color = false;
            if(f.intersectRayTriangle(ray, tuv)){
                if(focusFace == null || minDist > tuv.x) {
                    focusFace = f;
                    minDist = tuv.x;
                    focusTri = tuv.copy();
                }
            }
        }
        if(focusFace != null)
            focusFace.color = true;
    }

    public void keyPressed() {
        if(key == 'p')
            s.updatePlant();

        camera.keyPressed();
    }
    public void keyReleased(){
        camera.keyReleased();
    }

    public void mousePressed() {
        if(mouseButton == LEFT && focusFace != null){
            
            Vec3f point = focusFace.getPointInTriangle(focusTri.y, focusTri.z);
            
            Triple<Vec3f> dirNormPos = s.getData(0);
            
            Vec3f projectedDir = Vec3f.getOrientation(dirNormPos, focusFace, point);

            if(projectedDir.isPresqueNull() && dirNormPos.getB().equals(new Vec3f(0, 0, 0))){}
                //System.out.println("Impossible de placer une graine à la perpendiculaire, détermination de l'orientation impossible");
            else{
                for(int i = 0; i<start; i++)
                    s.add(new Node(this, focusFace, point, projectedDir, s.s.get(0)));

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

