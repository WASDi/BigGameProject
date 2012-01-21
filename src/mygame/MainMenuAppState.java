/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;

/**
 *
 * @author wasd
 */
public class MainMenuAppState extends AbstractAppState{
    
    private Game app;
    private Node stateNode = new Node("MainMenuAppState Root Node");

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
    }

    @Override
    public void update(float tpf) {
        
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
