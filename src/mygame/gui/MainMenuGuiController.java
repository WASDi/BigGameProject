package mygame.gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Game;

/**
 * Manages the main menu GUI
 * 
 * @author wasd
 */
public class MainMenuGuiController implements ScreenController{
    
    private GuiAppState gui;

    public MainMenuGuiController() {
    }

    public void setGui(GuiAppState gui) {
        this.gui = gui;
    }
    
//    public void show(){
//        app.getGuiViewPort().addProcessor(niftyDisplay);
//    }
//    
//    public void hide(){
//        app.getGuiViewPort().removeProcessor(niftyDisplay);
//    }

    public void bind(Nifty nifty, Screen screen) {
        //TODO save the variables?
    }

    public void onStartScreen() {
        //TODO check if there is a saved game. If not, disable continue and loadgame buttons
    }

    public void onEndScreen() {}
    
    
    //==Methods called by buttons==
    public void continueGame(){
        System.out.println("continue");
    }
    
    public void newGame(){
        System.out.println("newgame");
        gui.newGame();
//        niftyDisplay.getNifty().gotoScreen("cinematic"); //FIXME not woring when app.startGame(); is being called
        //TODO implement proper usage of GUI management
    }
    
    public void loadGame(){
        System.out.println("loadgame");
    }
    
    public void options(){
        System.out.println("options");
    }
    
    public void exitGame(){
        System.out.println("exit");
    }
    //==End of methods called by buttons==
    
}
