package mygame.quest;

import mygame.npc.Npc;
import mygame.npc.StaticNpc;

/**
 * Each Npc involved in a quest has the instance of that quest as a variable.
 * When Player talks to Npc it will run onTalk if it has a quest.
 * @author wasd
 */
public abstract class Quest {
    
    /**
     * Decides which stage the quest is on
     * 0 means unstartable
     * 1 means startable
     * 100 means it's completed
     * Everything in between is up to the subclass.
     */
    protected int stage;
    
    protected Quest prequest;
    protected Quest followup;

    protected Quest(Quest prequest, Quest followup) {
        this.prequest = prequest;
        this.followup = followup;
        
        if(prequest==null)
            stage=1; //no prequest, make quest startable
    }
    
    /**
     * Called when by Npc when Player talks to it if it has a quest
     * @param npc The Npc who calls this method.
     * @return What the Npc should say. null means to not say anything different
     */
    public abstract String onTalk(Npc npc);
    
    /**
     * Check if the quest is active or not. An NPC can only handle one active quest at the time
     * so make sure as few as possible are active simultaneously. (TODO change that design?)
     * @return true if the Quest is active
     */
    public boolean isActive(){
        return stage!=0 && stage!=100;
    }
    
    /**
     * Called when a quest is finished the make the followup avalible.
     */
    protected void onFinish(){
        stage=100;
        if(followup!=null)
            followup.stage=1;
    }
    
}
