package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import mygame.Game;
import mygame.ResourceLoader;

/**
 *
 * @author wasd
 */
public class LoadingAppState extends AbstractAppState{
    
    private Game app;
    private ResourceLoader loader;
    private InGameAppState gameState;
    
    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    private Future loadFuture = null;

    public LoadingAppState(InGameAppState gameState) {
        this.gameState=gameState;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Game) app;
        this.loader = this.app.getResourceLoader();
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
            app.getGui().doneLoading();
            setEnabled(false);
            app.getStateManager().detach(this);
        }
    }
    
    private Callable<Void> loadingCallable = new Callable<Void>(){

        public Void call() throws Exception {

            setProgress(.1f, "Loading player");
            loader.getPlayerModel();
            
            setProgress(.2f, "Loading skybox");
            loader.getSpace();

            setProgress(.3f, "Loading terrain");
            loader.getTerrain();

            setProgress(.8f, "Loading water");
            loader.getWater();

            setProgress(.9f, "Almost done!");
            gameState.finishLoading();
            
            try {
                //Wait for gameState to load its things
                Thread.sleep(1337);
            } catch (InterruptedException e) {
            }
            
            //Start the IntroCinematic
            app.enqueue(new Callable<Void>() {
                public Void call() throws Exception {
                    app.getStateManager().attach(new IntroCinematicAppState(app, gameState));
                    return null;
                }
            });
            
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
