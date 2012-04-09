package mygame.quest;

import mygame.npc.Npc;

/**
 * A quest where you have to kill a certain amount of enemies.
 * Stages:
 *  2 = started
 *  50 = can complete at questgiver
 * 
 * @author wasd
 */
public class KillQuest extends Quest {

    private String enemyName;
    private Npc startNpc;
    private int kills = 0;
    private int killsNeeded;
    private String predefinedStartSay;
    private String predefinedEndSay;

    public KillQuest(Npc startNpc, String enemyName, int killsNeeded, Quest followup) {
        super(followup);
        this.startNpc = startNpc;
        this.enemyName = enemyName;
        this.killsNeeded = killsNeeded;
        
        startNpc.addQuest(this);
    }

    @Override
    public String onTalk(Npc npc) {
        if (npc == startNpc) {
            if (stage == 1) {
                stage = 2; //start quest
                if(predefinedStartSay != null)
                    return predefinedStartSay;
                else
                    return String.format("Please kill %s %ss for me.", killsNeeded, enemyName);
            } else if (stage == 2) {
                return String.format("You need to kill %s more %ss.", (killsNeeded - kills), enemyName);
            } else if (stage == 50) {
                onFinish();
                startNpc.questMarkerUpdate(false);
                if(predefinedEndSay != null)
                    return predefinedEndSay;
                else
                    return String.format("Thank you for killing all these %ss!", enemyName);
            }
        }
        return null;
    }

    @Override
    public void onStart() {
        if(stage!=0)
            throw new Error("Quest already started");
        stage=1;
        startNpc.questMarkerUpdate(true);
        //TODO put questmarker on enemies?
    }

    /**
     * Should be called when an enemyName has been killed
     */
    public void onKill() {
        kills++;
        if (kills >= killsNeeded && stage != 50) {
            //can finish quest
            stage = 50;
            startNpc.questMarkerUpdate(true);
        }
    }

    public void onNpcKill(String name) {
        if(stage != 2){
            return;
        }
        if(name.equals(enemyName)){
            kills++;
            if(kills == killsNeeded){
                //Quest complete, go talk to startNpc
                stage = 50;
            }
        }
    }

    public void setPredefinedEndSay(String predefinedEndSay) {
        this.predefinedEndSay = predefinedEndSay;
    }

    public void setPredefinedStartSay(String predefinedStartSay) {
        this.predefinedStartSay = predefinedStartSay;
    }
    
}
