package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

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

    public NpcFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public Spatial getSandGuy(){
        return null;
    }
    
}
