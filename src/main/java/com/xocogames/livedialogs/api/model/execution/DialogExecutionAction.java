package com.xocogames.livedialogs.api.model.execution;

import com.xocogames.livedialogs.api.DialogSession;
import com.xocogames.livedialogs.api.model.objects.actions.DialogAction;

public class DialogExecutionAction {

    private final DialogMemory dialogMemory;

    public DialogExecutionAction(DialogSession dialogSession) {
        dialogMemory = dialogSession.getDialogMemory();
    }

    public boolean executeAction(DialogAction dialogAction) {
        try {
            if (dialogAction.getRefAction().equalsIgnoreCase("setVarValue"))
                return executeActionSetVarValue(dialogAction);
            if (dialogAction.getRefAction().equalsIgnoreCase("incVarValue"))
                return executeActionIncVarValue(dialogAction);
        }
        catch (Exception ignored) { }
        return false;
    }

    private boolean executeActionSetVarValue(DialogAction dialogAction) {
        String varName = dialogAction.getFieldValue("name");
        String varValue = dialogAction.getFieldValue("value");
        dialogMemory.setVarValue(varName, varValue);
        // System.out.println("DialogExecutionAction: setVarValue. varName = "+ varName + " / varValue = "+ varValue);
        return true;
    }

    private boolean executeActionIncVarValue(DialogAction dialogAction) {
        String varName = dialogAction.getFieldValue("name");
        String varValue = dialogAction.getFieldValue("value");
        dialogMemory.incVarValue(varName, Integer.parseInt(varValue));
        // System.out.println("DialogExecutionAction: setVarValue. varName = "+ varName + " / varValue = "+ varValue);
        return true;
    }
}
