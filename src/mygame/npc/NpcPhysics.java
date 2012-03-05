package mygame.npc;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;

/**
 *
 * @author wasd
 */
public class NpcPhysics extends CharacterControl implements Npc{

    public NpcPhysics(float sizex, float sizey) {
        super(new CapsuleCollisionShape(sizex, sizey), .1f);
    }

    public String talk() {
        return "I am physics NPC";
    }
    
}
