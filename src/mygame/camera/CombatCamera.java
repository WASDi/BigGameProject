package mygame.camera;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 *
 * @author wasd
 */
public class CombatCamera extends ChaseCamera{

    public CombatCamera(Camera cam, Spatial target, InputManager inputManager) {
        super(cam, target, inputManager);
        setDefaultDistance(30);
        setUpVector(Vector3f.UNIT_Y);
        setDragToRotate(false);
    }
    
}
