package quest;

import mygame.npc.Npc;

/**
 * Each Npc involved in a quest has the instance of that quest as a variable.
 * When Player talks to Npc it will run onTalk if it has a quest.
 * @author wasd
 */
public interface Quest {
    
    /**
     * Called when by Npc when Player talks to it if it has a quest
     * @param npc The Npc who calls this method.
     */
    public void onTalk(Npc npc);
    
}
