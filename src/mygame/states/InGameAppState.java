package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
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
    private Node stateNode = new Node("InGameAppState Root Node");
    private Spatial player;
    private ResourceLoader loader;
    private BulletAppState bulletAppState;

    public InGameAppState() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Game) app;
        this.loader = this.app.getResourceLoader();
    }
    
    private void initPhysics(){
        bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
    }
    
    private void initPlayerControl(){
        PlayerControl playerControl = new PlayerControl();
        player = loader.getPlayerModel();
        player.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(30);
        playerControl.setPhysicsLocation(new Vector3f(0, 250, 0));
    }
    
    private void initTerrainPhysics(){
        RigidBodyControl terrainPhys = new RigidBodyControl(CollisionShapeFactory.createMeshShape(loader.getTerrain()), 0);
        loader.getTerrain().addControl(terrainPhys);
        bulletAppState.getPhysicsSpace().add(terrainPhys);
    }
    
    public void show(){
        if(!app.getViewPort().getProcessors().contains(loader.getWater()))
            app.getViewPort().addProcessor(loader.getWater());
        app.getRootNode().attachChild(stateNode);
    }
    
    public void hide(){
        app.getViewPort().removeProcessor(loader.getWater());
        app.getRootNode().detachChild(stateNode);
    }

    /**
     * Called by LoadingAppState from another thread when it has finished loading.
     * Initiates IntroCinemaAppState.
     */
    protected void finishedLoading() {
        stateNode.attachChild(loader.getTerrain());
        stateNode.addLight(loader.getSun());
        initPhysics();
        bulletAppState.setEnabled(false);
        final InGameAppState gameState = this;
        app.enqueue(new Callable<Void>() {
            public Void call() throws Exception {
                app.getStateManager().attach(new IntroCinematicAppState(app, gameState));
                return null;
            }
        });
    }
    
    /**
     * Called by IntroCinemaAppState when it is finished.
     */
    protected void finishedIntroCinema(){
        
        initPlayerControl();
        initTerrainPhysics();
        
        stateNode.attachChild(player);
        bulletAppState.setEnabled(true);
        app.getFlyByCamera().setEnabled(true);
    }
    
}