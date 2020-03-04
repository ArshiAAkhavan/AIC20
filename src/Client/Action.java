package Client;

import Client.Model.Player;

import static Client.UsefulMethods.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static Client.Constants.*;

public class Action {
    private double P;
    private int R;
    private ArrayList<Integer> FM;// future movements

    public Action(int... futureMovements) {
        P = 1.0;
        this.R = 0;
        this.FM = new ArrayList<Integer>(Arrays.stream(futureMovements).boxed().collect(Collectors.toList()));
    }

    public Action() {
        P = 1.0;
        this.R = 0;
        this.FM = new ArrayList<>();
    }

    public String toString() {
        return "probability = " + P + '\n' +
                "reward = " + R + '\n' +
                "futureMovements = " + FM.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTwoArrayList(this.FM, ((Action) obj).FM);
    }

    public int calculateReward(State lastState, State thisState, Player me, Player closestEnemy) {
        int reward = 0;
        if (thisState.getGameStatus() == GameStatus.LOOSE) {
            reward += REWARD_FOR_LOOSING;
        } else if (thisState.getMyKingHP() < lastState.getMyKingHP()) {
            reward += REWARD_FOR_BITING_YOUR_KING;
        }
        if (thisState.getGameStatus() == GameStatus.WIN) {
            reward += REWARD_FOR_WINNING;
        } else if (thisState.getOppKingHP() < lastState.getOppKingHP()) {
            reward += REWARD_FOR_BITING_OPPONENT_KING;
        }
        reward += REWARD_FOR_KILLING_YOUR_UNIT * me.getDiedUnits().size();
        reward += REWARD_FOR_KILLING_OPPONENT_UNIT * closestEnemy.getDiedUnits().size();
        return reward;
    }

    public void initialLastStateRewardInRandomPrecision(State lastState, State thisState, Player me, Player closestEnemy) {
        int reward = calculateReward(lastState, thisState, me, closestEnemy);
        if (this.getR() != 0) {
            this.setR((reward + this.getR()) / 2);
        } else {
            this.setR(reward);
        }
    }

    public double getP() {
        return P;
    }

    public void setP(double p) {
        this.P = p;
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        this.R = r;
    }

    public ArrayList<Integer> getFM() {
        return FM;
    }

    public void setFM(ArrayList<Integer> FM) {
        this.FM = FM;
    }
}
