package portsim.evaluators;

import portsim.cargo.*;
import portsim.movement.CargoMovement;
import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;
import portsim.util.Tickable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoDecompositionEvaluator extends StatisticsEvaluator implements Tickable {

    /**
     *Returns the distribution of which cargo types that have entered the port.
     */
    private Map<String,Integer> cargoMap;
    /**
     * Returns the distribution of bulk cargo types that have entered the port.
     */
    private Map<BulkCargoType,Integer> bulkCargoType;
    /**
     * Returns the distribution of container cargo types that have entered the port.
     */
    private Map<ContainerType,Integer> containerType;

    /**
     * Constructs a new CargoDecompositionEvaluator.
     */
    public CargoDecompositionEvaluator() {
        super();
        cargoMap = new HashMap<>();
        bulkCargoType = new HashMap<>();
        containerType = new HashMap<>();
    }

    /**
     * Returns the distribution of which cargo types that have entered the port.
     * @return cargo distribution map
     */
    public Map<String,Integer> getCargoDistribution(){
        return cargoMap;
    }

    /**
     * Returns the distribution of bulk cargo types that have entered the port.
     * @return bulk cargo distribution map
     */
    public Map<BulkCargoType,Integer> getBulkCargoDistribution(){
        return bulkCargoType;
    }

    /**
     * Returns the distribution of container cargo types that have entered the port.
     * @return container distribution map
     */
    public  Map<ContainerType,Integer> getContainerDistribution(){
        return containerType;
    }

    /**
     *Updates the internal distributions of cargo types using the given movement.
     * If the movement is not an INBOUND movement, this method returns immediately without taking
     * any action.
     *
     * If the movement is an INBOUND movement, do the following:
     *
     * If the movement is a ShipMovement, Retrieve the cargo from the ships and for each piece of
     * cargo:
     * If the cargo class (Container / BulkCargo) has been seen before (simple name exists as a
     * key in the cargo map) -> increment that number
     * If the cargo class has not been seen before then add its class simple name as a key in the
     * map with a corresponding value of 1
     * If the cargo type (Value of ContainerType / BulkCargoType) for the given cargo class has
     * been seen before (exists as a key in the map) increment that number
     * If the cargo type (Value of ContainerType / BulkCargoType) for the given cargo class has
     * not been seen before add as a key in the map with a corresponding value of 1
     * If the movement is a CargoMovement, Retrieve the cargo from the movement. For the cargo
     * retrieved:
     * Complete steps 1-4 as given above for ShipMovement
     * Specified by:
     * onProcessMovement in class StatisticsEvaluator
     * @param movement - movement to read
     */

    @Override
    public void onProcessMovement​(Movement movement) {
        if(movement.getDirection().equals(MovementDirection.INBOUND)){
            if(movement instanceof ShipMovement){
                Ship ship = ((ShipMovement)movement).getShip();
                if(ship instanceof BulkCarrier){
                    BulkCargo cargo = ((BulkCarrier)ship).getCargo();
                    if(cargoMap.containsKey("BulkCargo")){
                        cargoMap.put("BulkCargo", cargoMap.get("BulkCargo") + 1);
                    }else{
                        cargoMap.put("BulkCargo",1);
                    }
                    if(bulkCargoType.containsKey(cargo.getType())){
                        bulkCargoType.put(cargo.getType(), bulkCargoType.get(cargo.getType()) + 1);
                    }else{
                        bulkCargoType.put(cargo.getType(), 1);
                    }

                }else if(ship instanceof ContainerShip){
                   List<Container> containerList = new ArrayList<>(((ContainerShip)ship).getCargo());
                   for(int i = 0; i < containerList.size(); i++){
                       if(cargoMap.containsKey("Container")){
                           cargoMap.put("Container", cargoMap.get("Container") + 1);

                       }else{
                           cargoMap.put("Container",1);
                       }
                       if(containerType.containsKey(containerList.get(i).getType())){
                           containerType.put(containerList.get(i).getType(),
                                   containerType.get(containerList.get(i).getType()) + 1);
                       }else{
                           containerType.put(containerList.get(i).getType(), 1);
                       }
                   }

                }

            }
            else if(movement instanceof CargoMovement){
                List<Cargo>cargoList = new ArrayList<>(((CargoMovement)movement).getCargo());
                for(int i = 0; i < cargoList.size(); i++){
                    if(cargoList.get(i) instanceof BulkCargo){
                        BulkCargo bulkCargo = (BulkCargo)(cargoList.get(i));
                        if(cargoMap.containsKey("BulkCargo")){
                            cargoMap.put("BulkCargo", cargoMap.get("BulkCargo") + 1);
                        }else{
                            cargoMap.put("BulkCargo",1);
                        }
                        if(bulkCargoType.containsKey(bulkCargo.getType())){
                            bulkCargoType.put(bulkCargo.getType(), bulkCargoType.get(bulkCargo.getType()) + 1);
                        }else{
                            bulkCargoType.put(bulkCargo.getType(), 1);
                        }
                    }
                    else if(cargoList.get(i) instanceof Container){
                        Container container = (Container)(cargoList.get(i));
                        if(cargoMap.containsKey("Container")){
                            cargoMap.put("Container", cargoMap.get("Container") + 1);

                        }else{
                            cargoMap.put("Container",1);
                        }
                        if(containerType.containsKey(container.getType())){
                            containerType.put(container.getType(),
                                    containerType.get(container.getType()) + 1);
                        }else{
                            containerType.put(container.getType(), 1);
                        }


                    }


                }
            }
        }

    }
}
