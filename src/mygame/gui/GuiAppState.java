package mygame.gui;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import mygame.Game;

/**
 *
 * @author wasd
 */
public class GuiAppState extends AbstractAppState{
    
    private Game app;
    private NiftyJmeDisplay niftyDisplay;
    private LoadingScreenController loading;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
                                                          app.getInputManager(),
                                                          app.getAudioRenderer(),
                                                          app.getGuiViewPort());
        Nifty nifty = niftyDisplay.getNifty();
        loading = new LoadingScreenController();
        nifty.registerScreenController(new MainMenuGuiController(this));
        nifty.registerScreenController(loading);
        nifty.addXml("Interface/mainmenu.xml");
        nifty.addXml("Interface/cinematic.xml");
        nifty.addXml("Interface/loadingscreen.xml");
        nifty.gotoScreen("mainmenu");
        
        setClickModeEnabled(true);
        app.getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    private void setClickModeEnabled(boolean enable){
        app.getFlyByCamera().setEnabled(!enable);
        app.getInputManager().setCursorVisible(enable);
    }
    
    public void newGame(){
        app.getInputManager().setCursorVisible(false);
        app.startGame();
        niftyDisplay.getNifty().gotoScreen("loadlevel");
    }
    
    public void updateLoadingStatus(float progress, String loadingText){
        loading.updateLoadingStatus(progress, loadingText);
    }
    
    public void doneLoading(){
        niftyDisplay.getNifty().gotoScreen("cinematic");
        setClickModeEnabled(false);
    }
    
}
