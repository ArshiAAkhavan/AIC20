package Client;

import Client.Model.Cell;
import Client.Model.Player;
import Client.Model.Unit;
import Client.Model.World;

import static Client.Constants.*;

public class MapUnit {
    private int hazardOrEffectivenessRate;

    public MapUnit(Unit unit, World world, Player closestEnemy) { // first we calculate the hazard rate or effectiveness rate of map units
        int distanceRate, hpRate, damageRate, rangeRate, targetRate = 0;

        //distance rate :
        if (unit.getPlayerId() == world.getMe().getPlayerId()) {
            distanceRate = calculateShortestPath(world, closestEnemy, unit);
        } else {
            distanceRate = calculateShortestPath(world, world.getMe(), unit);
        }
        if (distanceRate / MAP_UNIT_DISTANCE_RATE_DELIMITER >= 5) {
            distanceRate = 1;
        } else {
            distanceRate = 5 - distanceRate / MAP_UNIT_DISTANCE_RATE_DELIMITER;
        }

        //hp rate:
        hpRate = unit.getHp() / MAP_UNIT_HP_RATE_DELIMITER + 1;

        //damage rate:
        damageRate = (unit.getAttack() + unit.getDamageLevel() + 1) / MAP_UNIT_DAMAGE_RATE_DELIMITER - 2;

        //range rate:
        rangeRate = (unit.getRange() + unit.getRangeLevel()) / MAP_UNIT_RANGE_RATE_DELIMITER + 1;

        //target type rate:
        if (unit.getBaseUnit().isMultiple()) {
            targetRate = 3;
        } else
            switch (unit.getBaseUnit().getTargetType()) {
                case AIR:
                    targetRate = 1;
                    break;
                case GROUND:
                    targetRate = 2;
                    break;
                case BOTH:
                    targetRate = 3;
                    break;
            }

        hazardOrEffectivenessRate = (distanceRate * hpRate * damageRate * rangeRate * targetRate) / MAP_UNIT_HAZARD_EFFECTIVENESS_DELIMITER + 1;
        if (unit.getPlayerId() == closestEnemy.getPlayerId()) {
            hazardOrEffectivenessRate *= -1;
        }
    }

    public int getHazardOrEffectivenessRate() {
        return hazardOrEffectivenessRate;
    }

    public void setHazardOrEffectivenessRate(int hazardOrEffectivenessRate) {
        this.hazardOrEffectivenessRate = hazardOrEffectivenessRate;
    }

    public static int calculateShortestPath(World world, Player player, Unit unit) {
        int length = 0;
        for (Cell cell : world.getShortestPathToCell(player, unit.getCell()).getCells()) {
            length++;
        }
        return length;
    }
}
