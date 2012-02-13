package mygame.camera;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 *
 * @author wasd
 */
public class CombatCamera extends ChaseCamera{
    
    private boolean combatMode = true;

    public CombatCamera(Camera cam, Spatial target, InputManager inputManager) {
        super(cam, target, inputManager);
        setDefaultDistance(30);
        setUpVector(Vector3f.UNIT_Y);
        setDragToRotate(false);
        setMaxVerticalRotation(FastMath.PI/2.5f);
        setMinVerticalRotation(.1f);
        setMinDistance(10f);
    }
    
    /**
     * true = combat mode is enabled. Mouse invisible and clicking does attacks.
     * false = combat mode disabled. Mouse visible and can click on HUD.
     */
    public void setCombatMode(boolean enable){
        if(combatMode==enable)
            return;
        combatMode=enable;
        setDragToRotate(!enable);
    }
    
    public boolean isCombatMode(){
        return combatMode;
    }
    
}
