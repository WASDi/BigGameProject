package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author wasd
 */
public class StaticNpc extends AbstractControl implements Npc{
    
    private Node node;
    private String name;

    public StaticNpc(Node node, String name) {
        this.node=node;
        this.name=name;
    }

    public String talk() {
        return "My name is "+name;
    }

    @Override
    protected void controlUpdate(float tpf) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector3f getPosition() {
        return node.getLocalTranslation();
    }

    public void onTargeted(Spatial arrow) {
        //TODO set translation of arrow based on npc size.
        arrow.setLocalTranslation(0, 4, 0);
        node.attachChild(arrow);
    }

    public void setPosition(float x, float y, float z) {
        node.setLocalTranslation(x, y, z);
    }

    public void lookAt(float xlook, float zlook) {
        node.lookAt(getPosition().add(xlook, 0, zlook), Vector3f.UNIT_Y);
    }

    public Node getNode() {
        return node;
    }
    
}
