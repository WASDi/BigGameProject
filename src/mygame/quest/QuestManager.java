package mygame.quest;

import java.util.ArrayList;
import java.util.List;
import mygame.npc.Npc;

/**
 *
 * @author wasd
 */
public class QuestManager {
    
    private List<Quest> questList = new ArrayList<Quest>();

    public QuestManager() {
    }
    
    public DeliveryQuest createDeliveryQuest(Npc from, Npc to, String itemName, Quest followup){
        DeliveryQuest dq = new DeliveryQuest(from, to, itemName, followup);
        questList.add(dq);
        return dq;
    }
    
    public KillQuest createKillQuest(Npc startNpc, String enemyName, int killsNeeded, Quest followup){
        KillQuest kq = new KillQuest(startNpc, enemyName, killsNeeded, followup);
        questList.add(kq);
        return kq;
    }

    public void onNpcKill(Npc npc) {
        for(Quest q : questList){
            if(q instanceof KillQuest){
                KillQuest kq = (KillQuest) q;
                kq.onNpcKill(npc.getName());
            }
        }
    }
    
}
