package com.xocogames.livedialogs.api.model.objects;

import com.xocogames.livedialogs.api.model.objects.actions.DialogAction;
import com.xocogames.livedialogs.api.model.objects.conditions.DialogCondition;

import java.util.ArrayList;
import java.util.List;

public class DialogNode {

    private String refActor;

    private String idDialogNode;

    private String idDialogSentence;

    private List<String> listReplyNodesId;

    private  String idDialogParent;

    private DialogCondition dialogConditions;

    private List<DialogAction> listActions;


    public String getRefActor() {
        return refActor;
    }

    public String getIdDialogNode() {
        return idDialogNode;
    }

    public String getIdDialogSentence() {
        return idDialogSentence;
    }

    public List<String> getListReplyNodesId() {
        return listReplyNodesId;
    }

    public String getIdDialogParent() {
        return idDialogParent;
    }

    public void setIdDialogParent(String idDialogParent) {
        this.idDialogParent = idDialogParent;
    }

    public void setIdDialogSentence(String idDialogSentence) {
        this.idDialogSentence = idDialogSentence;
    }

    public void setDialogConditions(DialogCondition dialogConditions) {
        this.dialogConditions = dialogConditions;
    }

    public DialogCondition getDialogConditions() {
        return dialogConditions;
    }

    public List<DialogAction> getListActions() { return listActions; }


    public DialogNode(String refActor, String idDialogNode) {
        this.refActor = refActor;
        this.idDialogNode = idDialogNode;
        listReplyNodesId = new ArrayList<>();
        listActions = new ArrayList<>();
    }

    public DialogNode(String refActor, String idDialogNode, String idDialogSentence) {
        this(refActor, idDialogNode);
        this.idDialogSentence = idDialogSentence;
    }

    public void addDialogReply(String idDialogNode) {
        listReplyNodesId.add(idDialogNode);
    }

    public void addDialogAction(DialogAction dialogAction) {
        listActions.add(dialogAction);
    }
}
