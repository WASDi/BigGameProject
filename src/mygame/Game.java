package mygame;

import mygame.states.MainMenuAppState;
import mygame.states.InGameAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

/**
 * 
 * @author wasd
 */
public class Game extends SimpleApplication {
    
    private InGameAppState gameState;
    private MainMenuAppState menuState;
    private TerrainManager tl;

    /**
     * Does some basic initialization to get the application started
     */
    @Override
    public void simpleInitApp() {
        
        tl = new TerrainManager(assetManager, cam, rootNode);
        rootNode.attachChild(tl.getSky());
        
        flyCam.setMoveSpeed(25);
        
        menuState = new MainMenuAppState();
        stateManager.attach(menuState);
        
        gameState = new InGameAppState(tl);
    }
    
    /**
     * Called when starting a new game from the main menu
     */
    public void startGame(){
        
        menuState.setEnabled(false);
        
        if(gameState.isInitialized())
            gameState.setEnabled(true);
        else
            stateManager.attach(gameState);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {}

    @Override
    public void simpleRender(RenderManager rm) {}
}
