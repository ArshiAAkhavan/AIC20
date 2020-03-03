package Client;

import Client.Model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import static Client.Constants.*;

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
        System.out.println("random pick started");

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

        //getting transition table ready
//        try {
//            transitionTable = loadTransitionTableFromFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void turn(World world) {
        System.out.println("turn started: " + world.getCurrentTurn());

        // update last state reward
        State thisState = new State(world, closestEnemy);
        if (lastAction != null)
            lastAction.initialLastStateRewardInRandomPrecision(lastState, thisState, world.getMe(), closestEnemy);

        // set random action
        Action randomAction = thisState.getActions().get(random.nextInt(thisState.getActions().size()));

        // target for units is king
        int minPathID = calculateMinPathID(world.getFriend().getPathsFromPlayer());
        for (Integer unitID : randomAction.getFutureMovement()) {
            world.putUnit(unitID, world.getMe().getPathsFromPlayer().get(minPathID));
        }

        // update args
        lastAction = randomAction;
        lastState = thisState;

        // put in hashMap
        updateTransitionTable(thisState);
    }

    public void end(World world, Map<Integer, Integer> scores) {
        System.out.println("end started");
        System.out.println("My score: " + scores.get(world.getMe().getPlayerId()));
//        try {
//            saveTransitionTableInToFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
            System.out.printf("%d ", pathLength);
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

    public void updateTransitionTable(State newState) {
        State existedState = transitionTable.get(newState.getHashKey());
        if (existedState != null) {
            existedState.mergeActionsInRandomPrecision(newState);
        } else {
            transitionTable.put(newState.getHashKey(), newState);
        }
    }

    public HashMap<String, State> loadTransitionTableFromFile() throws IOException {
        IOHandler = new Socket("localhost", IO_HANDLER_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(IOHandler.getInputStream()));
        BufferedWriter order = new BufferedWriter(new OutputStreamWriter(IOHandler.getOutputStream()));
        Gson gson = new Gson();
        order.write(IO_READ_ORDER);
        order.flush();
        StringBuilder map = new StringBuilder();
        while (!map.toString().equals("finish")) {
            map.append(reader.readLine());
        }
        IOHandler.close();
        return gson.fromJson(map.toString(), new TypeToken<HashMap<String, State>>() {
        }.getType());
    }

    public void saveTransitionTableInToFile() throws IOException {
        IOHandler = new Socket("localhost", IO_HANDLER_PORT);
        BufferedWriter order = new BufferedWriter(new OutputStreamWriter(IOHandler.getOutputStream()));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        HashMap<String, State> oldTable = loadTransitionTableFromFile();
//        oldTable.putAll(transitionTable);
        order.write(Integer.parseInt(IO_WRITE_ORDER));
//        String[] map = gson.toJson(oldTable, new TypeToken<HashMap<String, State>>() {
//        }.getType()).split("\n");
        String[] map = gson.toJson(transitionTable, new TypeToken<HashMap<String, State>>() {}.getType()).split("\n");
        for (String s : map) {
            order.write(s);
            order.flush();
        }
        order.write("\n" + IO_EOF);
        order.flush();
        IOHandler.close();
    }
}