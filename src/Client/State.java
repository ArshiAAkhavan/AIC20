package Client;

import Client.Model.Player;
import Client.Model.Unit;
import Client.Model.World;

import static Client.Constants.*;
import static Client.GameStatus.*;
import static Client.UsefulMethods.*;
import static Client.MapUnit.*;

import java.util.ArrayList;

public class State {
    private int myKingHP;
    private int AP;
    private int oppKingHP;
    private GameStatus gameStatus;
    private ArrayList<MapUnit> mapUnits;
    private ArrayList<Action> actions;

    public State(World world, Player closestEnemy) {
        this.myKingHP = world.getMe().getKing().getHp() / KING_HP_DELIMITER + 1;
        this.oppKingHP = closestEnemy.getHp() / KING_HP_DELIMITER + 1;

        this.gameStatus = setGameStatusForSingleGame(world.getMe().getKing().getHp(), closestEnemy.getKing().getHp(), world.getCurrentTurn());

        this.AP = world.getMe().getAp();

        mapUnits = new ArrayList<>();
        for (Unit unit : world.getMap().getUnits()) {
            if (unit.getPlayerId() == closestEnemy.getPlayerId() || unit.getPlayerId() == world.getMe().getPlayerId()) {
                mapUnits.add(new MapUnit(unit, world, closestEnemy));
            }
        }

        actions = new ArrayList<>();
        calculateFutureActions(world.getMe(), actions);
    }

    public void calculateFutureActions(Player player, ArrayList<Action> actions) {
        for (int i = 0; i < player.getHand().size(); i++) {
            //single using unit
            if (player.getAp() >= player.getHand().get(i).getAp()) {
                actions.add(new Action(player.getHand().get(i).getTypeId()));
            }
            for (int j = i + 1; j < player.getHand().size(); j++) {
                //double using unit
                if (player.getAp() >= player.getHand().get(i).getAp() + player.getHand().get(j).getAp()) {
                    actions.add(new Action(player.getHand().get(i).getTypeId(), player.getHand().get(j).getTypeId()));
                }
                for (int k = j + 1; k < player.getHand().size(); k++) {
                    //triple using unit
                    if (player.getAp() >= player.getHand().get(i).getAp() + player.getHand().get(j).getAp() + player.getHand().get(k).getAp()) {
                        actions.add(new Action(player.getHand().get(i).getTypeId(), player.getHand().get(j).getTypeId(), player.getHand().get(k).getTypeId()));
                    }
                }
            }
        }
        //dont using unit
        actions.add(new Action());
    }

    public String getHashKey() {
        return myKingHP + String.valueOf(AP) + oppKingHP + gameStatus.toString() + showArrayList(mapUnits);
    }

    public void mergeActionsInRandomPrecision(State newState) {
        for (Action newAction : newState.actions) {
            int i;
            for (i = 0; i < this.actions.size(); i++) {
                if (newAction.equals(this.actions.get(i))) {
                    this.actions.get(i).updateReward(newAction.getR());
                    this.actions.get(i).updateProbability(newAction.getP());
                    break;
                }
            }
            if (i >= this.actions.size()) {
                this.actions.add(newAction);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return this.myKingHP == ((State) obj).myKingHP &&
                this.AP == ((State) obj).AP &&
                this.oppKingHP == ((State) obj).oppKingHP &&
                this.gameStatus == ((State) obj).gameStatus &&
                compareTwoArrayList(mapUnits, ((State) obj).mapUnits) &&
                compareTwoArrayList(actions, ((State) obj).actions);
    }

    public String toString() {
        ArrayList<String> actionStrings = new ArrayList<>();
        for (Action action : actions) {
            actionStrings.add(action.toString());
        }
        return "my king hp :" + myKingHP + '\n' +
                "my AP :" + AP + '\n' +
                "my opponent king hp :" + oppKingHP + '\n' +
                "game status :" + gameStatus.toString() + '\n' +
                "actions :" + actionStrings.toString();

    }

    public int getOppKingHP() {
        return oppKingHP;
    }

    public void setOppKingHP(int oppKingHP) {
        this.oppKingHP = oppKingHP;
    }

    public int getMyKingHP() {
        return myKingHP;
    }

    public void setMyKingHP(int myKingHP) {
        this.myKingHP = myKingHP;
    }

    public int getAP() {
        return AP;
    }

    public void setAP(int AP) {
        this.AP = AP;
    }

    public ArrayList<MapUnit> getMapUnits() {
        return mapUnits;
    }

    public void setMapUnits(ArrayList<MapUnit> mapUnits) {
        this.mapUnits = mapUnits;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
