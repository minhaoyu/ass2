package portsim.evaluators;

import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.port.Port;
import portsim.ship.Ship;
import portsim.util.Tickable;

public class QuayOccupancyEvaluator extends StatisticsEvaluator implements Tickable {

    /**
     * port to monitor quays
     */
    private Port port;

    /**
     * Constructs a new QuayOccupancyEvaluator.
     * @param port port to monitor quays
     */
    public QuayOccupancyEvaluator(Port port) {
        super();
        this.port = port;
    }

    /**
     * Return the number of quays that are currently occupied.
     * A quay is occupied if Quay.isEmpty() returns false.
     * @return number of quays
     */

    public int getQuaysOccupied(){
        int occupied = 0;
        for (int i =0; i<port.getQuays().size(); i++){
            if(!port.getQuays().get(i).isEmpty()){
                occupied++;
            }
        }
        return occupied;
    }

    /**
     * QuayOccupancyEvaluator does not make use of onProcessMovement(), so this method can be
     * left empty.
     * Does nothing. This method is not used by this evaluator.
     *
     * Specified by:
     * onProcessMovement in class StatisticsEvaluator
     * @param movement - movement to read
     */
    @Override
    public void onProcessMovement​(Movement movement) {

    }
}
