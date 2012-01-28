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

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(Game) app;
        niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
                                                          app.getInputManager(),
                                                          app.getAudioRenderer(),
                                                          app.getGuiViewPort());
        Nifty nifty = niftyDisplay.getNifty();
        nifty.addXml("Interface/mainmenu.xml");
        nifty.addXml("Interface/cinematic.xml");
        nifty.gotoScreen("mainmenu");
        
        MainMenuGuiController mainmenu = (MainMenuGuiController) nifty.getScreen("mainmenu").getScreenController();
        mainmenu.setGui(this);
        
        setClickModeEnabled(true);
        app.getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    private void setClickModeEnabled(boolean enable){
        app.getFlyByCamera().setEnabled(!enable);
        app.getInputManager().setCursorVisible(enable);
    }
    
    public void newGame(){
        setClickModeEnabled(false);
        app.startGame();
        niftyDisplay.getNifty().gotoScreen("cinematic");
        //TODO show loading screen
    }
    
}
