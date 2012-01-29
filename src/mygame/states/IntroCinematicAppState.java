package mygame.states;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.MotionTrack;
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
    private Spatial rock;
    private CameraNode camNode;

    public IntroCinematicAppState(Game app, InGameAppState gameState) {
        this.app = app;
        this.gameState = gameState;
        this.player = app.getResourceLoader().getPlayerModel();
        this.rock = app.getResourceLoader().getRockModel();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        
        this.app.enableSpaceBox(true);
        
        stateNode.attachChild(player);
        stateNode.attachChild(rock);
        
        Cinematic cinematic = new Cinematic(stateNode, 15f, LoopMode.DontLoop);
        CinematicEventListener cel = new CinematicEventListener() {

            public void onPlay(CinematicEvent cinematic) {}

            public void onPause(CinematicEvent cinematic) {}

            public void onStop(CinematicEvent cinematic) {
                cinematicEnded();
            }
        };
        cinematic.addListener(cel);
        
        cinematic.addCinematicEvent(0f, getShipTrack());
        cinematic.addCinematicEvent(0f, getCameraTrack());
        cinematic.addCinematicEvent(0f, getRockTrack());
        
        this.app.getRootNode().attachChild(stateNode);
        this.app.getStateManager().attach(cinematic);
        cinematic.play();
    }
    
    /**
     * @return The MotionTrack of the ship with the player
     */
    private MotionTrack getShipTrack(){
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(200, 0, -30));
        path.addWayPoint(new Vector3f(10, 0, -30));
        path.addWayPoint(new Vector3f(-10, -200, -100));
        path.setCurveTension(.1f);
        path.enableDebugShape(app.getAssetManager(), stateNode);
        
        //TODO use a node that contains player and the ship
        MotionTrack track = new MotionTrack(player, path);
        track.setSpeed(1f);
        
        return track;
    }
    
    /**
     * @return The MotionTrack of the camera
     */
    private MotionTrack getCameraTrack(){
        //TODO MotionTrack for the camera and make it always lookAt player
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(190, 0, 10));
        path.addWayPoint(new Vector3f(100, 5, 10));
        path.addWayPoint(new Vector3f(20, 0, 10));
        path.setCurveTension(1);
        
        camNode = new CameraNode("Motion Camera", app.getCamera());
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        stateNode.attachChild(camNode);
        
        MotionTrack track = new MotionTrack(camNode, path);
        track.setSpeed(1.5f);
        return track;
    }
    
    /**
     * @return The path of the rock that crashes with the ship.
     */
    private MotionTrack getRockTrack(){
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(110, 100, -30));
        path.addWayPoint(new Vector3f(-90, -100, -30));
        path.enableDebugShape(app.getAssetManager(), stateNode);

        MotionTrack track = new MotionTrack(rock, path);
        return track;
    }

    @Override
    public void update(float tpf) {
        camNode.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    /**
     * Called when the cinematic has finished
     */
    private void cinematicEnded() {
        stateNode.detachAllChildren();
        gameState.finishedIntroCinema();
        app.getStateManager().detach(this);
    }

}
