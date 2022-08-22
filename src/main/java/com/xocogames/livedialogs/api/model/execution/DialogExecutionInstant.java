package com.xocogames.livedialogs.api.model.execution;

import com.xocogames.livedialogs.api.model.objects.DialogNode;

public class DialogExecutionInstant {

    private long tsGame;

    private DialogNode dialogNode;

    public long getTsGame() {
        return tsGame;
    }

    public DialogNode getDialogNode() {
        return dialogNode;
    }

    public String getIdDialogNode() {
        return dialogNode.getIdDialogNode();
    }

    public DialogExecutionInstant(long tsGame, DialogNode dialogNode) {
        this.tsGame = tsGame;
        this.dialogNode = dialogNode;
    }
}
