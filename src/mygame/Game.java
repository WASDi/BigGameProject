package mygame;

import mygame.gui.MainMenuGuiController;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

/**
 * test
 * @author normenhansen
 */
public class Game extends SimpleApplication {
    
    private InGameAppState state;
    private MainMenuGuiController gui;
    private TerrainManager tl;

    @Override
    public void simpleInitApp() {
        
        tl = new TerrainManager(this);
        rootNode.attachChild(tl.getSky());
        
        gui = new MainMenuGuiController(this);
        gui.show();
        
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
    }
    
    public void startGame(){
        
        if(state==null){
            flyCam.setEnabled(true);
            flyCam.setMoveSpeed(25);
            inputManager.setCursorVisible(false);
            
            state = new InGameAppState();
            stateManager.attach(state);
            
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
