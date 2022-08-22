package com.xocogames.livedialogs.api.model.objects.conditions;

import com.xocogames.livedialogs.api.DialogSession;

import java.util.ArrayList;
import java.util.List;

public class DialogConditionOr implements DialogConditionContainer {

    private final List<DialogCondition> listConditions;

    public DialogConditionOr() {
        listConditions = new ArrayList<>();
    }

    public void addCondition(DialogCondition condition) {
        listConditions.add(condition);
    }

    @Override
    public boolean eval(DialogSession session) {
        for (DialogCondition condition: listConditions) {
            if (condition.eval(session)) return true;
        }
        return false;
    }
}
