package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

/**
 * test
 * @author normenhansen
 */
public class Game extends SimpleApplication {
    
    private InGameAppState state;

    @Override
    public void simpleInitApp() {
        
        state = new InGameAppState();
        stateManager.attach(state);
        
        TerrainManager tl = new TerrainManager(this);
        rootNode.attachChild(tl.getTerrain());
        rootNode.addLight(tl.getSun());
        rootNode.attachChild(tl.getSky());
        viewPort.addProcessor(tl.getWater());
        
        flyCam.setMoveSpeed(25);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {}
}
