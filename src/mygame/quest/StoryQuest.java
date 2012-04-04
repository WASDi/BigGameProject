package mygame.quest;

import mygame.npc.Npc;

/**
 * A quest where you just have to listen to an npc say one or more sentences.
 * 
 * @author wasd
 */
public class StoryQuest extends Quest{
    
    private Npc npc;
    private String[] sentences;

    public StoryQuest(Npc npc, Quest followup, String... sentences) {
        super(followup);
        this.npc = npc;
        this.sentences = sentences;
        
        npc.addQuest(this);
    }

    @Override
    public String onTalk(Npc npc) {
        if(npc != this.npc)
            return null;
        ++stage;
        if(stage > sentences.length){
            onFinish();
            npc.questMarkerUpdate(false);
            //return last position since onFinish modified stage
            return sentences[sentences.length-1];
        }
        return sentences[stage-2];
    }

    @Override
    public void onStart() {
        if(stage!=0)
            throw new Error("Quest already started");
        stage=1;
        npc.questMarkerUpdate(true);
    }
    
}
