package com.xocogames.livedialogs.api.json.dialogsparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xocogames.livedialogs.api.model.objects.conditions.*;

import java.util.Map;
import java.util.Set;

public class DialogsJsonConditionBuilder {

    public DialogsJsonConditionBuilder() {

    }

    public static void buildConditionContainer(JsonArray jsonArrConditions, DialogConditionContainer conditionContainer) {
        for (int r = 0; r < jsonArrConditions.size(); r++) {
            JsonObject jsonConditionInner = jsonArrConditions.get(r).getAsJsonObject();
            DialogCondition conditionInner = buildCondition(jsonConditionInner);
            if (conditionInner != null) conditionContainer.addCondition(conditionInner);
        }
    }

    private static DialogCondition buildCondition(JsonObject jsonObject) {

        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

        DialogCondition condition;

        condition = buildConditionFirst(entrySet);
        if (condition == null) condition = buildConditionNext(entrySet);
        if (condition == null) condition = buildConditionIdDialogPrev(entrySet);
        if (condition == null) condition = buildConditionAnd(entrySet);
        if (condition == null) condition = buildConditionOr(entrySet);
        if (condition == null) condition = buildConditionTimeAgo(entrySet);

        return condition;
    }

    private static DialogCondition buildConditionFirst(Set<Map.Entry<String, JsonElement>> entrySet) {
        JsonElement jsonField;
        if ((jsonField = getJsonField(entrySet, ConditionNames.CONDITION_FISRT)) != null) {
            DialogConditionFirst dialogConditionFirst = new DialogConditionFirst();
            return dialogConditionFirst;
        }
        return null;
    }

    private static DialogCondition buildConditionNext(Set<Map.Entry<String, JsonElement>> entrySet) {
        JsonElement jsonField;
        if ((jsonField = getJsonField(entrySet, ConditionNames.CONDITION_NEXT)) != null) {
            DialogConditionNext dialogConditionNext = new DialogConditionNext();
            return dialogConditionNext;
        }
        return null;
    }

    private static DialogCondition buildConditionTimeAgo(Set<Map.Entry<String, JsonElement>> entrySet) {
        JsonElement jsonField;
        if ((jsonField = getJsonField(entrySet, ConditionNames.CONDITION_TIME_AGO)) != null) {
            String strTimeAgo = jsonField.getAsString();
            if (strTimeAgo != null) {
                try {
                    long timeAgo = Long.parseLong(strTimeAgo);

                    DialogConditionTimeAgo dialogConditionTimeAgo = new DialogConditionTimeAgo();
                    dialogConditionTimeAgo.setTimeAgo(timeAgo);
                    return dialogConditionTimeAgo;
                }
                catch (Exception ignore) { }
            }
        }
        return null;
    }

    private static DialogCondition buildConditionIdDialogPrev(Set<Map.Entry<String, JsonElement>> entrySet) {
        JsonElement jsonField;
        if ((jsonField = getJsonField(entrySet, ConditionNames.CONDITION_ID_DIALOG_PREV)) != null) {
            String idDialogPrev = jsonField.getAsString();
            if (idDialogPrev != null) {
                DialogConditionPrev dialogConditionPrev = new DialogConditionPrev();
                dialogConditionPrev.setIdDialogPrev(idDialogPrev);
                return dialogConditionPrev;
            }
        }
        return null;
    }

    private static DialogCondition buildConditionAnd(Set<Map.Entry<String, JsonElement>> entrySet) {
        JsonElement jsonField;
        if ((jsonField = getJsonField(entrySet, ConditionNames.CONDITION_AND)) != null) {
            JsonArray jsonArrConditionsInner = (JsonArray) jsonField;
            DialogConditionAnd dialogConditionAnd = new DialogConditionAnd();
            buildConditionContainer(jsonArrConditionsInner, dialogConditionAnd);
            return dialogConditionAnd;
        }
        return null;
    }

    private static DialogCondition buildConditionOr(Set<Map.Entry<String, JsonElement>> entrySet) {
        JsonElement jsonField;
        if ((jsonField = getJsonField(entrySet, ConditionNames.CONDITION_OR)) != null) {
            JsonArray jsonArrConditionsInner = (JsonArray) jsonField;
            DialogConditionOr dialogConditionOr = new DialogConditionOr();
            buildConditionContainer(jsonArrConditionsInner, dialogConditionOr);
            return dialogConditionOr;
        }
        return null;
    }

    private static JsonElement getJsonField(Set<Map.Entry<String, JsonElement>> entrySet, String name) {
        for(Map.Entry<String, JsonElement> entry : entrySet) {
            if (entry.getKey().equals(name)) return entry.getValue();
        }
        return null;
    }

}
