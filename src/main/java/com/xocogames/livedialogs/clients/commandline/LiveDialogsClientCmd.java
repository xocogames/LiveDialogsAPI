package com.xocogames.livedialogs.clients.commandline;

import com.xocogames.livedialogs.api.DialogSession;
import com.xocogames.livedialogs.api.json.dialogsparser.DialogsJsonParser;
import com.xocogames.livedialogs.api.model.DialogConversation;
import com.xocogames.livedialogs.api.model.DialogsContainer;
import com.xocogames.livedialogs.api.model.objects.DialogNode;
import com.xocogames.livedialogs.api.model.objects.DialogSentence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveDialogsClientCmd {

    private final DialogsContainer dialogsContainer;
    private final DialogsJsonParser dialogsJsonParser;

    private DialogConversation dialogConversation;
    private DialogSession dialogSession;

    private Map<String, String> mapMemoryIds;

    private boolean close = false;
    private boolean exit = false;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println(">liveDialogs -file <fname>");
            System.out.println("Args.size = " + args.length);
            return;
        }

        String argFileName = getArgValue(args, "-file");
        if (argFileName != null) {
            clearConsole();

            LiveDialogsClientCmd liveDialogsClientCmd = new LiveDialogsClientCmd();
            liveDialogsClientCmd.loadResourceFile(argFileName);
            liveDialogsClientCmd.initConversation();
            liveDialogsClientCmd.continueConversation();
        }

    }

    private LiveDialogsClientCmd() throws IOException {

        mapMemoryIds = new HashMap<>();

        dialogsContainer = new DialogsContainer();
        dialogsJsonParser = new DialogsJsonParser(dialogsContainer);
    }

    private void loadResourceFile(String fname) throws IOException {
        dialogsJsonParser.loadFile(fname);
        dialogsJsonParser.parse();
    }

    private void initConversation() {
        dialogConversation = dialogsContainer.getFirstConversation();
        dialogSession = new DialogSession(dialogsContainer, dialogConversation.getRefActorOrg(), dialogConversation.getRefActorReply());
    }

    private void startConversation() {
        DialogNode dialogNodeSelected = dialogSession.startDialog(System.currentTimeMillis());
        if (dialogNodeSelected != null) {
            System.out.println("\n###########################################################################");
            System.out.println("Start dialog.\n");
            showDialog(dialogNodeSelected);
            // continueConversation();
        } else {
            System.out.println("End dialog.");
        }
    }

    private void continueConversation() {

        while (!exit) {
            close = false;
            startConversation();

            while (!exit && !close) {
                String inputLine = readConsoleLine();
                boolean command = resolveCommand(inputLine);

                if (!command && inputLine != null) {
                    String idReply = mapMemoryIds.get(inputLine);
                    if (idReply != null) {
                        DialogNode dialogNodeReply = dialogSession.replyDialog(System.currentTimeMillis(), idReply);
                        if (dialogNodeReply != null) {
                            showDialog(dialogNodeReply);
                        } else {
                            exit = true;
                            System.out.println("End dialog. No responses.");
                        }
                    } else {
                        exit = true;
                        System.out.println("End dialog. No replies.");
                    }
                }
            }
            if (close) {
                System.out.println("Closed dialog. Waitting for new star. Enter anything... \n");
                readConsoleLine();
            }
        }
    }

    private void showDialog(DialogNode dialogNode) {
        if (dialogNode != null) {
            DialogSentence dialogSentence = dialogConversation.getActorDialogSentence(dialogNode);
            System.out.println(">" + dialogSession.getRefActorOrg() + ": " + dialogSession.resolveTextVars(dialogSentence.getTextSentence()));

            List<DialogNode> listDialogNodesReplies = dialogConversation.buildDialogReplies(dialogNode);
            int idx = 1;
            mapMemoryIds.clear();
            System.out.println(">" + dialogSession.getRefActorReply() + ":");
            for (DialogNode dialogNodeReply : listDialogNodesReplies) {
                System.out.println("  - " + idx + ") " + dialogSession.resolveTextVars(dialogConversation.getActorDialogSentence(dialogNodeReply).getTextSentence()));

                mapMemoryIds.put(String.valueOf(idx), dialogNodeReply.getIdDialogNode());
                idx++;
            }
        }
    }

    private static String getArgValue(String[] args, String argName) {
        int i = 0;
        do {
            if (args[i].equalsIgnoreCase(argName) && i+1 < args.length) return args[i+1];
        } while (i++ < args.length);
        return null;
    }

    private static String readConsoleLine() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine().trim();
        } catch (Exception ex) {
            return null;
        }
    }

    private static void clearConsole() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    private boolean resolveCommand(String inputLine) {
        if (inputLine == null) return false;
        if (inputLine.equalsIgnoreCase("#close")) { close = true; return true; }
        if (inputLine.equalsIgnoreCase("#listVars")) return consoleCommandListVars();
        return false;
    }

    private boolean consoleCommandListVars() {
        System.out.println(">CMD: ListVars:");
        int idx = 0;
        for (String varName: dialogSession.getDialogMemory().getListVars()) {
            String varValue = dialogSession.getDialogMemory().getVarValue(varName);
            System.out.println(" - "+ varName +" = "+ varValue);
            idx++;
        }
        if (idx == 0) System.out.println(" - NONE.");
        return true;
    }
}
