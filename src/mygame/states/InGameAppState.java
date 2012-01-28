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
import com.jme3.scene.shape.Box;
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
    private Geometry geom;
    private TerrainManager tl;
    private BulletAppState bulletAppState;
    private PlayerControl player;

    public InGameAppState(TerrainManager tl) {
        this.tl = tl;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
    }
    
    protected void initPhysics(){
        bulletAppState = new BulletAppState();
        bulletAppState.setEnabled(false);
        app.getStateManager().attach(bulletAppState);
    }
    
    protected void initPlayer(){
        player = new PlayerControl();
        
        //Example box as placeholder for player. TODO replace with actual player model
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geom = new Geometry("Box", b);
        Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        geom.addControl(player);
        bulletAppState.getPhysicsSpace().add(player);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 250, 0));
    }
    
    protected void initTerrainPhysics(){
        RigidBodyControl terrainPhys = new RigidBodyControl(CollisionShapeFactory.createMeshShape(tl.getTerrain()), 0);
        tl.getTerrain().addControl(terrainPhys);
        bulletAppState.getPhysicsSpace().add(terrainPhys);
    }
    
    public void show(){
        app.getViewPort().addProcessor(tl.getWater());
        app.getRootNode().attachChild(stateNode);
    }
    
    public void hide(){
        app.getViewPort().removeProcessor(tl.getWater());
        app.getRootNode().detachChild(stateNode);
    }

    protected void finishedLoading() {
        stateNode.attachChild(geom);
        stateNode.attachChild(tl.getTerrain());
        stateNode.addLight(tl.getSun());
        bulletAppState.setEnabled(true);
    }
    
}