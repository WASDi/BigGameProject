package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import mygame.Game;
import mygame.gui.InGameScreenController;
import mygame.gui.LoadingScreenController;
import mygame.gui.MainMenuScreenController;
import mygame.npc.Npc;

/**
 * Handles the GUI. ScreenControllers speak to this class which then contacts Game.
 *
 * @author wasd
 */
public class GuiAppState extends AbstractAppState implements ActionListener{
    
    private Game app;
    private NiftyJmeDisplay niftyDisplay;
    private LoadingScreenController loading;
    private InGameScreenController ingame;

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
        ingame = new InGameScreenController();
        
        nifty.registerScreenController(new MainMenuScreenController(this));
        nifty.registerScreenController(loading);
        nifty.registerScreenController(ingame);
        
        nifty.addXml("Interface/mainmenu.xml");
        nifty.addXml("Interface/cinematic.xml");
        nifty.addXml("Interface/loadingscreen.xml");
        nifty.addXml("Interface/ingame.xml");
        app.getGuiViewPort().addProcessor(niftyDisplay);
        
        nifty.gotoScreen("mainmenu");
        setClickModeEnabled(true);
        
        initKeys();
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
    
    private void initKeys(){
        InputManager inputManager = app.getInputManager();
        inputManager.addMapping("esc", new KeyTrigger(KeyInput.KEY_ESCAPE));
        
        inputManager.addListener(this, "esc");
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if(isPressed && name.equals("esc")){
            String screen = niftyDisplay.getNifty().getCurrentScreen().getScreenId();
            if(screen.equals("mainmenu")){
                //stop game on escape presss from mainmenu
                app.stop();
            }
            else if(screen.equals("options") || screen.equals("loadmenu")){
                //when pressing escape from any of these menus, go back to the main menu.
                niftyDisplay.getNifty().gotoScreen("mainmenu");
            }
            
        }
    }

    public void exit() {
        app.stop();
    }

    public void onTargetChange(Npc target) {
        ingame.onTargetChange(target);
    }
    
    public void onChat(String message){
        ingame.onChat(message);
    }
    
}
