package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import mygame.Game;
import mygame.gui.LoadingScreenController;
import mygame.gui.MainMenuGuiController;

/**
 * Handles the GUI. ScreenControllers speak to this class which then contacts Game.
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
        nifty.addXml("Interface/ingame.xml");
        nifty.gotoScreen("mainmenu");
        
        setClickModeEnabled(true);
        app.getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    /**
     * Enables or disables mouse visibility and flyCam
     * Will become deprecated when Game extends Application and no longer uses flyCam
     * @param enable true to show mouse and disable movement
     */
    private void setClickModeEnabled(boolean enable){
        app.getInputManager().setCursorVisible(enable);
    }
    
    public void newGame(boolean newGame){
        app.getInputManager().setCursorVisible(false);
        app.startGame(newGame);
        niftyDisplay.getNifty().gotoScreen("loadlevel");
    }
    
    public void updateLoadingStatus(float progress, String loadingText){
        loading.updateLoadingStatus(progress, loadingText);
    }
    
    /**
     * Called by LoadingAppState when it has finished
     */
    public void showCinematicHud(){
        niftyDisplay.getNifty().gotoScreen("cinematic");
    }
    
    /**
     * Called by InGameAppState when the intro is done
     */
    public void showIngameHud(){
        niftyDisplay.getNifty().gotoScreen("ingame");
    }
    
}