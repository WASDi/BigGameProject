package mygame.states;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.MotionTrack;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import mygame.Game;

/**
 * AppState that managers the intro when starting a new game
 * The player flies in his ship and gets hit by a rock and crashes on the island
 *
 * @author wasd
 */
public class IntroCinematicAppState extends AbstractAppState{
    
    private Game app;
    private Node stateNode = new Node("IntroCinematic Root Node");
    private InGameAppState gameState;
    
    private Spatial player;
    private Spatial ship;
    private Spatial rock;
    private Node shipNode;
    private boolean rotateShip=false;
    private CameraNode camNode;
    
    private ParticleEmitter fire;
    private Cinematic cinematic;

    public IntroCinematicAppState(Game app, InGameAppState gameState) {
        this.app = app;
        this.gameState = gameState;
        this.player = app.getResourceLoader().getPlayerModel();
        this.rock = app.getResourceLoader().getRockModel();
        this.ship = app.getResourceLoader().getShipModel();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        
        this.app.enableSpaceBox(true);
        
        rock.setLocalScale(2f);
        ship.setLocalTranslation(2, -2, 0);
        player.setLocalTranslation(0, 5, 0);
        player.setLocalRotation(new Quaternion().fromAngles(0, 0, FastMath.HALF_PI));
        
        shipNode = new Node("Ship and Player");
        shipNode.attachChild(ship);
        
        stateNode.attachChild(shipNode);
        stateNode.attachChild(rock);
        
        cinematic = new Cinematic(stateNode, 24f, LoopMode.DontLoop);
        CinematicEventListener cel = new CinematicEventListener() {

            public void onPlay(CinematicEvent cinematic) {}

            public void onPause(CinematicEvent cinematic) {}

            public void onStop(CinematicEvent cinematic) {
                cinematicEnded();
            }
        };
        cinematic.addListener(cel);
        
        cinematic.addCinematicEvent(0f, getShipTrack());
        cinematic.addCinematicEvent(0f, getRockTrack());
        cinematic.addCinematicEvent(0f, getCameraTrack());
        cinematic.addCinematicEvent(20f, getSecondCameraTrack());
        
        this.app.getRootNode().attachChild(stateNode);
        this.app.getStateManager().attach(cinematic);
        initLight();
        initFire();
        cinematic.play();
    }
    
    private void spaceSceneDone(){
        camNode.setLocalTranslation(290, 20, 380);
        camNode.lookAt(shipNode.getLocalTranslation(), Vector3f.UNIT_Y);
        shipNode.attachChild(player);
        app.enableSpaceBox(false);
        stateNode.detachChild(rock);
        rock=null;
        gameState.show();
    }
    
    /**
     * @return The MotionTrack of the ship with the player
     */
    private MotionTrack getShipTrack(){
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(530, 450, 320));
        path.addWayPoint(new Vector3f(340, 450, 320));
        path.addWayPoint(new Vector3f(320, 250, 240));
        path.addWayPoint(new Vector3f(320, -1, 240));
        path.setCurveTension(.1f);
        
        path.addListener(new MotionPathListener() {

            public void onWayPointReach(MotionTrack motionControl, int wayPointIndex) {
                if(wayPointIndex==1){
                    shipNode.attachChild(fire);
                    rotateShip=true;
                }
                if(wayPointIndex==2){
                    spaceSceneDone();
                }
            }
        });
        
        MotionTrack track = new MotionTrack(shipNode, path, 18f);
        track.setSpeed(1f);
        
        return track;
    }
    
    /**
     * @return The MotionTrack of the camera
     */
    private MotionTrack getCameraTrack(){
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(520, 450, 360));
        path.addWayPoint(new Vector3f(430, 455, 360));
        path.addWayPoint(new Vector3f(350, 450, 360));
        path.setCurveTension(1);
        
        camNode = new CameraNode("Motion Camera", app.getCamera());
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        stateNode.attachChild(camNode);
        
        MotionTrack track = new MotionTrack(camNode, path, 5f);
        return track;
    }
    
    private MotionTrack getSecondCameraTrack(){
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(290, 20, 380));
        path.addWayPoint(new Vector3f(346, 13.8f, 240));
        path.setCurveTension(1);
        
        MotionTrack track = new MotionTrack(camNode, path, 3f);
        return track;
    }
    
    /**
     * @return The path of the rock that crashes with the ship.
     */
    private MotionTrack getRockTrack(){
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(420, 530, 320));
        path.addWayPoint(new Vector3f(270, 380, 320));

        MotionTrack track = new MotionTrack(rock, path, 10);
        return track;
    }
    
    private void initLight(){
        // sunset light
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f,-0.7f,1).normalizeLocal());
        dl.setColor(new ColorRGBA(0.44f, 0.30f, 0.20f, 1.0f));
        stateNode.addLight(dl);

        // skylight
        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.6f,-1,-0.6f).normalizeLocal());
        dl.setColor(new ColorRGBA(0.10f, 0.22f, 0.44f, 1.0f));
        stateNode.addLight(dl);

        // white ambient light
        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(1, -0.5f,-0.1f).normalizeLocal());
        dl.setColor(new ColorRGBA(0.80f, 0.70f, 0.80f, 1.0f));
        stateNode.addLight(dl);
    }
    
    private void initFire(){
        fire = new ParticleEmitter("Fire emitter", ParticleMesh.Type.Triangle, 50);
        Material mat_red = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", app.getAssetManager().loadTexture("Textures/flame.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(2); 
        fire.setImagesY(2);
        fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(3.5f);
        fire.setEndSize(0.5f);
        fire.setLowLife(1f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        fire.setShape(new EmitterSphereShape(Vector3f.ZERO, 2.5f));
    }
    
    @Override
    public void update(float tpf) {
        if(camNode!=null)
            camNode.lookAt(shipNode.getLocalTranslation(), Vector3f.UNIT_Y);
        if(rock!=null)
            rock.rotate(tpf, tpf, tpf);
        if(rotateShip)
            shipNode.rotate(tpf, tpf*2, tpf);
    }
    
    /**
     * Called when the cinematic has finished
     */
    private void cinematicEnded() {
        stateNode.detachAllChildren();
        app.getStateManager().detach(this);
        gameState.finishedIntroCinematic();
    }

}
