package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import mygame.ResourceLoader;
import mygame.quest.DeliveryQuest;

/**
 * This class creates a list with every Npc that will
 * be added to the game by InGameAppState.
 * 
 * @author wasd
 */
public class NpcManager {
    
    private ResourceLoader loader;
    private List<Npc> npcList = new ArrayList<Npc>();

    public NpcManager(ResourceLoader loader) {
        this.loader = loader;
    }
    
    private Npc createSandGuy(float x, float y, float z, float xlook, float zlook, String name){
        Spatial model = loader.getSandGuyModel();
        model.setLocalTranslation(0, -2.1f, 0); //centers the model
        Node node = new Node("SandGuyNode");
        node.attachChild(model);
        
        StaticNpc control = new StaticNpc(node, name, loader.getQuestMarker());
        npcList.add(control);
        node.addControl(control);
        control.setPosition(x,y,z);
        control.lookAt(xlook, zlook);
        return control;
    }
    
    /**
     * Gets an NPC close to position
     * @param position The position
     * @return The closest Npc
     */
    public Npc getCloseNpc(Vector3f position){
        Npc closest = null;
        float bestDistance = -1f;
        for(Npc npc : npcList){
            
            float distance = npc.getPosition().distance(position);
            if(bestDistance==-1f){
                closest = npc;
                bestDistance = distance;
                continue;
            }
            if(distance<bestDistance){
                closest=npc;
                bestDistance=distance;
            }
        }
        return closest;
    }
    
    /**
     * Initializes every Npc in the game and links quest between them if they have any.
     * @return List of Npc to be added to the game
     */
    public List<Npc> loadNpcs(){
        Npc sandberg = createSandGuy(245.6f, -0.4f, 388.2f, -0.00041f, -0.00194f, "Sandberg");
        Npc sandy = createSandGuy(232.7f, 0.5f, 381.6f, 0.00014f, -0.00029f, "Sandy");
        Npc mcSand = createSandGuy(226.8f, 0.6f, 368.0f, 0.00174f, -0.00096f, "McSand");
        
        //TODO Some class like QuestFactory or a more organized way to initialize quests
        DeliveryQuest dq2 = new DeliveryQuest(sandy, sandberg, "Void Salts", null);
        DeliveryQuest dq1 = new DeliveryQuest(sandberg, mcSand, "Staff", dq2);
        dq1.onStart();
        
        return npcList;
    }
    
}
