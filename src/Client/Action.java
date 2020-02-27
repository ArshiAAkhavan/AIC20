package Client;

import Client.Model.Player;

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

    public int calculateReward(State lastState, State thisState, Player me, Player opponent) {
        int reward = 0;

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
        reward += REWARD_FOR_KILLING_YOUR_UNIT * me.getDiedUnits().size();
        reward += REWARD_FOR_KILLING_OPPONENT_UNIT * opponent.getDiedUnits().size();
        return reward;
    }

    public void initialLastStateRewardInRandomPrecision(State lastState, State thisState, Player me, Player opponent) {
        int reward = calculateReward(lastState, thisState, me, opponent);
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
