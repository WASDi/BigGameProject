package mygame.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.npc.Npc;

/**
 *
 * @author wasd
 */
public class InGameScreenController implements ScreenController{
    
    private TextRenderer target_name;

    public void bind(Nifty nifty, Screen screen) {
        Element text = screen.findElementByName("target_name");
        target_name = text.getRenderer(TextRenderer.class);
        target_name.setText("1234567890");
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    public void onTargetChange(Npc target) {
        target_name.setText(target.getName());
    }
    
}
