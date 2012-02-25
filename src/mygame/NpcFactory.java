package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.controls.NpcControl;

/**
 * This class should have methods to return NPCs.
 * They will then be loaded from InGameAppState and get added to the world.
 * 
 * The methods will return spatials with CharacterControls attached.
 * Use spatial.getControl(CharacterControl.class) to get the control and add it to the physics.
 * 
 * @author wasd
 */
public class NpcFactory {
    
    private AssetManager assetManager;
    private ResourceLoader loader;

    public NpcFactory(AssetManager assetManager, ResourceLoader loader) {
        this.assetManager = assetManager;
        this.loader = loader;
    }
    
    public Node getSandGuy(float x, float y, float z){
        NpcControl control = new NpcControl(1, 2);
        Spatial model = loader.getSandFolkModel();
        model.setLocalRotation(new Quaternion().fromAngles(0, FastMath.PI/2, 0)); //fix the rotation
        model.setLocalTranslation(0, -1.95f, -1.6f); //centers the model
        Node node = new Node("SandGuyNode");
        node.attachChild(model);
        node.addControl(control);
        control.setPhysicsLocation(new Vector3f(x, y, z));
        return node;
    }
    
}
