package EntitiesAndModels;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {

    private Vector3f currentRay;

    private Matrix4f projectionMatrix;

    public MousePicker(){
    }

    public Vector3f getCurrentRay(){
        return currentRay;
    }

    public float[] calculateMouseRay(){
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        float [] normalizedCoords = getNormalizedDeviceCoords(mouseX,mouseY);
        //System.out.println("Mouse X: "+normalizedCoords[0]+" Mouse Y: "+ normalizedCoords[1]);
        return normalizedCoords;
    }

    private float[] getNormalizedDeviceCoords(float mouseX, float mouseY){
        float x = (2f*mouseX) / Display.getWidth() - 1;
        float y = (2f*mouseY) / Display.getHeight() - 1f;
        return new float[] {x,y};
    }

}
