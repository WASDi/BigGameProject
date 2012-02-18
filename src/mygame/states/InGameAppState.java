package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.concurrent.Callable;
import mygame.controls.PlayerControl;
import mygame.Game;
import mygame.ResourceLoader;

/**
 * Used when playing the game itself.
 * 
 * @author wasd
 */
public class InGameAppState extends AbstractAppState{
    
    private Game app;
    private Node stateNode;
    private Node playerNode = new Node("Player Node");
    private ResourceLoader loader;
    private BulletAppState bulletAppState;

    public InGameAppState() {
        stateNode = new Node("InGameAppState Root Node");
        stateNode.setCullHint(Spatial.CullHint.Always);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Game) app;
        this.loader = this.app.getResourceLoader();
    }
    
    private void initPhysics(){
        bulletAppState = new BulletAppState();
        bulletAppState.setEnabled(false);
        app.getStateManager().attach(bulletAppState);
        initTerrainPhysics();
    }
    
    private void initPlayerControl(){
        PlayerControl playerControl = new PlayerControl(app, playerNode);
        Spatial player = loader.getPlayerModel();
        player.setLocalRotation(new Quaternion().fromAngles(0, FastMath.PI/2, 0)); //fix the rotation
        player.setLocalTranslation(0, -1.95f, -1.6f); //centers the player model
        playerNode.attachChild(player);
        playerNode.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
        playerControl.setJumpSpeed(100); //TODO make it lower
        playerControl.setFallSpeed(30);
        playerControl.setGravity(30);
        playerControl.setPhysicsLocation(new Vector3f(320, -.5f, 240));
    }
    
    private void initTerrainPhysics(){
        RigidBodyControl terrainPhys = new RigidBodyControl(CollisionShapeFactory.createMeshShape(loader.getTerrain()), 0);
        loader.getTerrain().addControl(terrainPhys);
        bulletAppState.getPhysicsSpace().add(terrainPhys);
    }
    
    public void show(){
        app.getViewPort().addProcessor(loader.getWater());
        stateNode.setCullHint(Spatial.CullHint.Dynamic);
    }
    
    public void hide(){
        app.getViewPort().removeProcessor(loader.getWater());
        stateNode.setCullHint(Spatial.CullHint.Always);
    }

    /**
     * Called by LoadingAppState from another thread when it has (almost) finished loading.
     * Initiates IntroCinemaAppState.
     */
    protected void finishLoading() {
        app.enqueue(new Callable<Void>() {
            public Void call() throws Exception {
                stateNode.attachChild(loader.getTerrain());
                
                //FIXME this is a bad way to make the terrain brighter
                DirectionalLight sun2 = new DirectionalLight();
                sun2.setDirection(new Vector3f(4.9236743f, -1.27054665f, -5.896916f)); //(-x, y, -z) of ResourceLoader.lightDir
                sun2.setColor(ColorRGBA.White.clone().multLocal(.7f));
                stateNode.addLight(sun2);
                
                stateNode.addLight(loader.getSun());
                initPhysics();
                app.getRootNode().attachChild(stateNode);
                return null;
            }
        });
    }
    
    /**
     * Called by IntroCinemaAppState when it is finished.
     */
    protected void finishedIntroCinematic(){
        initPlayerControl();
        
        stateNode.attachChild(playerNode);
        bulletAppState.setEnabled(true);
        app.getGui().showIngameHud();
//        bulletAppState.getPhysicsSpace().enableDebug(app.getAssetManager());
    }
    
}
