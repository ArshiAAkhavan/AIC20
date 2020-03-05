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
    private int VR;// visited rewards
    private int VP;// visited probabilities
    private ArrayList<Integer> FM;// future movements

    public Action(int... futureMovements) {
        P = 1.0;
        R = 0;
        VR = 1;
        VP = 1;
        this.FM = new ArrayList<Integer>(Arrays.stream(futureMovements).boxed().collect(Collectors.toList()));
    }

    public Action() {
        P = 1.0;
        R = 0;
        VR = 1;
        VP = 1;
        this.FM = new ArrayList<>();
    }

    public String toString() {
        return "probability = " + String.valueOf(P) + '\n' +
                "reward = " + String.valueOf(R) + '\n' +
                "futureMovements = " + FM.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTwoArrayList(this.FM, ((Action) obj).FM);
    }

    public Action findInList(ArrayList<Action> actions) {
        int i, j;
        for (i = 0; i < actions.size(); i++) {
            if (this.getFM().size() != actions.get(i).getFM().size()) {
                continue;
            }
            for (j = 0; j < actions.get(i).getFM().size(); j++) {
                if (!actions.get(i).getFM().get(j).equals(this.getFM().get(j))) {
                    break;
                }
            }
            if (j >= actions.get(i).getFM().size()) {
                return actions.get(i);
            }
        }
        return null;
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
        updateReward(reward);
    }

    public void updateReward(int reward) {
        if (reward == 0) {
            return;
        }
        if (this.getR() != 0) {
            this.setR((reward + this.getR() * this.VR) / (this.VR + 1));
            setVR(VR+1);
        } else {
            this.setR(reward);
        }
    }

    public void updateProbability(double probability) {
        this.setP((probability + this.getP() * this.VP) / (this.VP + 1));
        setVP(VP+1);
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

    public int getVR() {
        return VR;
    }

    public void setVR(int VR) {
        this.VR = VR;
    }

    public int getVP() {
        return VP;
    }

    public void setVP(int VP) {
        this.VP = VP;
    }
}
