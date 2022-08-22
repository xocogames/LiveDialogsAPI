package com.xocogames.livedialogs.api.model.objects.conditions;

import com.xocogames.livedialogs.api.DialogSession;
import com.xocogames.livedialogs.api.model.execution.DialogExecutionInstant;

public class DialogConditionTimeAgo implements DialogCondition {

    private long timeAgo;

    public DialogConditionTimeAgo() {
    }

    public void setTimeAgo(long timeAgo) {
        this.timeAgo = timeAgo;
    }

    @Override
    public boolean eval(DialogSession session) {

        long tsLast = session.getTsGameLast();
        long tsNow = session.getTsGameNow();
        return tsLast + timeAgo*1000 < tsNow;
    }
}
