package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;

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
        
        TerrainQuad terrain = TerrainLoader.getTerrain(assetManager);
        rootNode.attachChild(terrain);
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        terrain.addControl(control);
        
        flyCam.setMoveSpeed(25);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        
    }

    @Override
    public void simpleRender(RenderManager rm) {}
}
