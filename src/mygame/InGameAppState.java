package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author wasd
 */
public class InGameAppState extends AbstractAppState{
    
    private Game app;
    private Node stateNode = new Node("InGameAppState Root Node");
    private Geometry geom;
    private TerrainManager tl;

    public InGameAppState(TerrainManager tl) {
        this.tl = tl;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geom = new Geometry("Box", b);

        Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        stateNode.attachChild(geom);
        
        stateNode.attachChild(tl.getTerrain());
        stateNode.addLight(tl.getSun());
        
        show();
    }

    @Override
    public void update(float tpf) {
        //TODO all the things
        geom.rotate(tpf, tpf, tpf);
        
    }
    
    //note to self: use setEnable to pause. Use show/hide to toggle if seen
    
    public void show(){
        app.getViewPort().addProcessor(tl.getWater());
        app.getRootNode().attachChild(stateNode);
    }
    
    public void hide(){
        app.getViewPort().removeProcessor(tl.getWater());
        app.getRootNode().detachChild(stateNode);
    }
    
}