package EntitiesAndModels;

import org.lwjgl.util.vector.Vector3f;

public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotx,roty;
    private float scale;
    private boolean moved;

    public Entity(TexturedModel model, Vector3f position, float rotx, float roty, float scale) {
        this.model = model;
        this.position = position;
        this.rotx = rotx;
        this.roty = roty;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy){
        this.position.x += dx;
        this.position.y += dy;
    }

    public void increaseScale(){

    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotx() {
        return rotx;
    }

    public void setRotx(float rotx) {
        this.rotx = rotx;
    }

    public float getRoty() {
        return roty;
    }

    public void setRoty(float roty) {
        this.roty = roty;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void move(){
        if (moved){
            returnToTop();
        }
        else {
            moveToBottom();
        }
    }

    public void moveToBottom(){
        scale = 0.2f;
        position.y = -0.86f;
        moved = true;
    }

    public void returnToTop(){
        scale = 0.25f;
        position.y = 0f;
        moved = false;
    }

    public boolean getMoved(){
        return moved;
    }


}
