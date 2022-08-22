package com.xocogames.livedialogs.api.model.objects.conditions;

import com.xocogames.livedialogs.api.DialogSession;
import com.xocogames.livedialogs.api.model.execution.DialogMemory;
import com.xocogames.livedialogs.api.model.execution.DialogExecutionInstant;

public class DialogConditionPrev implements DialogCondition {

    private String idDialogPrev;

    public void setIdDialogPrev(String idDialogPrev) {
        this.idDialogPrev = idDialogPrev;
    }


    public DialogConditionPrev() {
    }

    @Override
    public boolean eval(DialogSession session) {
        DialogExecutionInstant executionInstant = session.getDialogMemory().getInstantReplyByIdx(-1);
        return executionInstant != null && executionInstant.getIdDialogNode().equals(idDialogPrev);
    }
}
