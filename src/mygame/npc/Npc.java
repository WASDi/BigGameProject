package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author wasd
 */
public interface Npc {
    
    /**
     * @return A String with a sentenec that the Npc says
     */
    public String talk();

    /**
     * @return Where the Npc is located in the 3D space
     */
    public Vector3f getPosition();
    
    /**
     * Called when the player has targeted the Npc
     * @param arrow The spatial to be attached to the Npc
     */
    public void onTargeted(Spatial arrow);
    
}
