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
    private State lastState;
    private Action lastAction;
    private Random random = new Random();

    public void pick(World world) {
        System.out.println("random pick started");

        List<BaseUnit> myRandomHand = new ArrayList<>();

        // choosing random hand
        for (int i = 0; i < world.getGameConstants().getHandSize(); i++) {
            myRandomHand.add(world.getBaseUnitById(random.nextInt(world.getAllBaseUnits().size())));
        }

        // picking the chosen hand - rest of the hand will automatically be filled with random baseUnits
        world.getMe().setHand(myRandomHand);

        //making initial state
        lastState = new State(world);
    }

    public void turn(World world) {
        System.out.println("turn started: " + world.getCurrentTurn());

        // update last state reward
        State thisState = new State(world);
        if(lastAction!=null)
           lastAction.initialLastStateRewardInRandomPrecision(lastState, thisState, world.getMe(), world.getFirstEnemy());

        // set random action
        Action randomAction = thisState.getActions().get(random.nextInt(thisState.getActions().size()));
        // target for units is king
        for (Integer unitID : randomAction.getFutureMovement()) {
            world.putUnit(unitID, world.getShortestPathToCell(world.getMe(), world.getFirstEnemy().getKing().getCenter()));
        }

        // update args
        lastAction = randomAction;
        lastState = thisState;
    }

    public void end(World world, Map<Integer, Integer> scores) {
        System.out.println("end started");
        System.out.println("My score: " + scores.get(world.getMe().getPlayerId()));
    }
}