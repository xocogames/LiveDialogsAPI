package com.xocogames.livedialogs.api.model.execution;

import com.xocogames.livedialogs.api.model.DialogConversation;
import com.xocogames.livedialogs.api.model.DialogsContainer;
import com.xocogames.livedialogs.api.model.objects.DialogNode;

import java.util.*;

public class DialogMemory {

    private final DialogsContainer dialogsContainer;

    private final DialogConversation dialogConversation;

    private final LinkedHashMap<String, DialogExecutionInstant> lmapInstancesOrg;

    private final LinkedHashMap<String, DialogExecutionInstant> lmapInstancesReply;

    private Map<String, String> mapVarValues;

    public DialogMemory(DialogsContainer dialogsContainer, String refActorOrg, String refActorReply) {
        this.dialogsContainer = dialogsContainer;
        this.dialogConversation = dialogsContainer.getConversation(refActorOrg, refActorReply);

        lmapInstancesOrg = new LinkedHashMap<>();
        lmapInstancesReply = new LinkedHashMap<>();
        mapVarValues = new HashMap<>();
    }

    public void addExecutionDialogOrg(long tsGame, DialogNode dialogNode) {
        DialogExecutionInstant executionInstant = new DialogExecutionInstant(tsGame, dialogNode);
        lmapInstancesOrg.put(executionInstant.getIdDialogNode(), executionInstant);
    }

    public void addExecutionDialogReply(long tsGame, DialogNode dialogNode) {
        DialogExecutionInstant executionInstant = new DialogExecutionInstant(tsGame, dialogNode);
        lmapInstancesReply.put(executionInstant.getIdDialogNode(), executionInstant);
    }

    public DialogExecutionInstant getInstantOrgByIdx(int idx) {
        return getInstantByIdx(lmapInstancesOrg, idx);
    }

    public DialogExecutionInstant getInstantReplyByIdx(int idx) {
        return getInstantByIdx(lmapInstancesReply, idx);
    }

    public String setVarValue(String varName, String varValue) {
        mapVarValues.put(varName, varValue);
        return varValue;
    }

    public String getVarValue(String varName) {
        return mapVarValues.get(varName);
    }

    public String getVarValue(String varName, String defValue) {
        String varValue = mapVarValues.get(varName);
        if (varValue != null) return varValue;
        return defValue;
    }

    public int getVarValueInt(String varName, int defValue) {
        try {
            String varValue = getVarValue(varName, String.valueOf(defValue));
            return Integer.parseInt(varValue);
        }
        catch (Exception ex) {
            return defValue;
        }
    }

    public String incVarValue(String varName, int varValue) {
        if (!containsVar(varName)) return setVarValue(varName, String.valueOf(varValue));
        else {
            int newValue = getVarValueInt(varName, 0) + varValue;
            return setVarValue(varName, String.valueOf(newValue));
        }
    }

    public String decVarValue(String varName, int varValue) {
        if (!containsVar(varName)) return setVarValue(varName, String.valueOf(-varValue));
        else {
            int newValue = getVarValueInt(varName, 0) - varValue;
            return setVarValue(varName, String.valueOf(newValue));
        }
    }

    public boolean containsVar(String varName) {
        return mapVarValues.containsKey(varName);
    }

    public Set<String> getListVars() {
        return mapVarValues.keySet();
    }

    private DialogExecutionInstant getInstantByIdx(LinkedHashMap<String, DialogExecutionInstant> lmapInstances, int idx) {
        if (idx >= 0 && idx < lmapInstances.size()) {
            return new ArrayList<>(lmapInstances.values()).get(idx);
        }
        if (idx < 0 && -idx <= lmapInstances.size()) {
            return new ArrayList<>(lmapInstances.values()).get(lmapInstances.size() + idx);
        }
        return null;
    }
}
