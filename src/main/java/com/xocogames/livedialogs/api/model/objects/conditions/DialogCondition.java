package com.xocogames.livedialogs.api.model.objects.conditions;

import com.xocogames.livedialogs.api.DialogSession;

public interface DialogCondition {

    boolean eval(DialogSession session);
}
