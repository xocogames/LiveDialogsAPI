package com.xocogames.livedialogs.api.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DialogsContainer {

    private final Map<String, Map<String, DialogConversation>> mapConversations;

    public DialogsContainer() {
        mapConversations = new HashMap<>();
    }

    public DialogConversation createConversation(String refActorOrg, String refActorReply) {
        Map<String, DialogConversation> mapConversation = mapConversations.computeIfAbsent(refActorOrg, k -> new HashMap<>());
        DialogConversation dialogConversation = mapConversation.computeIfAbsent(refActorReply, k -> new DialogConversation(refActorOrg, refActorReply));
        return dialogConversation;
    }

    public DialogConversation getConversation(String refActorOrg, String refActorReply) {
        Map<String, DialogConversation> mapConversation = mapConversations.get(refActorOrg);
        if (mapConversation != null) return mapConversation.get(refActorReply);
        return null;
    }

    public DialogConversation getFirstConversation() {
        if (mapConversations.size() > 0) {
            Collection<Map<String, DialogConversation>> conversationValues = mapConversations.values();
            Map<String, DialogConversation> mapFirstConversations = conversationValues.iterator().next();
            if (mapFirstConversations.size() > 0) return mapFirstConversations.values().iterator().next();
        }
        return null;
    }
}
