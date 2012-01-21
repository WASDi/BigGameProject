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

    @Override
    public void simpleInitApp() {
        
        gui = new MainMenuGuiController(this);
        gui.show();
        
        flyCam.setMoveSpeed(25);
        inputManager.setCursorVisible(true);
    }
    
    public void startGame(){
        
        if(state==null){
            inputManager.setCursorVisible(false);
            state = new InGameAppState();
            stateManager.attach(state);

            TerrainManager tl = new TerrainManager(this);
            rootNode.attachChild(tl.getTerrain());
            rootNode.addLight(tl.getSun());
            rootNode.attachChild(tl.getSky());
            viewPort.addProcessor(tl.getWater());
        }
    }
    
    @Override
    public void simpleUpdate(float tpf) {}

    @Override
    public void simpleRender(RenderManager rm) {}
}
