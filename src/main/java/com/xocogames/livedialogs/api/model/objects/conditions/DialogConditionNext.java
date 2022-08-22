package com.xocogames.livedialogs.api.model.objects.conditions;

import com.xocogames.livedialogs.api.DialogSession;

public class DialogConditionNext implements DialogCondition {

    @Override
    public boolean eval(DialogSession session) {
        return session.isStartNext();
    }
}
