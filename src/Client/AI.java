package Client;

import Client.Model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import static Client.Constants.*;
import static Client.UsefulMethods.*;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Map;

/**
 * You must put your code in this class {@link AI}.
 * This class has {@link #pick}, to choose units before the start of the game;
 * {@link #turn}, to do orders while game is running;
 * {@link #end}, to process after the end of the game;
 */

public class AI {
    private Player closestEnemy;
    private State lastState;
    private Action lastAction;
    private Random random = new Random();
    private HashMap<String, State> transitionTable = new HashMap<>();
    private Socket IOHandler;

    public void pick(World world) {
        System.out.println("Client " + world.getMe().getPlayerId() + " random pick started");

        setClosestEnemy(world, world.getMe().getPlayerId());
        List<BaseUnit> myRandomHand = new ArrayList<>();

        // choosing random hand
        for (int i = 0; i < world.getGameConstants().getHandSize(); i++) {
            myRandomHand.add(world.getBaseUnitById(random.nextInt(world.getAllBaseUnits().size())));
        }

        // picking the chosen hand - rest of the hand will automatically be filled with random baseUnits
        world.getMe().setHand(myRandomHand);

        //making initial state
        lastState = new State(world, closestEnemy);

        //getting last transition table (for learn)
        //todo avoid clients from calling turn function
//        getTransitionTable(world.getMe().getPlayerId());
    }

    public void turn(World world) {
        System.out.println("Client " + world.getMe().getPlayerId() + " started turn : " + world.getCurrentTurn());

        // update last state reward
        State thisState = new State(world, closestEnemy);
        if (lastAction != null)
            lastAction.initialLastStateRewardInRandomPrecision(lastState, thisState, world.getMe(), closestEnemy);

        // set random action
        Action randomAction = thisState.getActions().get(random.nextInt(thisState.getActions().size()));

        // target for units is king
        int minPathID = calculateMinPathID(world.getFriend().getPathsFromPlayer());
        for (Integer unitID : randomAction.getFM()) {
            world.putUnit(unitID, world.getMe().getPathsFromPlayer().get(minPathID));
        }

        // update args
        lastAction = randomAction;
        lastState = thisState;

        // put in hashMap
        updateTransitionTable(transitionTable, thisState);
    }

    public void end(World world, Map<Integer, Integer> scores) {
        System.out.println("Client " + world.getMe().getPlayerId() + " end started");
        System.out.println("Client " + world.getMe().getPlayerId() + " score: " + scores.get(world.getMe().getPlayerId()));
        saveEveryTing(world.getMe().getPlayerId());
    }

    public void setClosestEnemy(World world, int myId) {
        switch (myId) {
            case 0:
                closestEnemy = world.getPlayerById(3);
                break;
            case 1:
                closestEnemy = world.getPlayerById(2);
                break;
            case 2:
                closestEnemy = world.getPlayerById(1);
                break;
            case 3:
                closestEnemy = world.getPlayerById(0);
                break;
        }
    }

    public int calculateMinPathID(List<Path> paths) {
        int minLength = Integer.MAX_VALUE;
        int minPathID = 0;
        for (int j = 0; j < paths.size(); j++) {
            int pathLength = 0;
            for (int i = 0; i < paths.get(j).getCells().size(); i++) {
                pathLength++;
            }
            if (minLength > pathLength) {
                minLength = pathLength;
                minPathID = j;
            }
        }
        return minPathID;
    }

    public static int calculateShortestPath(World world, Player player, Unit unit) {
        int length = 0;
        for (Cell cell : world.getShortestPathToCell(player, unit.getCell()).getCells()) {
            length++;
        }
        return length;
    }

    public static void updateTransitionTable(HashMap<String, State> transitionTable, State newState) {
        State existedState = transitionTable.get(newState.getHashKey());
        if (existedState != null) {
            existedState.mergeActionsInRandomPrecision(newState);
        } else {
            transitionTable.put(newState.getHashKey(), newState);
        }
    }

    public void getTransitionTable(int clientNumber) {
        try {
            IOHandler = new Socket("localhost", IO_HANDLER_PORT);
            sendMessageToSomewhere(IOHandler.getOutputStream(), IO_MESSAGE_VIEWER, "Client " + clientNumber + " connected");
            HashMap<String, State> oldTable = loadTransitionTableFromFile(clientNumber);
            sendMessageToSomewhere(IOHandler.getOutputStream(), IO_CLIENT_CLOSE, "Client " + clientNumber + " disconnected");
            IOHandler.close();

            // merge because of time limit
            for (Map.Entry<String, State> entry : transitionTable.entrySet()) {
                updateTransitionTable(oldTable, entry.getValue());
            }
            transitionTable = oldTable;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveEveryTing(int clientNumber) {
        try {
            IOHandler = new Socket("localhost", IO_HANDLER_PORT);
            sendMessageToSomewhere(IOHandler.getOutputStream(), IO_MESSAGE_VIEWER, "Client " + clientNumber + " connected");
            saveTransitionTableInToFile(clientNumber);
            sendMessageToSomewhere(IOHandler.getOutputStream(), IO_CLIENT_DONE, "Client " + clientNumber + " terminated");
            IOHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, State> loadTransitionTableFromFile(int clientNumber) throws IOException {
        OutputStream query = IOHandler.getOutputStream();
        Scanner inputStream = new Scanner(IOHandler.getInputStream());
        Gson gson = new Gson();
        sendMessageToSomewhere(query, IO_READ_ORDER, "Client " + clientNumber + " start reading");
        StringBuilder map = new StringBuilder("");
        String checker = inputStream.nextLine();
        while (!checker.equals(IO_EOF)) {
            map.append(checker);
            checker = inputStream.nextLine();
        }
        sendMessageToSomewhere(query, IO_MESSAGE_VIEWER, "Client " + clientNumber + " reading finished");
        return gson.fromJson(map.toString(), new TypeToken<HashMap<String, State>>() {
        }.getType());
    }

    public void saveTransitionTableInToFile(int clientNumber) throws IOException {
        OutputStream query = IOHandler.getOutputStream();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HashMap<String, State> oldTable = loadTransitionTableFromFile(clientNumber);
        for (Map.Entry<String, State> entry : transitionTable.entrySet()) {
            updateTransitionTable(oldTable, entry.getValue());
        }
        sendMessageToSomewhere(query, IO_WRITE_ORDER, "Client " + clientNumber + " start writing");
        String[] maps = gson.toJson(oldTable, new TypeToken<HashMap<String, State>>() {
        }.getType()).split("\n");
        for (String s : maps) {
            sendDataToSomewhere(query, s);
        }
        sendDataToSomewhere(query, IO_EOF);
        sendMessageToSomewhere(query, IO_MESSAGE_VIEWER, "Client " + clientNumber + " writing finished");
    }
}