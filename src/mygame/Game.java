package mygame;

import mygame.gui.MainMenuGuiController;
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

    @Override
    public void simpleInitApp() {
        
        tl = new TerrainManager(this);
        rootNode.attachChild(tl.getSky());
        
        flyCam.setMoveSpeed(25);
        
        menuState = new MainMenuAppState();
        stateManager.attach(menuState);
    }
    
    public void startGame(){
        
        if(gameState==null){
            flyCam.setEnabled(true);
            
            inputManager.setCursorVisible(false);
            
            gameState = new InGameAppState();
            stateManager.attach(gameState);
            
            rootNode.attachChild(tl.getTerrain());
            rootNode.addLight(tl.getSun());
            viewPort.addProcessor(tl.getWater());
        }
    }
    
    @Override
    public void simpleUpdate(float tpf) {}

    @Override
    public void simpleRender(RenderManager rm) {}
}
