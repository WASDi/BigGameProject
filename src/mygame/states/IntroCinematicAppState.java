package mygame.states;

import com.jme3.animation.Animation;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.MotionTrack;
import com.jme3.cinematic.events.PositionTrack;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Game;

/**
 *
 * @author wasd
 */
public class IntroCinematicAppState extends AbstractAppState{
    
    private Game app;
    private Node stateNode = new Node("IntroCinematic Root Node");
    private InGameAppState gameState;
    private Spatial player;

    public IntroCinematicAppState(Game app, InGameAppState gameState) {
        this.app = app;
        this.gameState = gameState;
        this.player = gameState.getPlayer();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application appz) {
        super.initialize(stateManager, app);
        this.app=(Game) appz;
        
        app.enableSpaceBox(true);
        
        stateNode.attachChild(player);
        
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
        
        app.getRootNode().attachChild(stateNode);
        app.getStateManager().attach(cinematic);
//        app.getFlyByCamera().setEnabled(false);
        cinematic.play();
    }
    
    private MotionTrack getShipTrack(){
        MotionPath path = new MotionPath();
        path.addWayPoint(new Vector3f(100, 0, -30));
        path.addWayPoint(new Vector3f(10, 0, -30));
        path.addWayPoint(new Vector3f(-10, -200, -100));
        path.setCurveTension(.05f);
        path.enableDebugShape(app.getAssetManager(), stateNode);
        
        MotionTrack track = new MotionTrack(player, path);
        track.setSpeed(1f);
        
        return track;
    }

    @Override
    public void update(float tpf) {
//        app.getCamera().lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    private void cinematicEnded() {
        stateNode.detachAllChildren();
        gameState.finishedIntroCinema();
        app.getStateManager().detach(this);
    }

}
