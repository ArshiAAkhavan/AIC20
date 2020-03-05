package Client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static Client.Constants.*;
import static Client.UsefulMethods.*;

public class TableHandler {
    private static HashMap<String, State> transitionTable = new HashMap<>();
    private static int numberOfFinishedClients = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(TABLE_HANDLER_PORT);
        setTransitionTable(ss);
        System.out.println("Table Size: " + transitionTable.size());

        while (numberOfFinishedClients < 4) {
            Socket handler = ss.accept();
            Scanner inputStream = new Scanner(handler.getInputStream());
            OutputStream outputStream = handler.getOutputStream();
            while (true) {
                try {
                    if (inputStream.hasNext()) {
                        String query = inputStream.nextLine();
                        System.out.println(query.substring(1));
                        if (query.getBytes()[0] == TABLE_MESSAGE_VIEWER) {
                            continue;
                        }
                        if (query.getBytes()[0] == TABLE_CLIENT_DONE) {
                            numberOfFinishedClients++;
                            handler.close();
                            break;
                        }
                        if (query.getBytes()[0] == TABLE_CLIENT_CLOSE) {
                            handler.close();
                            break;
                        }
                        if (query.getBytes()[0] == TABLE_UPDATE_ORDER) {
                            getElementsToUpdateTransitionTable(inputStream);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        System.out.println("IO Start Writing");
        writeTransitionTableInFile();
        System.out.println("IO Ends Writing");
        System.out.println("IO Ended");
        ss.close();

    }

    public static void setTransitionTable(ServerSocket serverSocket) throws IOException {
        Socket ILoadedTable = serverSocket.accept();
        System.out.println("IO Started");
        System.out.println("IO Start Reading");
        transitionTable = loadTransitionTableFromFile();
        System.out.println("IO Ends Reading");
        sendDataToSomewhere(ILoadedTable.getOutputStream(), TABLE_READY_MESSAGE);
        ILoadedTable.close();
    }

    public static HashMap<String, State> loadTransitionTableFromFile() throws FileNotFoundException {
        File file = new File(TRANSITION_TABLE_FILE_PATH);
        Scanner fileReader = new Scanner(new FileReader(file));
        StringBuilder jsonTable = new StringBuilder("");
        while (fileReader.hasNext()) {
            jsonTable.append(fileReader.nextLine());
        }
        fileReader.close();
        return new Gson().fromJson(jsonTable.toString(), new TypeToken<HashMap<String, State>>() {
        }.getType());
    }

    public static void writeTransitionTableInFile() throws IOException {
        File file = new File(TRANSITION_TABLE_FILE_PATH);
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String[] maps = gson.toJson(transitionTable, new TypeToken<HashMap<String, State>>() {
        }.getType()).split("\n");
        for (String s : maps) {
            fileWriter.write(s + "\n");
        }
        fileWriter.close();
    }

    public static void updateTransitionTable(State newState) {
        State existedState = transitionTable.get(newState.getHashKey());
        if (existedState != null) {
            existedState.mergeActionsInRandomPrecision(newState);
        } else {
            transitionTable.put(newState.getHashKey(), newState);
        }
    }

    public static void getElementsToUpdateTransitionTable(Scanner inputStream) throws IOException {
        Gson gson = new Gson();
        State lastState = null;
        State thisState = null;
        String gsonObject = inputStream.nextLine();
        if (!gsonObject.equals(TABLE_SEND_NULL)) {
            lastState = gson.fromJson(gsonObject, State.class);
        }
        gsonObject = inputStream.nextLine();
        if (!gsonObject.equals(TABLE_SEND_NULL)) {
            thisState = gson.fromJson(gsonObject, State.class);
        }
        if (lastState != null) {
            State relatedState = transitionTable.get(lastState.getHashKey());
            relatedState.setActions(lastState.getActions());
        }
        if (thisState == null) {
            throw new NullPointerException();
        }
        updateTransitionTable(thisState);
    }

    public static void showTable() {
        for (Map.Entry<String, State> entry : transitionTable.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

}
