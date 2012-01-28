package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.concurrent.Callable;
import mygame.controls.PlayerControl;
import mygame.Game;
import mygame.TerrainManager;

/**
 * Used when playing the game itself
 * 
 * @author wasd
 */
public class InGameAppState extends AbstractAppState{
    
    private Game app;
    private Node stateNode = new Node("InGameAppState Root Node");
    private Spatial player;
    private TerrainManager tl;
    private BulletAppState bulletAppState;

    public InGameAppState(TerrainManager tl) {
        this.tl = tl;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
    }
    
    //TODO move all the loading and initiating somewhere else to make InGameAppState actually control in-game stuff
    protected void initPhysics(){
        bulletAppState = new BulletAppState();
        bulletAppState.setEnabled(false);
        app.getStateManager().attach(bulletAppState);
    }
    
    protected void initPlayer(){
        PlayerControl playerControl = new PlayerControl();
        
        //Example box as placeholder for player. TODO replace with actual player model
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        player = new Geometry("Box", b);
        Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(mat);
        
        player.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(30);
        playerControl.setEnabled(false);
    }
    
    protected void initTerrainPhysics(){
        RigidBodyControl terrainPhys = new RigidBodyControl(CollisionShapeFactory.createMeshShape(tl.getTerrain()), 0);
        tl.getTerrain().addControl(terrainPhys);
        bulletAppState.getPhysicsSpace().add(terrainPhys);
    }
    
    private void show(){
        app.getViewPort().addProcessor(tl.getWater());
        app.getRootNode().attachChild(stateNode);
    }
    
    private void hide(){
        app.getViewPort().removeProcessor(tl.getWater());
        app.getRootNode().detachChild(stateNode);
    }

    protected void finishedLoading() {
        stateNode.attachChild(player);
        stateNode.attachChild(tl.getTerrain());
        stateNode.addLight(tl.getSun());
        final InGameAppState gameState = this;
        app.enqueue(new Callable<Void>() {
            public Void call() throws Exception {
                app.getStateManager().attach(new IntroCinematicAppState(app, gameState));
                return null;
            }
        });
    }
    
    protected void finishedIntroCinema(){
        PlayerControl playerControl = player.getControl(PlayerControl.class);
        playerControl.setEnabled(true);
        playerControl.setPhysicsLocation(new Vector3f(0, 250, 0));
        
        app.enableSpaceBox(false);
        stateNode.attachChild(player);
        bulletAppState.setEnabled(true);
        show();
    }

    public Spatial getPlayer() {
        return player;
    }
    
}