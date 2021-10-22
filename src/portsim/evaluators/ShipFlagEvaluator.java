package portsim.evaluators;

import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.Ship;
import portsim.util.Tickable;

import java.util.HashMap;
import java.util.Map;

public class ShipFlagEvaluator extends StatisticsEvaluator implements Tickable {

    /**
     * the flag distribution seen at this port.
     */
    private Map<String, Integer> flagDistribution;

    /**
     * Constructs a new ShipFlagEvaluator.
     *
     */
    public ShipFlagEvaluator() {
        super();
        flagDistribution = new HashMap<>();
    }

    /**
     * Return the flag distribution seen at this port.
     * @return flag distribution
     */
    public Map<String, Integer> getFlagDistribution(){
        return flagDistribution;
    }

    /**
     * Return the number of times the given flag has been seen at the port.
     * @param flag - country flag to find in the mapping
     * @return number of times flag seen or 0 if not seen
     */
    public int getFlagStatistics​(String flag){
        if(flagDistribution.containsKey(flag)){
            return flagDistribution.get(flag);
        }
        return 0;
    }


    /**
     * Updates the internal mapping of ship country flags using the given movement.
     * If the movement is not an INBOUND movement, this method returns immediately without taking
     * any action.
     *
     * If the movement is not a ShipMovement, this method returns immediately without taking any
     * action.
     *
     * If the movement is an INBOUND ShipMovement, do the following:
     *
     * If the flag has been seen before (exists as a key in the map) increment that number
     * If the flag has not been seen before add as a key in the map with a corresponding value of 1
     * Specified by:
     * onProcessMovement in class StatisticsEvaluator
     * @param movement - movement to read
     */
    @Override
    public void onProcessMovement​(Movement movement) {
        if(movement.getDirection().equals(MovementDirection.INBOUND)){
            if(movement instanceof ShipMovement){
                Ship ship = ((ShipMovement)movement).getShip();
                if(flagDistribution.containsKey(ship.getOriginFlag())){
                    flagDistribution.put((ship.getOriginFlag()),
                            flagDistribution.get((ship.getOriginFlag()) + 1));
                }else {
                    flagDistribution.put((ship.getOriginFlag()), 1);
                }
            }
        }

    }
}
