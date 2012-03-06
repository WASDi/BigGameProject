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
public class NpcRegular extends AbstractControl implements Npc{
    
    private Node node;

    public NpcRegular(Node node) {
        this.node=node;
    }

    public String talk() {
        return "I am regular NPC";
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
        return spatial.getLocalTranslation();
    }

    public void onTargeted(Spatial arrow) {
        //TODO set translation of arrow.
        node.attachChild(arrow);
    }
    
}
