package mygame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wasd
 */
public class Main {
    
    public static void main(String[] args) {
        
        //only log errors
        Logger.getLogger("").setLevel(Level.SEVERE);
        
        //start everything
        Game app = new Game();
        app.start();
    }
    
}
