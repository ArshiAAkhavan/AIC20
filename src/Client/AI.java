package Client;

import Client.Model.*;
import com.google.gson.Gson;

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
    }

    public void turn(World world) {
        System.out.println("Client " + world.getMe().getPlayerId() + " started turn : " + world.getCurrentTurn());

        // update last state reward
        State thisState = new State(world, closestEnemy);
        if (lastAction != null && lastState != null) {
            lastAction.initialLastStateRewardInRandomPrecision(lastState, thisState, world.getMe(), closestEnemy);
        }

        // set random action
        Action randomAction = thisState.getActions().get(random.nextInt(thisState.getActions().size()));

        // target for units is king
        setUnitTargetsInRandomIteration(randomAction,world);

        // put in hashMap
        State lastStateTemp = lastState;
        new Thread(() -> sendDataForUpdateTransitionTable(lastStateTemp, thisState, world.getMe().getPlayerId())).start();

        // update args
        lastAction = randomAction;
        lastState = thisState;

    }

    public void end(World world, Map<Integer, Integer> scores) {
        System.out.println("Client " + world.getMe().getPlayerId() + " end started");
        System.out.println("Client " + world.getMe().getPlayerId() + " score: " + scores.get(world.getMe().getPlayerId()));
        finalizeClient(world);
    }

    // target for units is king
    public void setUnitTargetsInRandomIteration(Action randomAction,World world){
        int minPathID = calculateMinPathID(world.getFriend().getPathsFromPlayer());
        for (Integer unitID : randomAction.getFM()) {
            world.putUnit(unitID, world.getMe().getPathsFromPlayer().get(minPathID));
        }
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

    public void finalizeClient(World world) {
        //for turn 100
        State thisState = new State(world, closestEnemy);
        lastAction.initialLastStateRewardInRandomPrecision(lastState, thisState, world.getMe(), closestEnemy);
        sendDataForUpdateTransitionTable(lastState, thisState, world.getMe().getPlayerId());
        //shutdown client table handler
        try {
            IOHandler = new Socket("localhost", TABLE_HANDLER_PORT);
            sendMessageToSomewhere(IOHandler.getOutputStream(), TABLE_MESSAGE_VIEWER, "Client " + world.getMe().getPlayerId() + " connected");
            sendMessageToSomewhere(IOHandler.getOutputStream(), TABLE_CLIENT_DONE, "Client " + world.getMe().getPlayerId() + " terminated");
            IOHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataForUpdateTransitionTable(State updatedLastState, State thisState, int clientNumber) {
        try {
            IOHandler = new Socket("localhost", TABLE_HANDLER_PORT);
            sendMessageToSomewhere(IOHandler.getOutputStream(), TABLE_MESSAGE_VIEWER, "Client " + clientNumber + " connected");
            Gson gson = new Gson();
            sendMessageToSomewhere(IOHandler.getOutputStream(), TABLE_UPDATE_ORDER, "Client " + clientNumber + " start sending");
            if (updatedLastState != null) {
                sendDataToSomewhere(IOHandler.getOutputStream(), gson.toJson(updatedLastState, State.class));
            } else {
                sendDataToSomewhere(IOHandler.getOutputStream(), TABLE_SEND_NULL);
            }
            if (thisState != null) {
                sendDataToSomewhere(IOHandler.getOutputStream(), gson.toJson(thisState, State.class));
            } else {
                sendDataToSomewhere(IOHandler.getOutputStream(), TABLE_SEND_NULL);
            }
            sendMessageToSomewhere(IOHandler.getOutputStream(), TABLE_MESSAGE_VIEWER, "Client " + clientNumber + " ends sending");
            sendMessageToSomewhere(IOHandler.getOutputStream(), TABLE_CLIENT_CLOSE, "Client " + clientNumber + " disconnected");
            IOHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}