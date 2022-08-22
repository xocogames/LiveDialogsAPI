package com.xocogames.livedialogs.api;

import com.xocogames.livedialogs.api.model.objects.actions.DialogAction;

public interface DialogSessionCallback {

    boolean executeAction(DialogAction dialogAction);
}
