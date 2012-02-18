package mygame;

import mygame.states.InGameAppState;
import mygame.states.GuiAppState;
import mygame.states.LoadingAppState;

/**
 * 
 * @author wasd
 */
public class Game extends CustomApplication {
    
    private ResourceLoader loader;
    private GuiAppState gui;

    /**
     * Does some basic initialization to get the application started
     */
    @Override
    public void simpleInitApp() {
        
        loader = new ResourceLoader(assetManager, cam, rootNode);
        rootNode.attachChild(loader.getSky());
        
        cam.setFrustumFar(9000);
        
        gui = new GuiAppState();
        stateManager.attach(gui);
        
    }
    
    /**
     * Called when starting a new game from the main menu
     */
    public void startGame(boolean newGame){
        
        InGameAppState gameState = new InGameAppState();
        stateManager.attach(new LoadingAppState(gameState, newGame));
        stateManager.attach(gameState);
    }

    public GuiAppState getGui() {
        return gui;
    }

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
