package com.xocogames.livedialogs.api.model.objects;

public class DialogSentence {

    private String idSentence;
    private String textSentence;

    public String getIdSentence() {
        return idSentence;
    }

    public String getTextSentence() {
        return textSentence;
    }

    public DialogSentence(String idSentence, String textSentence) {
        this.idSentence = idSentence;
        this.textSentence = textSentence;
    }

}
