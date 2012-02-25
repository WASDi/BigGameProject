package mygame.controls;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import mygame.Game;

/**
 *
 * @author wasd
 */
public class NpcControl extends CharacterControl{
    
    private Game app;

    public NpcControl(Game app, float sizex, float sizey) {
        super(new CapsuleCollisionShape(sizex, sizey), .1f);
        this.app = app;
    }
    
}
