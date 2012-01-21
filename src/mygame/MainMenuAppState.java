package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import mygame.gui.MainMenuGuiController;

/**
 *
 * @author wasd
 */
public class MainMenuAppState extends AbstractAppState{
    
    private Game app;
    private MainMenuGuiController gui;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        
        gui = new MainMenuGuiController(this.app);
        showMenu();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(isEnabled()!=enabled){
            if(enabled)
                showMenu();
            else
                hideMenu();
        }
        super.setEnabled(enabled);
    }

    private void showMenu() {
        app.getFlyByCamera().setEnabled(false);
        app.getInputManager().setCursorVisible(true);
        gui.show();
    }
    
    private void hideMenu(){
        app.getFlyByCamera().setEnabled(true);
        app.getInputManager().setCursorVisible(false);
        gui.hide();
    }
    
}
