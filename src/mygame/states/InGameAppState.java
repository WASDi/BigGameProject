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
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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
    
    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    private boolean needsLoading = true;
    private Future loadFuture = null;

    public InGameAppState(TerrainManager tl) {
        this.tl = tl;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
    }
    
    private void initPhysics(){
        bulletAppState = new BulletAppState();
        bulletAppState.setEnabled(false);
        app.getStateManager().attach(bulletAppState);
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
        bulletAppState.getPhysicsSpace().add(player);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 250, 0));
    }
    
    private void initTerrainPhysics(){
        RigidBodyControl terrainPhys = new RigidBodyControl(CollisionShapeFactory.createMeshShape(tl.getTerrain()), 0);
        tl.getTerrain().addControl(terrainPhys);
        bulletAppState.getPhysicsSpace().add(terrainPhys);
    }

    @Override
    public void update(float tpf) {
        if(needsLoading){
            if(loadFuture==null){
                loadFuture = exec.submit(loadingCallable);
            }
            if(loadFuture.isDone()){
                exec.shutdown();
                exec=null;
                show();
                app.getGui().doneLoading();
                needsLoading=false;
            }
            return;
        }
    }
    
    public void show(){
        app.getViewPort().addProcessor(tl.getWater());
        app.getRootNode().attachChild(stateNode);
    }
    
    public void hide(){
        app.getViewPort().removeProcessor(tl.getWater());
        app.getRootNode().detachChild(stateNode);
    }
    
    private Callable<Void> loadingCallable = new Callable<Void>(){

        public Void call() throws Exception {
            setProgress(.1f, "Loading physics");
            initPhysics();

            setProgress(.2f, "Loading player");
            initPlayer();

            setProgress(.3f, "Loading terrain");
            tl.getTerrain();

            setProgress(.7f, "Loading water");
            tl.getWater();

            setProgress(.8f, "Loading terrain physics");
            initTerrainPhysics();

            setProgress(.9f, "Almost done!");

            stateNode.attachChild(geom);
            stateNode.attachChild(tl.getTerrain());
            stateNode.addLight(tl.getSun());
            bulletAppState.setEnabled(true);
            return null;
        }
        
    };
    
    private void setProgress(final float progress, final String loadingText) {
        app.enqueue(new Callable() {
            public Object call() throws Exception {
                System.out.println(progress+": "+loadingText);
                app.getGui().updateLoadingStatus(progress, loadingText);
                return null;
            }
        });
    }
    
}