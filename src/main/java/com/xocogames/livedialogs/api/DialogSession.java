package com.xocogames.livedialogs.api;

import com.xocogames.livedialogs.api.model.DialogConversation;
import com.xocogames.livedialogs.api.model.DialogsContainer;
import com.xocogames.livedialogs.api.model.execution.DialogExecutionAction;
import com.xocogames.livedialogs.api.model.execution.DialogMemory;
import com.xocogames.livedialogs.api.model.objects.DialogNode;
import com.xocogames.livedialogs.api.model.objects.actions.DialogAction;
import com.xocogames.livedialogs.api.model.objects.conditions.DialogCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogSession {

    private DialogsContainer dialogsContainer;

    private String refActorOrg;
    private String refActorReply;

    private DialogConversation dialogConversation;
    private DialogMemory dialogMemory;
    private DialogExecutionAction dialogExecutionAction;

    private DialogSessionCallback dialogSessionCallback;

    private long tsGameLast = 0L;
    private long tsGameNow = 0L;

    private boolean startFirst;
    private boolean startNext = false;


    private static final String REGEXP_VARNAME = "\\$\\{\\w+}";

    public String getRefActorOrg() {
        return refActorOrg;
    }

    public String getRefActorReply() {
        return refActorReply;
    }

    public long getTsGameLast() {
        return tsGameLast;
    }

    public long getTsGameNow() {
        return tsGameNow;
    }

    public void setTsGameNow(long tsGameNow) {
        this.tsGameLast = this.tsGameNow;
        this.tsGameNow = tsGameNow;
    }

    public boolean isStartFirst() {
        return startFirst;
    }
    public boolean isStartNext() {
        return startNext;
    }

    public DialogMemory getDialogMemory() {
        return dialogMemory;
    }


    public DialogSession(DialogsContainer dialogsContainer, String refActorOrg, String refActorReply) {
        this.dialogsContainer = dialogsContainer;
        this.refActorOrg = refActorOrg;
        this.refActorReply = refActorReply;

        startFirst = true;

        dialogConversation = dialogsContainer.getConversation(refActorOrg, refActorReply);
        dialogMemory = new DialogMemory(dialogsContainer, refActorOrg, refActorReply);
        dialogExecutionAction = new DialogExecutionAction(this);
        dialogSessionCallback = null;
    }

    public void setDialogSessionCallback(DialogSessionCallback dialogSessionCallback) {
        this.dialogSessionCallback = dialogSessionCallback;
    }

    public DialogNode startDialog(long tsGame) {
        setTsGameNow(tsGame);
        startNext = true;

        DialogNode dialogNodeSelected = evalDialogConditions();
        executeDialogActions(dialogNodeSelected);
        return dialogNodeSelected;
    }

    public DialogNode replyDialog(long tsGame, String idDialogReply) {
        setTsGameNow(tsGame);
        startFirst = false;
        startNext = false;

        evalDialogReply(idDialogReply);

        DialogNode dialogNodeSelected = evalDialogConditions();
        executeDialogActions(dialogNodeSelected);
        return dialogNodeSelected;
    }

    public String resolveTextVars(String text) {
        List<String> listVarNames = resolverVarsExpressions(text);
        for (String varName: listVarNames) {
            text = text.replace("${"+varName+"}", resolverVar(varName));
        }
        return text;
    }

    private DialogNode evalDialogConditions() {
        for (DialogNode dialogNode: dialogConversation.getActorOrgDialogs()) {
            boolean okConditions = false;
            DialogCondition conditions = dialogNode.getDialogConditions();
            if (conditions != null) {
                okConditions = conditions.eval(this);
            }
            if (okConditions) return dialogNode;
        }
        return null;
    }

    private void evalDialogReply(String idDialogReply) {
        DialogNode dialogNodeReply = dialogConversation.getActorDialogNode(refActorReply, idDialogReply);
        if (dialogNodeReply != null) {
            executeDialogActions(dialogNodeReply);
            dialogMemory.addExecutionDialogReply(getTsGameNow(), dialogNodeReply);
        }
    }

    private void executeDialogActions(DialogNode dialogNode) {
        if (dialogNode != null) {
            for (DialogAction dialogAction: dialogNode.getListActions()) {
                boolean executed = false;
                if (dialogSessionCallback != null) executed = dialogSessionCallback.executeAction(dialogAction);
                if (!executed) dialogExecutionAction.executeAction(dialogAction);
            }
        }
    }

    private String resolverVar(String varName) {
        if (varName.equalsIgnoreCase("ACTOR")) return getRefActorOrg();
        if (varName.equalsIgnoreCase("PLAYER") || varName.equalsIgnoreCase("PLAYERNAME")) return getRefActorReply();
        return dialogMemory.getVarValue(varName, "");
    }

    public static List<String> resolverVarsExpressions(String text) {
        List<String> listVarNames = new ArrayList<>();

        Pattern patternVarName = Pattern.compile(REGEXP_VARNAME); // "\\$\\{\\w+}"
        Matcher matcher = patternVarName.matcher(text);

        while (matcher.find()) {
            String varName = matcher.group(0);
            varName = varName.substring(2, varName.length() -1);
            listVarNames.add(varName);
        }
        return listVarNames;
    }
}
