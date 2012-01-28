package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import mygame.Game;
import mygame.TerrainManager;

/**
 *
 * @author wasd
 */
public class LoadingAppState extends AbstractAppState{
    
    private Game app;
    private TerrainManager tl;
    private InGameAppState inGame;
    
    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    private Future loadFuture = null;

    public LoadingAppState(TerrainManager tl, InGameAppState inGame) {
        this.tl = tl;
        this.inGame=inGame;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
    }

    @Override
    public void update(float tpf) {
        if(!isEnabled())
            return;
        if(loadFuture==null){
            loadFuture = exec.submit(loadingCallable);
        }
        if(loadFuture.isDone()){
            exec.shutdown();
            exec=null;
            inGame.show();
            app.getGui().doneLoading();
            setEnabled(false);
            app.getStateManager().detach(this);
        }
    }
    
    private Callable<Void> loadingCallable = new Callable<Void>(){

        public Void call() throws Exception {
            setProgress(.1f, "Loading physics");
            inGame.initPhysics();

            setProgress(.2f, "Loading player");
            inGame.initPlayer();

            setProgress(.3f, "Loading terrain");
            tl.getTerrain();

            setProgress(.7f, "Loading water");
            tl.getWater();

            setProgress(.8f, "Loading terrain physics");
            inGame.initTerrainPhysics();

            setProgress(1f, "Almost done!");
            
            inGame.finishedLoading();
            return null;
        }
        
    };
    
    private void setProgress(final float progress, final String loadingText) {
        app.enqueue(new Callable() {
            public Object call() throws Exception {
                app.getGui().updateLoadingStatus(progress, loadingText);
                return null;
            }
        });
    }
    
}
