package portsim.port;

import portsim.cargo.Cargo;
import portsim.evaluators.ShipThroughputEvaluator;
import portsim.evaluators.StatisticsEvaluator;
import portsim.movement.CargoMovement;
import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.Encodable;
import portsim.util.NoSuchCargoException;
import portsim.util.Tickable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * A place where ships can come and dock with Quays to load / unload their
 * cargo.
 * <p>
 * Ships can enter a port through its queue. Cargo is stored within the port at warehouses.
 *
 * @ass1_partial
 */
public class Port implements Encodable, Tickable {

    /**
     * The name of this port used for identification
     */
    private String name;
    /**
     * The quays associated with this port
     */
    private List<Quay> quays;
    /**
     * The cargo currently stored at the port at warehouses. Cargo unloaded from trucks / ships
     */
    private List<Cargo> storedCargo;
    /**
     * The time since the simulation was started
     */
    private long time;
/**
 * statistics evaluators list
 */
    private List<StatisticsEvaluator> statisticsEvaluator;

    /**
     * priorityQueue
     */
    private PriorityQueue<Movement> priorityQueue;
    /**
     * ShipQueue object
     */
    private ShipQueue shipQueue;



    /**
     * Creates a new port with the given name.
     * <p>
     * The time since the simulation was started should be initialised as 0.
     * <p>
     * The list of quays in the port, stored cargo (warehouses) and statistics evaluators should be
     * initialised as empty lists.
     * <p>
     * An empty ShipQueue should be initialised, and a PriorityQueue should be initialised
     * to store movements ordered by the time of the movement (see {@link Movement#getTime()}).
     *
     * @param name name of the port
     * @ass1_partial
     */
    public Port(String name) {
        this.name = name;
        this.time = 0;
        this.priorityQueue = new PriorityQueue<>(new Comparator<Movement>() {
            @Override
            public int compare(Movement o1, Movement o2) {
                return Long.compare(o1.getTime(), o2.getTime());
            }
        });
        this.shipQueue = null;
        this.statisticsEvaluator = new ArrayList<>();
        this.quays = new ArrayList<Quay>();
        this.storedCargo = new ArrayList<Cargo>();
    }

    /**
     * Creates a new port with the given name, time elapsed, ship queue, quays and stored cargo.
     * The list of statistics evaluators should be initialised as an empty list.
     *
     * A PriorityQueue should be initialised to store movements ordered by the time of the
     * movement (see Movement.getTime()).
     * @param name - name of the port
     * @param time - number of minutes since simulation started
     * @param shipQueue - ships waiting to enter the port
     * @param quays - the port's quays
     * @param storedCargo storedCargo - the cargo stored at the port
     * @throws IllegalArgumentException - if time < 0
     */

    public Port(String name, long time,ShipQueue shipQueue, List<Quay> quays,
                List<Cargo> storedCargo) throws IllegalArgumentException{

        if(time < 0){
            throw new IllegalArgumentException();
        }
        this.statisticsEvaluator = new ArrayList<>();
        this.name = name;
        this.quays = quays;
        this.storedCargo = storedCargo;
        this.time = time;
        this.shipQueue = shipQueue;
    }

    /**
     * Adds a movement to the PriorityQueue of movements.
     * If the given movement's action time is less than the current number of minutes elapsed
     * than an IllegalArgumentException should be thrown.
     * @param movement  - movement to add
     * @throws IllegalArgumentException
     */
    public void addMovement​(Movement movement) throws IllegalArgumentException{
        if(movement.getTime() < time){
            throw new IllegalArgumentException();
        }
        priorityQueue.add(movement);
    }

    /**
     * Processes a movement.
     * The action taken depends on the type of movement to be processed.
     *
     * If the movement is a ShipMovement:
     *
     * If the movement direction is INBOUND then the ship should be added to the ship queue.
     * If the movement direction is OUTBOUND then any cargo stored in the port whose destination
     * is the ship's origin port should be added to the ship according to Ship.canLoad(Cargo).
     * Next, the ship should be removed from the quay it is currently docked in (if any).
     * If the movement is a CargoMovement:
     * If the movement direction is INBOUND then all of the cargo that is being moved should be
     * added to the port's stored cargo.
     * If the movement direction is OUTBOUND then all cargo with the given IDs should be removed
     * from the port's stored cargo.
     * Finally, the movement should be forwarded onto each statistics evaluator stored by the
     * port by calling StatisticsEvaluator.onProcessMovement(Movement).
     * @param movement - movement to execute
     */
    public void processMovement​(Movement movement){
        if(movement instanceof ShipMovement){
            if(movement.getDirection().equals(MovementDirection.OUTBOUND)){
                Ship ship = ((ShipMovement)movement).getShip();
                for(int i=0; i<quays.size();i++){
                    if(quays.get(i).getShip()==ship){
                        quays.get(i).shipDeparts();
                        quays.remove(i);
                    }
                }
                for (int i=0; i<storedCargo.size();i++){
                    if(storedCargo.get(i).getDestination().equals(ship.getOriginFlag()) && ship.canLoad(storedCargo.get(i))){
                            ship.loadCargo(storedCargo.get(i));
                    }
                }

            }else{
                Ship ship = ((ShipMovement)movement).getShip();
                shipQueue.add​(ship);
            }
        } else if(movement instanceof CargoMovement){
            List<Cargo> cargo = new ArrayList<>(((CargoMovement)movement).getCargo());
            if(movement.getDirection().equals(MovementDirection.OUTBOUND)){
                for (int i=0; i<cargo.size();i++){
                    for(int j=0;j<storedCargo.size();j++){
                        if(cargo.get(i).getId() == storedCargo.get(j).getId()){
                            storedCargo.remove(j);
                        }
                    }
                }
            }else {
                storedCargo.addAll(cargo);

            }

        }

       for(int i=0; i<statisticsEvaluator.size();i++){
           statisticsEvaluator.get(i).onProcessMovement​(movement);
       }

    }

    /**
     * Adds the given statistics evaluator to the port's list of evaluators.
     * If the port already has an evaluator of that type, no action should be taken.
     * @param eval- statistics evaluator to add to the port
     */
    public void addStatisticsEvaluator​(StatisticsEvaluator eval){
        if(!statisticsEvaluator.contains(eval)){
            statisticsEvaluator.add(eval);
        }
    }

    /**
     *  Returns the time since simulation started
     * @return time in minutes
     */
    public long getTime(){
        return time;
    }

    /**
     * Returns the queue of ships waiting to be docked at this port.
     * @return port's queue of ships
     */
    public ShipQueue getShipQueue(){
        return shipQueue;
    }

    /**
     * Returns the queue of movements waiting to be processed.
     * @return movements queue
     */
    public PriorityQueue<Movement> getMovements(){
        return priorityQueue;
    }

    /**
     * Returns the list of evaluators at the port.
     * Adding or removing elements from the returned list should not affect the original list.
     * @return the ports evaluators
     */
    public List<StatisticsEvaluator> getEvaluators(){
        return new ArrayList<>(this.statisticsEvaluator);
    }

    /**
     * Returns the name of this port.
     *
     * @return port's name
     * @ass1
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a list of all quays associated with this port.
     * <p>
     * Adding or removing elements from the returned list should not affect the original list.
     * <p>
     * The order in which quays appear in this list should be the same as
     * the order in which they were added by calling {@link #addQuay(Quay)}.
     *
     * @return all quays
     * @ass1
     */
    public List<Quay> getQuays() {
        return new ArrayList<>(this.quays);
    }

    /**
     * Returns the cargo stored in warehouses at this port.
     * <p>
     * Adding or removing elements from the returned list should not affect the original list.
     *
     * @return port cargo
     * @ass1
     */
    public List<Cargo> getCargo() {
        return new ArrayList<>(this.storedCargo);
    }

    /**
     * Adds a quay to the ports control.
     *
     * @param quay the quay to add
     * @ass1
     */
    public void addQuay(Quay quay) {
        this.quays.add(quay);
    }

    /**
     * Advances the simulation by one minute.
     * On each call to elapseOneMinute(), the following actions should be completed by the port
     * in order:
     *
     * Advance the simulation time by 1
     * If the time is a multiple of 10, attempt to bring a ship from the ship queue to any empty
     * quay that matches the requirements from Ship.canDock(Quay). The ship should only be docked
     * to one quay.
     * If the time is a multiple of 5, all quays must unload the cargo from ships docked (if any)
     * and add it to warehouses at the port (the Port's list of stored cargo)
     * All movements stored in the queue whose action time is equal to the current time should be
     * processed by processMovement(Movement)
     * Call StatisticsEvaluator.elapseOneMinute() on all statistics evaluators
     */
    @Override
    public void elapseOneMinute() throws NoSuchCargoException {
        time++;
        if(time%10==0){
            for(int i=0; i<quays.size(); i++){
                if(shipQueue.peek().canDock(quays.get(i))){
                    quays.get(i).shipArrives(shipQueue.poll());
                    break;
                }
            }

        }
        if(time%5==0){
            for(int i=0; i<quays.size(); i++){
                if(!quays.get(i).isEmpty()){
                   Ship ship = quays.get(i).getShip();
                   if(ship instanceof BulkCarrier){
                       storedCargo.add(((BulkCarrier)ship).getCargo());
                       try{
                           ((BulkCarrier)ship).unloadCargo();

                       }catch (Exception e){

                       }

                   }else if(ship instanceof ContainerShip){
                       storedCargo.addAll(((ContainerShip)ship).getCargo());
                       try{
                           ((ContainerShip)ship).unloadCargo();
                       }catch (Exception e){

                       }

                   }
                }
            }
        }

        for (Movement movement: priorityQueue) {
            if(movement.getTime() == time){
                for(int i=0; i<statisticsEvaluator.size();i++){
                    statisticsEvaluator.get(i).onProcessMovement​(movement);
                }
            }
    }

        for(int i=0; i<statisticsEvaluator.size();i++){
            statisticsEvaluator.get(i).elapseOneMinute();
        }
}

    /**
     * Returns the machine-readable string representation of this Port.
     * The format of the string to return is
     *
     *  Name
     *  Time
     *  numCargo
     *  EncodedCargo
     *  EncodedCargo...
     *  numShips
     *  EncodedShip
     *  EncodedShip...
     *  numQuays
     *  EncodedQuay
     *  EncodedQuay...
     *  ShipQueue:numShipsInQueue:shipID,shipID,...
     *  StoredCargo:numCargo:cargoID,cargoID,...
     *  Movements:numMovements
     *  EncodedMovement
     *  EncodedMovement...
     *  Evaluators:numEvaluators:EvaluatorSimpleName,EvaluatorSimpleName,...
     *
     * Where:
     * Name is the name of the Port
     * Time is the time elapsed since the simulation started
     * numCargo is the total number of cargo in the simulation
     * If present (numCargo > 0): EncodedCargo is the encoded representation of each individual
     * cargo in the simulation
     * numShips is the total number of ships in the simulation
     * If present (numShips > 0): EncodedShip is the encoded representation of each individual
     * ship encoding in the simulation
     * numQuays is the total number of quays in the Port
     * If present (numQuays > 0): EncodedQuay is the encoded representation of each individual
     * quay in the simulation
     * numShipsInQueue is the total number of ships in the ship queue in the port
     * If present (numShipsInQueue > 0): shipID is each ship's ID in the aforementioned queue
     * numCargo is the total amount of stored cargo in the Port
     * If present (numCargo > 0): cargoID is each cargo's ID in the stored cargo list of Port
     * numMovements is the number of movements in the list of movements in Port
     * If present (numMovements > 0): EncodedMovement is the encoded representation of each
     * individual Movement in the aforementioned list
     * numEvaluators is the number of statistics evaluators in the Port evaluators list
     * If present (numEvaluators > 0): EvaluatorSimpleName is the name given by Class
     * .getSimpleName() for each evaluator in the aforementioned list separated by a comma
     * Each line is separated by a System.lineSeparator()
     * For example the minimum / default encoding would be:
     *
     *  PortName
     *  0
     *  0
     *  0
     *  0
     *  ShipQueue:0:
     *  StoredCargo:0:
     *  Movements:0
     *  Evaluators:0:
     *
     * Specified by:
     * encode in interface Encodable
     * @return
     */
    @Override
    public String encode(){

        List<String> results = new ArrayList<>();
        List<String> cargoId = new ArrayList<>();
        List<String> evals = new ArrayList<>();

        results.add(name);
        results.add(String.valueOf(getTime()));
        results.add(String.valueOf(Cargo.getCargoRegistry().size()));
        for(Cargo cargo: Cargo.getCargoRegistry().values()){
            results.add(cargo.toString());
        }
        results.add(String.valueOf(getShipQueue().getShipQueue().size()));
        for (Ship ship:getShipQueue().getShipQueue()){
            results.add(ship.toString());
        }
        results.add(String.valueOf(getQuays().size()));
        for (Quay quay : quays){
            results.add(quay.toString());
        }
        results.add(shipQueue.toString());
        for (Cargo cargo:storedCargo){
            cargoId.add(String.valueOf(cargo.getId()));
        }
        results.add("StoredCargo:" + storedCargo.size() + ":" + String.join(",", cargoId));
        results.add("Movements:" + priorityQueue.size());
        for(Movement movement : priorityQueue){
            results.add(movement.toString());
        }
        for (StatisticsEvaluator eval: statisticsEvaluator){
            evals.add(eval.getClass().getSimpleName());
        }
        results.add("Evaluators:" + statisticsEvaluator.size() + ":" + String.join(",", evals));
        return String.join("\n", results);

    }

    /**
     * Creates a port instance by reading various ship, quay, cargo, movement and evaluator
     * entities from the given reader.
     * The provided file should be in the format:
     *
     *  Name
     *  Time
     *  numCargo
     *  EncodedCargo
     *  EncodedCargo...
     *  numShips
     *  EncodedShip
     *  EncodedShip...
     *  numQuays
     *  EncodedQuay
     *  EncodedQuay...
     *  ShipQueue:NumShipsInQueue:shipID,shipId
     *  StoredCargo:numCargo:cargoID,cargoID
     *  Movements:numMovements
     *  EncodedMovement
     *  EncodedMovement...
     *  Evaluators:numEvaluators:EvaluatorSimpleName,EvaluatorSimpleName
     *
     * As specified by encode()
     * The encoded string is invalid if any of the following conditions are true:
     *
     * The time is not a valid long (i.e. cannot be parsed by Long.parseLong(String)).
     * The number of cargo is not an integer (i.e. cannot be parsed by Integer.parseInt(String)).
     * The number of cargo to be read in does not match the number specified above. (ie. too many
     * / few encoded cargo following the number)
     * An encoded cargo line throws a BadEncodingException
     * The number of ships is not an integer (i.e. cannot be parsed by Integer.parseInt(String)).
     * The number of ship to be read in does not match the number specified above. (ie. too many
     * / few encoded ships following the number)
     * An encoded ship line throws a BadEncodingException
     * The number of quays is not an integer (i.e. cannot be parsed by Integer.parseInt(String)).
     * The number of quays to be read in does not match the number specified above. (ie. too many
     * / few encoded quays following the number)
     * An encoded quay line throws a BadEncodingException
     * The shipQueue does not follow the last encoded quay
     * The number of ships in the shipQueue is not an integer (i.e. cannot be parsed by Integer
     * .parseInt(String)).
     * The imoNumber of the ships in the shipQueue are not valid longs. (i.e. cannot be parsed by
     * Long.parseLong(String)).
     * Any imoNumber read does not correspond to a valid ship in the simulation
     * The storedCargo does not follow the encoded shipQueue
     * The number of cargo in the storedCargo is not an integer (i.e. cannot be parsed by Integer
     * .parseInt(String)).
     * The id of the cargo in the storedCargo are not valid Integers. (i.e. cannot be parsed by
     * Integer.parseInt(String)).
     * Any cargo id read does not correspond to a valid cargo in the simulation
     * The movements do not follow the encoded storedCargo
     * The number of movements is not an integer (i.e. cannot be parsed by Integer.parseInt
     * (String)).
     * The number of movements to be read in does not match the number specified above. (ie. too
     * many / few encoded movements following the number)
     * An encoded movement line throws a BadEncodingException
     * The evaluators do not follow the encoded movements
     * The number of evaluators is not an integer (i.e. cannot be parsed by Integer.parseInt
     * (String)).
     * The number of evaluators to be read in does not match the number specified above. (ie. too
     * many / few encoded evaluators following the number)
     * An encoded evaluator name does not match any of the possible evaluator classes
     * If any of the following lines are missing:
     * Name
     * Time
     * Number of Cargo
     * Number of Ships
     * Number of Quays
     * ShipQueue
     * StoredCargo
     * Movements
     * Evaluators
     * @param reader - reader from which to load all info
     * @return port created by reading from given reader
     * @throws IOException IOException - if an IOException is encountered when reading from the reader
     * @throws BadEncodingException BadEncodingException - if the reader reads a line that does not adhere to the rules above indicating that the contents of the reader are invalid
     */

    public static Port initialisePort​(Reader reader) throws IOException, BadEncodingException {

        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> results = new ArrayList<>();
        String line;
        Port port = null;
        while ((line=bufferedReader.readLine()) != null){
            results.add(line);
            line = bufferedReader.readLine();
        }
        try{
            if(!results.get(0).startsWith("Name")){
                throw new BadEncodingException();
            }
            if(!results.get(1).startsWith("Time")){
                throw new BadEncodingException();
            }
            if(!results.get(2).startsWith("Number of Cargo")){
                throw new BadEncodingException();
            }
            if(!results.get(3).startsWith("Number of Ships")){
                throw new BadEncodingException();
            }
            if(!results.get(4).startsWith("Number of Quays")){
                throw new BadEncodingException();
            }
            if(!results.get(5).startsWith("ShipQueue")){
                throw new BadEncodingException();
            }
            if(!results.get(6).startsWith("StoredCargo")){
                throw new BadEncodingException();
            }
            if(!results.get(7).startsWith("Movements")){
                throw new BadEncodingException();
            }
            if(!results.get(8).startsWith("Evaluators")){
                throw new BadEncodingException();
            }
            String name = results.get(0);
            port = new Port(name);
            int cargo = Integer.parseInt(results.get(2));
     



        }catch (Exception e){

            
        }
        return port;
    }

}
