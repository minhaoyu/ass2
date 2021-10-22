package portsim.port;

import portsim.ship.NauticalFlag;
import portsim.ship.Ship;
import portsim.util.Encodable;

import java.util.*;

public class ShipQueue implements Encodable {

    private PriorityQueue<Ship> shipQueue;
    private Map<NauticalFlag,Integer> priority;
    List<Ship> shipList;


    public ShipQueue() {
        priority = new HashMap<>();
        priority.put(NauticalFlag.BRAVO,1);
        priority.put(NauticalFlag.WHISKEY,2);
        priority.put(NauticalFlag.HOTEL,3);
        priority.put(NauticalFlag.NOVEMBER,4);

        shipList = new ArrayList<>();


        this.shipQueue = new PriorityQueue<>((o1, o2) -> {
            if(priority.get(o1.getFlag()) == 4 && priority.get(o2.getFlag()) == 4){
                if("ContainerShip".equals(o1.getClass().getSimpleName())){
                    return -1;
                }else if("ContainerShip".equals(o2.getClass().getSimpleName())){
                    return 1;
                }else{
                    return 0;
                }
            }else{
                return priority.get(o1.getFlag()).compareTo(priority.get(o2.getFlag()));
            }


        });

    }

    /**
     *Gets the next ship to enter the port and removes it from the queue.
     * The same rules as described in peek() should be used for determining which ship to remove
     * and return.
     * @return next ship to dock
     */
    public Ship poll(){
        return shipQueue.poll();
    }

    /**
     * Returns the next ship waiting to enter the port. The queue should not change.
     * The rules for determining which ship in the queue should be returned next are as follows:
     *
     * If a ship is carrying dangerous cargo, it should be returned. If more than one ship is
     * carrying dangerous cargo return the one added to the queue first.
     * If a ship requires medical assistance, it should be returned. If more than one ship
     * requires medical assistance, return the one added to the queue first.
     * If a ship is ready to be docked, it should be returned. If more than one ship is ready to
     * be docked, return the one added to the queue first.
     * If there is a container ship in the queue, return the one added to the queue first.
     * If this point is reached and no ship has been returned, return the ship that was added to
     * the queue first.
     * If there are no ships in the queue, return null.
     * @return next ship in queue
     */
    public Ship peek(){
        return shipQueue.peek();
    }

    /**
     * Adds the specified ship to the queue.
     * @param ship - to be added to queue
     */
    public void add​(Ship ship){
        shipQueue.add(ship);
    }

    /**
     *Returns a list containing all the ships currently stored in this ShipQueue.
     * The order of the ships in the returned list should be the order in which the ships were
     * added to the queue.
     *
     * Adding or removing elements from the returned list should not affect the original list.
     * @return ships in queue
     */

    public List<Ship> getShipQueue(){
        PriorityQueue<Ship> shipQueueCopy = new PriorityQueue<>(shipQueue);
        while(!shipQueueCopy.isEmpty()){
            shipList.add(0, shipQueueCopy.poll());
        }
        Collections.reverse(shipList);
        return shipList;
    }

    /**
     * Returns true if and only if this ship queue is equal to the other given ship queue.
     * For two ship queue to be equal, they must have the same ships in the queue, in the same
     * order.
     * @param o - other object to check equality
     * @return true if equal, false otherwise
     */
    public boolean equals​(Object o){
        boolean result = true;
        if(this.getClass() != o.getClass()){
            return false;
        }else if(((ShipQueue)o).getShipQueue().size() != this.getShipQueue().size()){
            return false;
        }else if(o == null){
            return false;
        }
        for(int i = 0; i<this.getShipQueue().size();i++){
            if(this.getShipQueue().get(i) != ((ShipQueue)o).getShipQueue().get(i)){
                result = false;
            }
        }
        return result;

    }

    /**
     * Returns the hash code of this ship queue.
     * Two ship queue's that are equal according to equals(Object) method should have the same
     * hash code.
     * @return hash code of this ship queue.
     */
    @Override
    public int hashCode(){
        return getShipQueue().hashCode();
    }

    /**
     * Returns the machine-readable string representation of this ShipQueue.
     * The format of the string to return is
     *
     * ShipQueue:numShipsInQueue:shipID,shipID,...
     * Where:
     * numShipsInQueue is the total number of ships in the ship queue in the port
     * If present (numShipsInQueue > 0): shipID is each ship's ID in the aforementioned queue
     * For example:
     * ShipQueue:0:
     * or
     * ShipQueue:2:3456789,1234567
     * Specified by:
     * encode in interface Encodable
     * @return encoded string representation of this ShipQueue
     */

    public String encode(){
        List<Ship> result = new ArrayList<>(getShipQueue());
        System.out.println(result.get(0).getImoNumber());
        if(result.size() > 0){
            String list = "";
            for(int i = 0; i < result.size(); i++){
                if(i + 1 == result.size()){
                    list += result.get(i).getImoNumber();
                }else{
                    list += result.get(i).getImoNumber() + ",";
                }
            }
            return getClass().getSimpleName() + ":" + result.size() + ":" + list;
        }else{
            return getClass().getSimpleName() + ":" + "0:";

        }
    }

    //toDo:fromString

}
