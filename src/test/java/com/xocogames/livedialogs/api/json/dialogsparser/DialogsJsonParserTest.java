package com.xocogames.livedialogs.api.json.dialogsparser;

import com.xocogames.livedialogs.api.DialogSession;
import com.xocogames.livedialogs.api.model.DialogConversation;
import com.xocogames.livedialogs.api.model.DialogsContainer;
import com.xocogames.livedialogs.api.model.objects.DialogNode;
import com.xocogames.livedialogs.api.model.objects.DialogSentence;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

class DialogsJsonParserTest {

    @Test
    public void testDialog() throws IOException {
        DialogsContainer dialogsContainer = testJsonParse();

        DialogSession dialogSession = new DialogSession(dialogsContainer, "Elvira", "Ramoncín");

        dialogSession.startDialog(System.currentTimeMillis());

        dialogSession.replyDialog(System.currentTimeMillis(), "3eb1b71d-0dbf-46f2-bb42-8debd234955e");
    }

    @Test
    public DialogsContainer testJsonParse() throws IOException {
        DialogsContainer dialogsContainer = new DialogsContainer();
        DialogsJsonParser dialogsJsonParser = new DialogsJsonParser(dialogsContainer);
        dialogsJsonParser.loadFileFromResource("livedialogs-example02.json");
        dialogsJsonParser.parse();

        DialogConversation dialogConversation = dialogsContainer.getConversation("Elvira", "Ramoncín");

        System.out.println("LIST NpgSentences");
        Map<String, DialogSentence> mapSentences = dialogConversation.getActorSentences("Elvira");
        for (DialogSentence dialogSentence: mapSentences.values()) {
            System.out.println("> Sentence: ");
            System.out.println("  - idSentence: "+ dialogSentence.getIdSentence());
            System.out.println("  - textSentence: "+ dialogSentence.getTextSentence());
        }


        System.out.println("LIST PlayerSentences");
        mapSentences = dialogConversation.getActorSentences("Ramoncín");
        for (DialogSentence dialogSentence: mapSentences.values()) {
            System.out.println("> Sentence: ");
            System.out.println("  - idSentence: "+ dialogSentence.getIdSentence());
            System.out.println("  - textSentence: "+ dialogSentence.getTextSentence());
        }

        System.out.println("LIST DialogNodeReplies from idDialog: "+ "37f9967f-ddb9-4c45-8bf5-104c50f61c9f");
        List<DialogNode> listDialogNodesReplies = dialogConversation.buildDialogReplies(dialogConversation.getActorDialogNode("Elvira", "37f9967f-ddb9-4c45-8bf5-104c50f61c9f"));
        for (DialogNode dialogNodeReply: listDialogNodesReplies) {
            System.out.println(" > Reply id: "+ dialogNodeReply.getIdDialogNode() + " / text = "+ dialogConversation.getActorDialogSentence(dialogNodeReply).getTextSentence());
        }

        return dialogsContainer;
    }
}