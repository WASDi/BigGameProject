package mygame.controls;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import mygame.Game;
import mygame.camera.CombatCamera;
import mygame.npc.Npc;
import mygame.npc.NpcManager;

/**
 *
 * @author wasd
 */
public class PlayerControl extends CharacterControl implements ActionListener{
    
    private Game app;
    private boolean w,a,s,d;
    private Vector3f walkDir = new Vector3f();
    private CombatCamera combatCam;
    private boolean keysEnabled = true;
    private Npc target;
    private NpcManager npcManager;
    private long lastNpcCheck = 0;
    private Spatial targetArrow;
    private boolean drowning = false;

    public PlayerControl(Game app, Node playerNode, NpcManager npcManager) {
        super(new CapsuleCollisionShape(1, 2), .1f);
        this.app=app;
        this.npcManager=npcManager;
        initKeys();
        combatCam = new CombatCamera(app.getCamera(), playerNode, app.getInputManager());
        
        //init targetArrow
        Box b = new Box(.2f, .4f, .2f);
        targetArrow = new Geometry("Target Arrow", b);
        Material mat = new Material(app.getAssetManager(),
          "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        targetArrow.setMaterial(mat);
    }
    
    private void initKeys(){
        InputManager inputManager = app.getInputManager();
        inputManager.addMapping("w", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("a", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("s", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("d", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("EE", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("tab", new KeyTrigger(KeyInput.KEY_TAB));
        inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("debug", new KeyTrigger(KeyInput.KEY_T));
        
        inputManager.addListener(this, "w", "a", "s", "d", "jump", "tab", "EE", "debug");
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.length()==1){
            //wasd movement
            char key = name.charAt(0);
            handleWasd(key, isPressed);
            return;
        }
        if(isPressed && name.equals("jump")){
            jump();
            return;
        }
        if(isPressed && name.equals("tab")){
            combatCam.setCombatMode(!combatCam.isCombatMode());
            return;
        }
        if(isPressed && name.equals("debug")){
            System.out.printf("addSandGuy(%.1ff, %.1ff, %.1ff, %.5ff, %.5ff);\n",
                    getPhysicsLocation().x, getPhysicsLocation().y, getPhysicsLocation().z,
                    getViewDirection().x, getViewDirection().z);
//            System.out.println(getViewDirection());
            return;
        }
        if(isPressed && name.equals("EE")){
            if(target!=null){
                app.getGui().onChat(target.getName(), target.talk());
            }
            else{
                app.getGui().onChat("*No target*", null);
            }
            return;
        }
    }

    private void handleWasd(char key, boolean pressed) {
        if(!keysEnabled)
            return;
        
        switch(key){
            case 'w':
                w=pressed;
                break;
            case 'a':
                a=pressed;
                break;
            case 's':
                s=pressed;
                break;
            case 'd':
                d=pressed;
                break;
        }
    }

    @Override
    public void update(float tpf) {
        
        if(drowning || getPhysicsLocation().y<-11){
            drowning=true;
            if(keysEnabled){
                setKeysEnabled(false);
            }
            setWalkDirection(Vector3f.ZERO);
            Spatial playerModel = app.getResourceLoader().getPlayerModel();
            playerModel.setLocalTranslation(playerModel.getLocalTranslation().add(0, -tpf*3, 0));
            if(playerModel.getLocalTranslation().y<-10){
                //respawn
                drowning=false;
                app.getResourceLoader().resetPlayerTranslations();
                setKeysEnabled(true);
                setPhysicsLocation(new Vector3f(320, -.5f, 240));
            }
            return;
        }
        
        if(combatCam.isCombatMode() && System.currentTimeMillis()>lastNpcCheck+250){
            //target the closest npc every 250 millis if in combatmode
            lastNpcCheck = System.currentTimeMillis();
            Npc newTarget = npcManager.getCloseNpc(getPhysicsLocation());
            if(newTarget!=target){
                target = newTarget;
                target.onTargeted(targetArrow);
                app.getGui().onTargetChange(target);
            }
        }
        
        Vector3f camDir = app.getCamera().getDirection().clone();
        Vector3f camLeft = app.getCamera().getLeft().clone();
        camDir.y = 0;
        camLeft.y = 0;
        camDir.normalizeLocal();
        camLeft.normalizeLocal();
        walkDirection.set(0, 0, 0);
        if(w)
            walkDir.addLocal(camDir);
        if(a)
            walkDir.addLocal(camLeft);
        if(s)
            walkDir.addLocal(camDir.negateLocal());
        if(d)
            walkDir.addLocal(camLeft.negateLocal());
        walkDir.multLocal(.5f); //TODO set to .2f for normal speed
        setWalkDirection(walkDir);
        if(walkDir.length()!=0)
            setViewDirection(walkDir);
        super.update(tpf);
    }
    
    private void setKeysEnabled(boolean enable){
        keysEnabled=enable;
        w=false;
        a=false;
        s=false;
        d=false;
    }
    
}
