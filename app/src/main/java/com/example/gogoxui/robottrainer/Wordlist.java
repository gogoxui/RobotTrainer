package com.example.gogoxui.robottrainer;

import org.litepal.crud.DataSupport;

/**
 * Created by gogoxui on 17-7-28.
 */

public class Wordlist extends DataSupport {
    private int id;
    private String name;
    private String triggerWord;
    private String reactionWord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTriggerWord() {
        return triggerWord;
    }

    public void setTriggerWord(String triggerWord) {
        this.triggerWord = triggerWord;
    }

    public String getReactionWord() {
        return reactionWord;
    }

    public void setReactionWord(String reactionWord) {
        this.reactionWord = reactionWord;
    }

}
