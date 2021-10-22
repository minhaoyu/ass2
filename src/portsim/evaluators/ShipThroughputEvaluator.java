package portsim.evaluators;

import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.Ship;
import portsim.util.Tickable;

import java.util.HashMap;
import java.util.Map;

public class ShipThroughputEvaluator extends StatisticsEvaluator implements Tickable {


    /**
     * Numbers with the given time
     */
    Map<Ship, Long> count;
    /**
     * count the number of ships
     */

    /**
     * Constructs a new ShipThroughputEvaluator.
     * Immediately after creating a new ShipThroughputEvaluator, getThroughputPerHour() should
     * return 0.
     */
    public ShipThroughputEvaluator() {
        super();
        count = new HashMap<>();

    }

    /**
     * Return the number of ships that have passed through the port in the last 60 minutes.
     * @return ships throughput
     */
    public int getThroughputPerHour(){
        return count.size();
    }

    /**
     * Updates the internal count of ships that have passed through the port using the given
     * movement.
     * If the movement is not an OUTBOUND ShipMovement, this method returns immediately without
     * taking any action.
     *
     * Otherwise, the internal state of this evaluator should be modified such that
     * getThroughputPerHour() should return a value 1 more than before this method was called. e
     * .g. If the following code and output occurred over a program execution:
     * Example of behaviour
     * Java method call	Returned value
     * getThroughputPerHour()	1
     * onProcessMovement(validMovement)	void
     * getThroughputPerHour()	2
     * Where validMovement is an OUTBOUND ShipMovement.
     *
     * Specified by:
     * onProcessMovement in class StatisticsEvaluator
     * @param movement - movement to read
     */
    @Override
    public void onProcessMovement​(Movement movement) {
        if(movement.getDirection().equals(MovementDirection.OUTBOUND)){
            if(movement instanceof ShipMovement){
                Ship ship = ((ShipMovement)movement).getShip();
                long time = movement.getTime();
                count.put(ship,time);
            }
        }
    }

    /**
     * Simulate a minute passing. The time since the evaluator was created should be incremented
     * by one.
     * If it has been more than 60 minutes since a ship exited the port, it should no longer be
     * counted towards the count returned by getThroughputPerHour().
     */
    @Override
    public void elapseOneMinute(){
        for(Map.Entry<Ship, Long> entry : count.entrySet()){
            if(getTime() - entry.getValue() > 60){
                count.remove(entry.getKey());
            }
        }

    }



}
