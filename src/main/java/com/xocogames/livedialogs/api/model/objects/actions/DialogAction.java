package com.xocogames.livedialogs.api.model.objects.actions;

import java.util.HashMap;
import java.util.Map;

public class DialogAction {

    private final String refAction;

    private final Map<String, String> mapFields;

    public DialogAction(String refAction) {
        this.refAction = refAction;
        mapFields = new HashMap<>();
    }

    public String getRefAction() {
        return refAction;
    }

    public void addField(String fieldName, String fieldValue) {
        mapFields.put(fieldName, fieldValue);
    }

    public String getFieldValue(String fieldName) {
        return mapFields.get(fieldName);
    }

    public String getFieldValue(String fieldName, String defValue) {
        String fieldValue = mapFields.get(fieldName);
        if (fieldValue != null) return fieldValue;
        return defValue;
    }
}
