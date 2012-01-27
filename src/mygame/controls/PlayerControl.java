package mygame.controls;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;

/**
 *
 * @author wasd
 */
public class PlayerControl extends CharacterControl{

    public PlayerControl() {
        super(new CapsuleCollisionShape(1, 2), .1f);
    }
    
}
