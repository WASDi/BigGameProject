package mygame.npc;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mygame.ResourceLoader;
import mygame.quest.DeliveryQuest;
import mygame.quest.KillQuest;
import mygame.quest.QuestManager;
import mygame.quest.StoryQuest;

/**
 * This class creates a list with every Npc that will
 * be added to the game by InGameAppState.
 * 
 * @author wasd
 */
public class NpcManager {
    
    private ResourceLoader loader;
    private QuestManager questManager = new QuestManager();
    private List<Npc> npcList = new ArrayList<Npc>();

    public NpcManager(ResourceLoader loader) {
        this.loader = loader;
    }
    
    private StaticNpc createSandGuy(float x, float y, float z, float xlook, float zlook, String name){
        Spatial model = loader.getSandGuyModel();
        model.setLocalTranslation(0, -2.1f, 0); //centers the model
        Node node = new Node("SandGuy_"+name);
        node.attachChild(model);
        
        StaticNpc control = new StaticNpc(node, name, loader.getQuestMarker());
        npcList.add(control);
        node.addControl(control);
        control.setPosition(x,y,z);
        control.lookAt(xlook, zlook);
        return control;
    }
    private Npc createCrab(float x, float y, float z){
        Spatial model = loader.getCrabModel();
        model.setLocalRotation(new Quaternion().fromAngles(0, -FastMath.HALF_PI, 0));
        model.setLocalTranslation(0, -4.1f, 0); //centers the model
        Node node = new Node("Crab");
        node.attachChild(model);
        
        PhysicNpc control = new PhysicNpc(2, .5f, node, 100);
        npcList.add(control);
        node.addControl(control);
        control.setPosition(x,y,z);
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
        if(bestDistance>15f){
            //no close npc found
            return null;
        }
        return closest;
    }
    
    public Iterator<Npc> getNpcIterator(){
        return npcList.iterator();
    }
    
    /**
     * Initializes every Npc in the game and links quest between them if they have any.
     * @return List of Npc to be added to the game
     */
    public List<Npc> loadNpcs(){
        
        //Sand people have sand-names like Sandra
        
        StaticNpc sandberg = createSandGuy(204.1f, -0.3f, 207.9f, 0.37331f, -0.65049f, "Sandberg");
        StaticNpc sanderella = createSandGuy(200.0f, -1.3f, 198.1f, 0.72915f, 0.17561f, "Sanderella");
        
        StaticNpc sandy = createSandGuy(211.5f, 2.7f, 247.0f, -0.31863f, 0.38533f, "Sandy");
        StaticNpc mcSand = createSandGuy(201.9f, 3.6f, 258.6f, 0.47794f, -0.57799f, "McSand");
        
        StaticNpc sandersson = createSandGuy(311.7f, -0.8f, 245.0f, 0.96285f, -0.27004f, "Sandersson");
//        sandersson.setSay("Welcome to our island! Your spaceship must have crashed.\n"
//                + "Go and talk to Sandberg friends over there. He got some stuff for you to do.\n"
//                + "People with yellow spheres over their head have important stuff to say.");
        
        for(int z=0;z<3;z++){
            createCrab(255+z*10, 0f, 260+z*10);
        }
        
        KillQuest kq = questManager.createKillQuest(mcSand, "Crab", 3, null);
        kq.setPredefinedStartSay("Can you kill 3 crabs and collect their meat for me?");
        kq.setPredefinedEndSay("Thank you! I now have enough crab meat to complete the machine.");
        
        StoryQuest talkAboutFish = questManager.createStoryQuest(mcSand, null,
                //implicit thank you
                "With this extra fish, I can almost complete my fish-machine.",
                "I have been working on it for a long time.",
                "There is still work to do though...",
                "I need some crab meat to complete the machine.");
        
        DeliveryQuest fishToSandberg = questManager.createDeliveryQuest(sandberg, mcSand, "fish", talkAboutFish);
                //implicit "please deliver this fish
        
        StoryQuest fishStory = questManager.createStoryQuest(sandberg, fishToSandberg,
                "Hello! You must be new here",
                "My friend McSand is making a fish-machine. But he is out of fish...");
        
        StoryQuest intro = questManager.createStoryQuest(sandersson, fishStory,
                "Welcome to our island! Your spaceship must have crashed.",
                "People with yellow spheres over their head have important stuff to say.",
                "Go and talk to Sandberg over there. I think he has some work for you.");
        intro.onStart();
        
        return npcList;
    }
    
    public void onNpcKill(Npc npc){
        questManager.onNpcKill(npc);
        npcList.remove(npc);
        //TODO do something with npc. Or maybe let it do stuff in its own method
    }
    
}
