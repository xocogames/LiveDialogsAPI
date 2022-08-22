package com.xocogames.livedialogs.api.model.objects.conditions;

import com.xocogames.livedialogs.api.DialogSession;

public class DialogConditionEquals implements DialogCondition {

    private String leftOperand;

    private String rightOperand;

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
    }

    @Override
    public boolean eval(DialogSession session) {
        if (leftOperand == null && rightOperand == null) return true;
        if (leftOperand == null || rightOperand == null) return false;
        return leftOperand.equals(rightOperand);
    }
}
