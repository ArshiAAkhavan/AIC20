package Client;

import Client.Model.Player;
import Client.Model.Unit;
import Client.Model.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static Client.Constants.*;
import static Client.AI.*;

public class MapUnit implements Comparable<MapUnit>{
    private int hazardOrEffectivenessRate;

    public MapUnit(Unit unit, World world, Player closestEnemy){ // first we calculate the hazard rate or effectiveness rate of map units
        hazardOrEffectivenessRate = (calculateDistanceRate(unit , world ,closestEnemy) * calculateHPRate(unit) * calculateDamageRate(unit) * calculateRangeRate(unit) * calculateTargetTypeRate(unit)) / MAP_UNIT_HAZARD_EFFECTIVENESS_DELIMITER + 1;
        if (unit.getPlayerId() == closestEnemy.getPlayerId()) {
            hazardOrEffectivenessRate *= -1;
        }
    }

    @Override
    public int compareTo(MapUnit o) {
        return Integer.compare(this.getHazardOrEffectivenessRate(), (o.getHazardOrEffectivenessRate()));
    }

    @Override
    public boolean equals(Object obj) {
        return this.hazardOrEffectivenessRate == ((MapUnit) obj).hazardOrEffectivenessRate;
    }

    public int calculateDistanceRate(Unit unit, World world, Player closestEnemy) {
        int distanceRate = 0;
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
        return distanceRate;
    }

    public int calculateTargetTypeRate(Unit unit) {
        int targetRate = 0;
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
        return targetRate;
    }

    public int calculateHPRate(Unit unit) {
        return unit.getHp() / MAP_UNIT_HP_RATE_DELIMITER + 1;
    }

    public int calculateDamageRate(Unit unit) {
        return (unit.getAttack() + unit.getDamageLevel() + 1) / MAP_UNIT_DAMAGE_RATE_DELIMITER - 2;
    }

    public int calculateRangeRate(Unit unit) {
        return (unit.getRange() + unit.getRangeLevel()) / MAP_UNIT_RANGE_RATE_DELIMITER + 1;
    }

    public int getHazardOrEffectivenessRate() {
        return hazardOrEffectivenessRate;
    }

    public void setHazardOrEffectivenessRate(int hazardOrEffectivenessRate) {
        this.hazardOrEffectivenessRate = hazardOrEffectivenessRate;
    }

    public static int calculateMapUnitsSituation(ArrayList<MapUnit> mapUnits) {
        int result = 0;
        for (MapUnit mapUnit : mapUnits) {
            result += mapUnit.getHazardOrEffectivenessRate();
        }
        return result;
    }

    public static String showArrayList(ArrayList<MapUnit> mapUnits){
        StringBuilder result= new StringBuilder("[ ");
        Collections.sort(mapUnits);
        for (MapUnit mapUnit : mapUnits) {
            result.append(mapUnit.getHazardOrEffectivenessRate() + ' ');
        }
        return result + "]";
    }
}
