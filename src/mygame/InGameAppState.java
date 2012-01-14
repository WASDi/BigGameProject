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

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geom = new Geometry("Box", b);

        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        stateNode.attachChild(geom);
        
        
        this.app.getRootNode().attachChild(stateNode);
    }

    @Override
    public void update(float tpf) {
        //TODO all the things
        geom.rotate(tpf, tpf, tpf);
        
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(isEnabled()!=enabled){
            if(enabled)
                app.getRootNode().attachChild(stateNode);
            else
                app.getRootNode().detachChild(stateNode);
        }
        super.setEnabled(enabled);
    }
    
}