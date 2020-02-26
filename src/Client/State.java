package Client;

import Client.Model.Unit;
import Client.Model.World;

import static Client.Constants.*;

import java.util.ArrayList;

public class State {
    private int kingHP;
    private int AP;
    private int opponentKingHP;
    private ArrayList<mapUnit> mapUnits;
    private ArrayList<Action> actions;

    public State(World world) {
        this.kingHP = world.getMe().getKing().getHp() / KING_HP_DELIMITER + 1;
        this.AP = world.getMe().getAp();
        this.opponentKingHP = world.getFirstEnemy().getHp() / KING_HP_DELIMITER + 1;
        mapUnits = new ArrayList<>();
        for (Unit unit : world.getMap().getUnits()) {
            mapUnits.add(new mapUnit(unit, world));
        }
        actions = new ArrayList<>();
        //todo set initial actions
    }

    public int getOpponentKingHP() {
        return opponentKingHP;
    }

    public void setOpponentKingHP(int opponentKingHP) {
        this.opponentKingHP = opponentKingHP;
    }

    public int getKingHP() {
        return kingHP;
    }

    public void setKingHP(int kingHP) {
        this.kingHP = kingHP;
    }

    public int getAP() {
        return AP;
    }

    public void setAP(int AP) {
        this.AP = AP;
    }

    public ArrayList<mapUnit> getMapUnits() {
        return mapUnits;
    }

    public void setMapUnits(ArrayList<mapUnit> mapUnits) {
        this.mapUnits = mapUnits;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
