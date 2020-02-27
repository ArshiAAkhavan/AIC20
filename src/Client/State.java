package Client;

import Client.Model.Player;
import Client.Model.Unit;
import Client.Model.World;

import static Client.Constants.*;
import static Client.GameStatus.*;

import java.util.ArrayList;

public class State {
    private int myKingHP;
    private int AP;
    private int opponentKingHP;
    private GameStatus gameStatus;
    private ArrayList<MapUnit> mapUnits;
    private ArrayList<Action> actions;

    public State(World world) {
        this.myKingHP = world.getMe().getKing().getHp() / KING_HP_DELIMITER + 1;
        this.opponentKingHP = world.getFirstEnemy().getHp() / KING_HP_DELIMITER + 1;

        this.gameStatus = setGameStatusForSingleGame(world.getMe().getKing().getHp(), world.getFirstEnemy().getKing().getHp(), world.getCurrentTurn());

        this.AP = world.getMe().getAp();

        mapUnits = new ArrayList<>();
        for (Unit unit : world.getMap().getUnits()) {
            mapUnits.add(new MapUnit(unit, world));
        }

        actions = new ArrayList<>();
        calculateFutureActions(world.getMe(), actions);
    }

    public void calculateFutureActions(Player player, ArrayList<Action> actions) {
        for (int i = 0; i < player.getHand().size(); i++) {
            //single using unit
            if (player.getAp() >= player.getHand().get(i).getAp()) {
                actions.add(new Action(player.getHand().get(i).getAp()));
            }
            for (int j = i + 1; j < player.getHand().size(); j++) {
                //double using unit
                if (player.getAp() >= player.getHand().get(i).getAp() + player.getHand().get(j).getAp()) {
                    actions.add(new Action(player.getHand().get(i).getAp(), player.getHand().get(j).getAp()));
                }
                for (int k = j + 1; k < player.getHand().size(); k++) {
                    //triple using unit
                    if (player.getAp() >= player.getHand().get(i).getAp() + player.getHand().get(j).getAp() + player.getHand().get(k).getAp()) {
                        actions.add(new Action(player.getHand().get(i).getAp(), player.getHand().get(j).getAp(), player.getHand().get(k).getAp()));
                    }
                }
            }
        }
        //dont using unit
        actions.add(new Action());
    }

    public int getOpponentKingHP() {
        return opponentKingHP;
    }

    public void setOpponentKingHP(int opponentKingHP) {
        this.opponentKingHP = opponentKingHP;
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
