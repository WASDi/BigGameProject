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
    private String predefinedFromSay;
    private String predefinedSayTo;

    public DeliveryQuest(Npc from, Npc to, String itemName, Quest followup) {
        super(followup);
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
                from.questMarkerUpdate(false);
                to.questMarkerUpdate(true);
                if(predefinedFromSay!=null)
                    return predefinedFromSay;
                return String.format("Could you please deliver this %s to %s?.", itemName, to.getName());
            }
            else if(npc==to){
                // return String.format("Could you please visit %s and get the %s to me?", from.getName(), itemName);
                return null;
            }
        }
        else if(stage==2){
            if(npc==from){
                return String.format("Have you delivered the %s to %s yet?", itemName, to.getName());
            }
            else if(npc==to){
                onFinish();
                to.questMarkerUpdate(false);
                if(predefinedSayTo!=null)
                    return predefinedSayTo;
                return "Thank you!";
            }
        }
        return null;
    }

    @Override
    public void onStart() {
        if(stage!=0)
            throw new Error("Quest already started");
        stage=1;
        from.questMarkerUpdate(true);
    }

    public void setExtraSayFrom(String extraSayFrom) {
        this.predefinedFromSay = extraSayFrom;
    }

    public void setExtraSayTo(String extraSayTo) {
        this.predefinedSayTo = extraSayTo;
    }
    
}
