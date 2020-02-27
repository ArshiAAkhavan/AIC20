package Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static Client.Constants.*;

public class Action {
    private double probability;
    private int reward;
    private ArrayList<Integer> futureMovement;

    public Action(int... futureMovements) {
        probability = 1.0;
        this.reward = 0;
        this.futureMovement = new ArrayList<Integer>(Arrays.stream(futureMovements).boxed().collect(Collectors.toList()));
    }

    public Action() {
        probability = 1.0;
        this.reward = 0;
        this.futureMovement = new ArrayList<>();
    }

    public int calculateReward(State lastState, State thisState) {
        int reward = 0, lastStateUnitsRate = 0, thisStateUnitsRate = 0;

        if (thisState.getGameStatus() == GameStatus.LOOSE) {
            reward += REWARD_FOR_LOOSING;
        } else if (thisState.getMyKingHP() < lastState.getMyKingHP()) {
            reward += REWARD_FOR_BITING_YOUR_KING;
        }
        if (thisState.getGameStatus() == GameStatus.WIN) {
            reward += REWARD_FOR_WINNING;
        } else if (thisState.getOpponentKingHP() < lastState.getOpponentKingHP()) {
            reward += REWARD_FOR_BITING_OPPONENT_KING;
        }

        for (MapUnit mapUnit : lastState.getMapUnits()) {
            lastStateUnitsRate += mapUnit.getHazardOrEffectivenessRate();
        }
        for (MapUnit mapUnit : thisState.getMapUnits()) {
            thisStateUnitsRate += mapUnit.getHazardOrEffectivenessRate();
        }
        if (thisStateUnitsRate > lastStateUnitsRate) {
            reward += REWARD_FOR_BETTER_MAP_SITUATION;
        } else {
            reward += REWARD_FOR_WORSE_MAP_SITUATION;
        }
        return reward;
    }

    public void initialLastStateRewardInRandomPrecision(State lastState, State thisState) {
        int reward = calculateReward(lastState, thisState);
        if (this.getReward() != 0) {
            this.setReward((reward + this.getReward()) / 2);
        } else {
            this.setReward(reward);
        }
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public ArrayList<Integer> getFutureMovement() {
        return futureMovement;
    }

    public void setFutureMovement(ArrayList<Integer> futureMovement) {
        this.futureMovement = futureMovement;
    }
}
