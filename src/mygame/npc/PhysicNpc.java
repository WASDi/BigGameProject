package mygame.npc;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.quest.Quest;

/**
 *
 * @author wasd
 */
public class PhysicNpc extends CharacterControl implements Npc{
    
    private Node node;
    private Vector3f arrowTranslation;

    public PhysicNpc(float sizex, float sizey, Node node) {
        super(new CapsuleCollisionShape(sizex, sizey), .1f);
        this.node = node;
        arrowTranslation = new Vector3f(0, sizey+1f, 0);
    }

    public String talk() {
        return "I am physics NPC";
    }

    public Vector3f getPosition() {
        return getPhysicsLocation();
    }

    public void onTargeted(Spatial arrow) {
        arrow.setLocalTranslation(arrowTranslation);
        node.attachChild(arrow);
    }

    public void setPosition(float x, float y, float z) {
        setPhysicsLocation(new Vector3f(x, y, z));
    }

    public void lookAt(float xlook, float zlook) {
        setViewDirection(getPosition().add(xlook, 0, zlook));
    }

    public Node getNode() {
        return node;
    }
    
    public String getName() {
        return "NAME_NOT_IMPLEMENTED";
    }

    public void addQuest(Quest quest) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void questMarkerUpdate(boolean add) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
