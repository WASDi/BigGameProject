package mygame.controls;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;

/**
 *
 * @author wasd
 */
public class NpcControl extends CharacterControl{

    public NpcControl(float sizex, float sizey) {
        super(new CapsuleCollisionShape(sizex, sizey), .1f);
    }
    
}
