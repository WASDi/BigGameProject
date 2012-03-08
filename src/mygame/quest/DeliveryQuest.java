package mygame.quest;

import mygame.npc.Npc;

/**
 *
 * @author wasd
 */
public class DeliveryQuest extends Quest{
    
    private Npc from;
    private Npc to;
    private String itemName;

    public DeliveryQuest(Npc from, Npc to, String itemName, Quest prequest, Quest followup) {
        super(prequest, followup);
        this.from = from;
        this.to = to;
        this.itemName = itemName;
        
        from.addQuest(this);
        to.addQuest(this);
    }

    public String onTalk(Npc npc) {
        if(stage==1){
            if(npc==from){
                stage=2;
                return String.format("Please deliver this %s to %s.", itemName, to.getName());
            }
            else if(npc==to){
                return String.format("Could you please visit %s and get the %s to me?", from.getName(), itemName);
            }
        }
        else if(stage==2){
            if(npc==from){
                return String.format("Have you delivered my %s to %s yet?", itemName, to.getName());
            }
            if(npc==to){
                stage=100;
                onFinish();
                return String.format("Thank you! I will have great use for this %s.", itemName);
            }
        }
        return null;
    }
    
}
