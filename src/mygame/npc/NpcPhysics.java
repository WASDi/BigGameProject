package mygame.npc;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author wasd
 */
public class NpcPhysics extends CharacterControl implements Npc{
    
    private Node node;
    private Vector3f arrowTranslation;

    public NpcPhysics(float sizex, float sizey, Node node) {
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
    
}
