package com.xocogames.livedialogs.api.model;

import com.xocogames.livedialogs.api.model.objects.DialogNode;
import com.xocogames.livedialogs.api.model.objects.DialogSentence;

import java.util.*;

public class DialogConversation {

    private final String refActorOrg;

    private final String refActorReply;

    private final Map<String, Map<String, DialogSentence>> mapActorSentences;

    private final Map<String, Map<String, DialogNode>> mapActorDialogs;

    private final Map<String, DialogNode> mapActorOrgDialogs;

    private final Map<String, DialogNode> mapActorReplyDialogs;


    public String getRefActorOrg() {
        return refActorOrg;
    }

    public String getRefActorReply() {
        return refActorReply;
    }


    public DialogConversation(String refActorOrg, String refActorReply) {
        this.refActorOrg = refActorOrg;
        this.refActorReply = refActorReply;

        mapActorSentences = new HashMap<>();
        mapActorDialogs = new LinkedHashMap<>();

        mapActorOrgDialogs = new LinkedHashMap<>();
        mapActorReplyDialogs = new LinkedHashMap<>();
    }

    public DialogSentence createActorSentence(String refActor, String idSentence, String textSentence) {
        Map<String, DialogSentence> mapSentences = mapActorSentences.computeIfAbsent(refActor, k -> new HashMap<>());
        DialogSentence dialogSentence = mapSentences.computeIfAbsent(idSentence, k -> new DialogSentence(idSentence, textSentence));
        return dialogSentence;
    }

    public DialogNode createActorDialogNode(String refActor, String idDialog) {
        Map<String, DialogNode> mapDialogNodes = mapActorDialogs.computeIfAbsent(refActor, K -> new HashMap<>());
        DialogNode dialogNode = mapDialogNodes.computeIfAbsent(idDialog, k -> new DialogNode(refActor, idDialog));

        if (refActor.equals(refActorOrg)) mapActorOrgDialogs.put(dialogNode.getIdDialogNode(), dialogNode);
        else mapActorReplyDialogs.put(dialogNode.getIdDialogNode(), dialogNode);

        return dialogNode;
    }

    public DialogNode createActorDialogNode(String refActor, String idDialog, String idSentence) {
        DialogNode dialogNode = createActorDialogNode(refActor, idDialog);
        dialogNode.setIdDialogSentence(idSentence);
        return dialogNode;
    }

    public DialogNode createActorDialogReply(String refActorOrg, String refActorReply, String idDialogNode, String idDialogReply) {
        DialogNode dialogNodeParent = createActorDialogNode(refActorOrg, idDialogNode);
        DialogNode dialogNodeReply = createActorDialogNode(refActorReply, idDialogReply);

        dialogNodeParent.addDialogReply(dialogNodeReply.getIdDialogNode());
        dialogNodeReply.setIdDialogParent(dialogNodeParent.getIdDialogNode());
        return dialogNodeReply;
    }

    public Map<String, DialogSentence> getActorSentences(String refActor) {
        return mapActorSentences.get(refActor);
    }

    public DialogSentence getActorDialogSentence(String refActor, String idSentence) {
        Map<String, DialogSentence> mapSentences = getActorSentences(refActor);
        if (mapSentences != null) return mapSentences.get(idSentence);
        return null;
    }

    public DialogSentence getActorDialogSentence(DialogNode dialogNode) {
        return getActorDialogSentence(dialogNode.getRefActor(), dialogNode.getIdDialogSentence());
    }

    public DialogNode getActorDialogNode(String refActor, String idDialogNode) {
        Map<String, DialogNode> mapDialogNodes = mapActorDialogs.get(refActor);
        if (mapDialogNodes != null) return mapDialogNodes.get(idDialogNode);
        return null;
    }

    public List<DialogNode> buildDialogReplies(DialogNode dialogNode) {
        List<DialogNode> listDialogNodesReplies = new ArrayList<>();
        for (String idDialogNodeReply: dialogNode.getListReplyNodesId()) {
            DialogNode dialogNodeReply = getActorDialogNode(getReplyActor(dialogNode.getRefActor()), idDialogNodeReply);
            if (dialogNodeReply != null) listDialogNodesReplies.add(dialogNodeReply);
        }
        return listDialogNodesReplies;
    }

    public String getReplyActor(String refActor) {
        return refActor.equals(refActorOrg) ? refActorReply : refActorOrg;
    }

    public Collection<DialogNode> getActorOrgDialogs() {
        return mapActorOrgDialogs.values();
    }

    public Collection<DialogNode> getActorReplyDialogs() {
        return mapActorReplyDialogs.values();
    }
}
