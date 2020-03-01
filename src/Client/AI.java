package Client;

import Client.Model.*;

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
    private Object readLock = new Object();
    private Object writeLock = new Object();

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
        transitionTable = loadTransitionTableFromFile();
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
        saveTransitionTableInToFile();
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

    public void updateTransitionTable(State newState){
        State existedState = transitionTable.get(newState.getHashKey());
        if (existedState != null) {
            existedState.mergeActionsInRandomPrecision(newState);
        } else {
            transitionTable.put(newState.getHashKey(), newState);
        }
    }

    public HashMap<String,State> loadTransitionTableFromFile(){
        HashMap<String,State> oldTransitionTable = new HashMap<>();

        return oldTransitionTable;
    }

    public void saveTransitionTableInToFile(){

    }
}