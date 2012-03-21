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

    public KillQuest(Quest followup, Npc startNpc, String enemyName, int killsNeeded) {
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
                return String.format("Please kill %s %ss for me.", killsNeeded, enemyName);
            } else if (stage == 2) {
                return String.format("You need to kill %s more %ss.", (killsNeeded - kills), enemyName);
            } else if (stage == 50) {
                onFinish();
                startNpc.questMarkerUpdate(false);
                return String.format("Thank you for killing all these %ss!", enemyName);
            }
        }
        return null;
    }

    @Override
    public void onStart() {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
