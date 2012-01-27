package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
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
    private PhysicsSpace physics;
    private PlayerControl player;

    public InGameAppState(TerrainManager tl) {
        this.tl = tl;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        
        initPhysics();
        initPlayer();
        
        stateNode.attachChild(geom);
        stateNode.attachChild(tl.getTerrain());
        initTerrainPhysics();
        stateNode.addLight(tl.getSun());
        
        show();
    }
    
    private void initPhysics(){
        BulletAppState bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
        physics = bulletAppState.getPhysicsSpace();
    }
    
    private void initPlayer(){
        player = new PlayerControl();
        
        //Example box as placeholder for player. TODO replace with actual player model
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geom = new Geometry("Box", b);
        Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        geom.addControl(player);
        physics.add(player);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 150, 0));
    }
    
    private void initTerrainPhysics(){
        RigidBodyControl terrainPhys = new RigidBodyControl(CollisionShapeFactory.createMeshShape(tl.getTerrain()), 0);
        tl.getTerrain().addControl(terrainPhys);
        physics.add(terrainPhys);
    }

    @Override
    public void update(float tpf) {
        //TODO all the things
//        geom.rotate(tpf, tpf, tpf);
        
    }
    
    //note to self: use setEnable to pause. Use show/hide to toggle if seen
    
    public void show(){
        app.getViewPort().addProcessor(tl.getWater());
        app.getRootNode().attachChild(stateNode);
    }
    
    public void hide(){
        app.getViewPort().removeProcessor(tl.getWater());
        app.getRootNode().detachChild(stateNode);
    }
    
}