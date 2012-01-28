package mygame;

import mygame.states.InGameAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import mygame.gui.GuiAppState;

/**
 * 
 * @author wasd
 */
public class Game extends SimpleApplication {
    
    private InGameAppState gameState;
    private TerrainManager tl;
    private GuiAppState gui;

    /**
     * Does some basic initialization to get the application started
     */
    @Override
    public void simpleInitApp() {
        
        tl = new TerrainManager(assetManager, cam, rootNode);
        rootNode.attachChild(tl.getSky());
        
        flyCam.setMoveSpeed(100);
        cam.setFrustumFar(9000);
        
        gui = new GuiAppState();
        stateManager.attach(gui);
        
    }
    
    /**
     * Called when starting a new game from the main menu
     */
    public void startGame(){
        
        gameState = new InGameAppState(tl);
        stateManager.attach(gameState);
        
    }

    public GuiAppState getGui() {
        return gui;
    }
    
    @Override
    public void simpleUpdate(float tpf) {}

    @Override
    public void simpleRender(RenderManager rm) {}
}
