package com.xocogames.livedialogs.api.model.objects.conditions;

import com.xocogames.livedialogs.api.DialogSession;

import java.util.ArrayList;
import java.util.List;

public class DialogConditionAnd implements DialogConditionContainer {

    private final List<DialogCondition> listConditions;

    public DialogConditionAnd() {
        listConditions = new ArrayList<>();
    }

    public void addCondition(DialogCondition condition) {
        listConditions.add(condition);
    }

    @Override
    public boolean eval(DialogSession session) {
        if (listConditions.size() == 0) return false;

        for (DialogCondition condition: listConditions) {
            if (!condition.eval(session)) return false;
        }
        return true;
    }
}
