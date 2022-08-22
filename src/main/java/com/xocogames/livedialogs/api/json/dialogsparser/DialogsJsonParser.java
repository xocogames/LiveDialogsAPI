package com.xocogames.livedialogs.api.json.dialogsparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xocogames.livedialogs.api.model.DialogConversation;
import com.xocogames.livedialogs.api.model.DialogsContainer;
import com.xocogames.livedialogs.api.model.objects.DialogNode;
import com.xocogames.livedialogs.api.model.objects.actions.DialogAction;
import com.xocogames.livedialogs.api.model.objects.conditions.ConditionNames;
import com.xocogames.livedialogs.api.model.objects.conditions.DialogConditionAnd;
import com.xocogames.utils.files.FileUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class DialogsJsonParser {

    private final DialogsContainer dialogsContainer;

    private String jsonContent = null;

    public DialogsJsonParser(DialogsContainer dialogsContainer) {
        this.dialogsContainer = dialogsContainer;
    }

    public void loadFile(String fpname) throws IOException {
        jsonContent = FileUtils.readFile(fpname);
    }

    public void loadFileFromResource(String fname) throws IOException {
        jsonContent = FileUtils.readResourceFile(fname);
    }

    public void parse() {

        JsonObject jsonObject = new JsonParser().parse(jsonContent).getAsJsonObject();

        String refActorNpg = jsonObject.get("refActorNpg").getAsString();
        String refActorPlayer = jsonObject.get("refActorPlayer").getAsString();

        // System.out.println(" - refActorNpg: " + refActorNpg);
        // System.out.println(" - refActorPlayer: " + refActorPlayer);

        DialogConversation dialogConversation = dialogsContainer.createConversation(refActorNpg, refActorPlayer);
        parseSentences(jsonObject, "npgSentences", refActorNpg, dialogConversation);
        parseSentences(jsonObject, "playerSentences", refActorPlayer, dialogConversation);

        parseDialog(jsonObject, "npgDialog", refActorNpg, refActorPlayer, dialogConversation);
        parseDialog(jsonObject, "playerDialog", refActorPlayer, refActorNpg, dialogConversation);
    }

    private void parseSentences(JsonObject jsonObject, String refSentences, String refActor, DialogConversation dialogConversation) {
        JsonArray arr = jsonObject.getAsJsonArray(refSentences);
        for (int i = 0; i < arr.size(); i++) {
            String idSentence = arr.get(i).getAsJsonObject().get("id").getAsString();
            String textSentence = arr.get(i).getAsJsonObject().get("text").getAsString();

            dialogConversation.createActorSentence(refActor, idSentence, textSentence);
        }
    }

    private void parseDialog(JsonObject jsonObject, String refDialog, String refActorOrg, String refActorReply, DialogConversation dialogConversation) {
        JsonArray arrDialog = jsonObject.getAsJsonArray(refDialog);
        if (arrDialog != null) {
            for (int i = 0; i < arrDialog.size(); i++) {
                JsonObject jsonDialogNode = arrDialog.get(i).getAsJsonObject();
                String idDialogNode = jsonDialogNode.get("idDialogNode").getAsString();
                String idDialogSentence = jsonDialogNode.get("idDialogSentence").getAsString();

                DialogNode dialogNode = dialogConversation.createActorDialogNode(refActorOrg, idDialogNode, idDialogSentence);

                // Réplicas/hijos
                JsonArray arrReplyOptions = jsonDialogNode.getAsJsonArray("replyOptions");
                if (arrReplyOptions != null) {
                    for (int r = 0; r < arrReplyOptions.size(); r++) {
                        String idDialogNodeReply = arrReplyOptions.get(r).getAsJsonObject().get("idDialogNode").getAsString();
                        String idDialogSentenceReply = arrReplyOptions.get(r).getAsJsonObject().get("idDialogSentence").getAsString();
                        DialogNode dialogNodeReply = dialogConversation.createActorDialogReply(refActorOrg, refActorReply, idDialogNode, idDialogNodeReply);
                        dialogNodeReply.setIdDialogSentence(idDialogSentenceReply);
                    }
                }

                // Condiciones
                JsonElement jsonConditions = jsonDialogNode.get(ConditionNames.CONDITION_AND);
                if (jsonConditions != null) {
                    DialogConditionAnd dialogConditionAnd = new DialogConditionAnd();
                    DialogsJsonConditionBuilder.buildConditionContainer((JsonArray) jsonConditions, dialogConditionAnd);
                    dialogNode.setDialogConditions(dialogConditionAnd);
                }

                // Acciones
                JsonArray arrActions = jsonDialogNode.getAsJsonArray("actions");
                if (arrActions != null) {
                    for (int r = 0; r < arrActions.size(); r++) {
                        JsonObject jsonAction = arrActions.get(r).getAsJsonObject();

                        String refAction = jsonAction.get("refAction").getAsString();
                        DialogAction dialogAction = new DialogAction(refAction);

                        Set<Map.Entry<String, JsonElement>> entrySet = jsonAction.entrySet();
                        for(Map.Entry<String, JsonElement> entry : entrySet) {
                            String fieldName = entry.getKey();
                            if (fieldName != refAction) {
                                String fieldValue = entry.getValue().getAsString();
                                dialogAction.addField(fieldName, fieldValue);
                            }
                        }
                        dialogNode.addDialogAction(dialogAction);
                    }
                }
            }
        }
    }
}
