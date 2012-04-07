package mygame.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.npc.Npc;

/**
 *
 * @author wasd
 */
public class InGameScreenController implements ScreenController{
    
    private TextRenderer targetName;
    private TextRenderer targetHp;
    
    private TextRenderer chatArea;

    public void bind(Nifty nifty, Screen screen) {
        targetName = screen.findElementByName("target_name").getRenderer(TextRenderer.class);
        targetHp = screen.findElementByName("target_hp").getRenderer(TextRenderer.class);
        chatArea = screen.findElementByName("chat_area").getRenderer(TextRenderer.class);
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    public void onTargetChange(Npc target) {
        if(targetName == null){
            //not initialized
            return;
        }
        if (target == null) {
            targetName.setText("-");
            targetHp.setText("-");
        }
        else {
            targetName.setText(target.getName());
            targetHp.setText(String.format("%s/%s", target.getHp(), target.getMaxHp()));
        }
        
    }
    
    public void onChat(String npcName, String message){
        if(message!=null)
            chatArea.setText(npcName+"\n"+message);
        else    
            chatArea.setText(npcName); //should say "no target"
    }
    
}
