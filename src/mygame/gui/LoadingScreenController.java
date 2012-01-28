/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *
 * @author wasd
 */
public class LoadingScreenController implements ScreenController{
    
    private Element progressBarElement;
    private TextRenderer text;

    public void bind(Nifty nifty, Screen screen) {
        progressBarElement = nifty.getScreen("loadlevel").findElementByName("progressbar");
        text = nifty.getScreen("loadlevel").findElementByName("loadingtext").getRenderer(TextRenderer.class);
    }

    @Override
    public void onStartScreen() {}
    @Override
    public void onEndScreen() {}
    
    public void updateLoadingStatus(float progress, String loadingText){
        final int MIN_WIDTH = 32;
        int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
        progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
        progressBarElement.getParent().layoutElements();

        text.setText(loadingText);
    }
    
}
