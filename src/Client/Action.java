package Client;

import java.util.ArrayList;

public class Action {
    private double probability;
    private int reward;
    private ArrayList<Integer> futureMovement;

    public Action() {
        probability = 1.0;
        this.reward = 0;
        futureMovement = new ArrayList<>();
    }

    public Action(double probability, int reward) {
        this.probability = probability;
        this.reward = reward;
        futureMovement = new ArrayList<>();
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
