package mygame;

import mygame.states.InGameAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import mygame.gui.GuiAppState;
import mygame.states.LoadingAppState;

/**
 * 
 * @author wasd
 */
public class Game extends SimpleApplication {
    
    private ResourceLoader loader;
    private GuiAppState gui;

    /**
     * Does some basic initialization to get the application started
     */
    @Override
    public void simpleInitApp() {
        
        loader = new ResourceLoader(assetManager, cam, rootNode);
        rootNode.attachChild(loader.getSky());
        
        flyCam.setMoveSpeed(100);
        cam.setFrustumFar(9000);
        
        gui = new GuiAppState();
        stateManager.attach(gui);
        
    }
    
    /**
     * Called when starting a new game from the main menu
     */
    public void startGame(){
        
        InGameAppState gameState = new InGameAppState();
        stateManager.attach(new LoadingAppState(gameState));
        stateManager.attach(gameState);
    }

    public GuiAppState getGui() {
        return gui;
    }
    
    @Override
    public void simpleUpdate(float tpf) {}

    @Override
    public void simpleRender(RenderManager rm) {}

    /**
     * Called when switching between skyBox and spaceBox
     * @param enable true if spaceBox. false if skyBox.
     */
    public void enableSpaceBox(boolean enable) {
        if(enable){
            rootNode.detachChild(loader.getSky());
            rootNode.attachChild(loader.getSpace());
        }
        else{
            rootNode.detachChild(loader.getSpace());
            rootNode.attachChild(loader.getSky());
        }
    }

    public ResourceLoader getResourceLoader() {
        return loader;
    }
}
