package mygame.npc;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.List;
import mygame.quest.Quest;

/**
 *
 * @author wasd
 */
public class StaticNpc extends AbstractControl implements Npc{
    
    private Node node;
    private String name;
    private String say;
    private Spatial questMarker;
    /**
     * Used to decide if questMarker should be displayed.
     * If it's more than 0, it will be displayed.
     * This is so that a quest wont remove the marker if another quest is active,
     * instead it will reduce the number from 2 to 1.
     */
    private int numQuests=0;
    
    //TODO somehow indicate in the game if the Npc has a quest avalible
    private List<Quest> quests;

    public StaticNpc(Node node, String name, Spatial questMarker) {
        this.node=node;
        this.name=name;
        this.questMarker=questMarker;
        
        questMarker.setLocalTranslation(0, 5, 0); //TODO set based on size of Npc
        node.attachChild(questMarker);
    }

    public String talk() {
        if(quests!=null){
            for(Quest q : quests){
                if (q.isActive()){
                    String reply = q.onTalk(this);
                    if(reply!=null)
                        return reply;
                }
            }
        }
        if(say!=null)
            return say;
        else
            return "I have nothing to say.";
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

    public String getName() {
        return name;
    }

    public void addQuest(Quest quest) {
        if(quests==null)
            quests = new ArrayList<Quest>();
        quests.add(quest);
    }

    public void questMarkerUpdate(boolean add) {
        int prev = numQuests;
        if(add)
            numQuests++;
        else
            numQuests--;
        
        if(numQuests<0)
            throw new Error("numQuest bellow zero.");
        
        if(prev==0 && numQuests>0){
            //display questMarker
            questMarker.setCullHint(Spatial.CullHint.Dynamic);
        }
        else if (numQuests==0 && prev>0){
            //hide questMarker
            questMarker.setCullHint(Spatial.CullHint.Always);
        }
    }

    public void setSay(String say) {
        this.say=say;
    }

    public void onAttack(int dmg) {
        //do nothing, StaticNpc can't be hit for now.
    }
    
}
