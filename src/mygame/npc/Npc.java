package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.quest.Quest;

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
 
    public void setPosition(float x, float y, float z);
    
    /**
     * Looks at a direction
     */
    public void lookAt(float xlook, float zlook);
            
    /**
     * Called when the player has targeted the Npc
     * @param arrow The spatial to be attached to the Npc
     */
    public void onTargeted(Spatial arrow);
    
    /**
     * @return The Node that this Npc is
     */
    public Node getNode();
    
    /**
     * @return The name of the Npc
     */
    public String getName();
    
    /**
     * Adds a Quest to this Npc
     * @param quest The Quest
     */
    public void addQuest(Quest quest);
    
    /**
     * Used by Quest to increase or decrease numQuests of the Npc.
     * @param add true to add to the questNumber. false to subtract
     */
    public void questMarkerUpdate(boolean add);
    
    /**
     * Called by Player when he does an attack on the Npc
     * @param dmg How much health the Npc lost
     */
    public void onAttack(int dmg);
    
}
