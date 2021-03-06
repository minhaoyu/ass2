package portsim.ship;

import portsim.cargo.Cargo;
import portsim.port.Quay;
import portsim.util.Encodable;
import portsim.util.NoSuchShipException;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a ship whose movement is managed by the system.
 * <p>
 * Ships store various types of cargo which can be loaded and unloaded at a port.
 *
 * @ass1_partial
 */
public abstract class Ship implements Encodable {
    /**
     * Name of the ship
     */
    private String name;

    /**
     * Unique 7 digit identifier to identify this ship (no leading zero's [0])
     */
    private long imoNumber;

    /**
     * Port of origin of ship
     */
    private String originFlag;

    /**
     * Maritime flag designated for use on this ship
     */
    private NauticalFlag flag;

    /**
     * Database of all ships currently active in the simulation
     */
    private static Map<Long, Ship> shipRegistry = new HashMap<>();

    /**
     * Creates a new ship with the given
     * <a href="https://en.wikipedia.org/wiki/IMO_number">IMO number</a>,
     * name, origin port flag and nautical flag.
     * <p>
     * Finally, the ship should be added to the ship registry with the
     * IMO number as the key.
     *
     * @param imoNumber  unique identifier
     * @param name       name of the ship
     * @param originFlag port of origin
     * @param flag       the nautical flag this ship is flying
     * @throws IllegalArgumentException if a ship already exists with the given
     *                                  imoNumber, imoNumber &lt; 0 or imoNumber is not 7 digits
     *                                  long (no leading zero's [0])
     * @ass1_partial
     */
    public Ship(long imoNumber, String name, String originFlag,
                NauticalFlag flag) throws IllegalArgumentException {
        if (imoNumber < 0) {
            throw new IllegalArgumentException("The imoNumber of the ship "
                + "must be positive: " + imoNumber);
        }
        if (shipExistsâ€‹(imoNumber) || String.valueOf(imoNumber).length() != 7 || String.valueOf(imoNumber).startsWith("0")) {
            throw new IllegalArgumentException("The imoNumber of the ship "
                + "must have 7 digits (no leading zero's [0]): " + imoNumber);
        }else{
            this.imoNumber = imoNumber;
            this.name = name;
            this.originFlag = originFlag;
            this.flag = flag;
            shipRegistry.put(imoNumber, this);
        }


    }

    /**
     * Checks if a ship exists in the simulation using its IMO number.
     * @param imoNumber - unique key to identify ship
     * @return true if there is a ship with key imoNumber else false
     */

    public static boolean shipExistsâ€‹(long imoNumber){
        return shipRegistry.containsKey(imoNumber);
    }

    /**
     * Returns the ship specified by the IMO number.
     * @param imoNumber - unique key to identify ship
     * @return Ship specified by the given IMO number
     * @throws NoSuchShipException - if the ship does not exist
     */
    public static Ship getShipByImoNumberâ€‹(long imoNumber) throws NoSuchShipException {
        if(!shipExistsâ€‹(imoNumber)){
            throw new NoSuchShipException();
        }
        return shipRegistry.get(imoNumber);
    }


    /**
     * Check if this ship can dock with the specified quay according
     * to the conditions determined by the ships type.
     *
     * @param quay quay to be checked
     * @return true if the Quay satisfies the conditions else false
     * @ass1
     */
    public abstract boolean canDock(Quay quay);

    /**
     * Checks if the specified cargo can be loaded onto the ship according
     * to the conditions determined by the ships type and contents.
     *
     * @param cargo cargo to be loaded
     * @return true if the Cargo satisfies the conditions else false
     * @ass1
     */
    public abstract boolean canLoad(Cargo cargo);

    /**
     * Loads the specified cargo onto the ship.
     *
     * @param cargo cargo to be loaded
     * @require Cargo given is able to be loaded onto this ship according to
     * the implementation of {@link Ship#canLoad(Cargo)}
     * @ass1
     */
    public abstract void loadCargo(Cargo cargo);

    /**
     * Returns this ship's name.
     *
     * @return name
     * @ass1
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns this ship's IMO number.
     *
     * @return imoNumber
     * @ass1
     */
    public long getImoNumber() {
        return this.imoNumber;
    }

    /**
     * Returns this ship's flag denoting its origin.
     *
     * @return originFlag
     * @ass1
     */
    public String getOriginFlag() {
        return this.originFlag;
    }

    /**
     * Returns the nautical flag the ship is flying.
     *
     * @return flag
     * @ass1
     */
    public NauticalFlag getFlag() {
        return this.flag;
    }
/**
 * Returns the database of ships currently active in the simulation as a mapping from the ship's
 * IMO number to its Ship instance.
 * Adding or removing elements from the returned map should not affect the original map.
 *  @return ship registry database
 */
    public static Map<Long, Ship> getShipRegistry(){
        return new HashMap<>(shipRegistry);
    }

    /**
     * Returns true if and only if this ship is equal to the other given ship.
     * For two ships to be equal, they must have the same name, flag, origin port, and IMO number.
     * @param o - other object to check equality
     * @return true if equal, false otherwise
     */
    public boolean equalsâ€‹(Object o){
        if(this == o){
            return true;
        }
        if(o == null || this.getClass() != o.getClass()){
            return false;
        }
        return this.getName().equals(((Ship) o).getName()) &&
                this.getFlag().equals(((Ship)o).getFlag()) &&
                this.getOriginFlag().equals(((Ship) o).getOriginFlag()) &&
                this.getImoNumber() == ((Ship) o).getImoNumber();

    }

    /**
     * Returns the hash code of this ship.
     * Two ships that are equal according to the equals(Object) method should have the same hash
     * code.
     * @return hash code of this ship.
     */
    @Override
    public int hashCode(){
        String hashCode = Long.toString(getImoNumber()) + getFlag() + getOriginFlag() + getName() ;
        return hashCode.hashCode();
    }

    /**
     * Returns the human-readable string representation of this Ship.
     * <p>
     * The format of the string to return is
     * <pre>ShipClass name from origin [flag]</pre>
     * Where:
     * <ul>
     *   <li>{@code ShipClass} is the Ship class</li>
     *   <li>{@code name} is the name of this ship</li>
     *   <li>{@code origin} is the country of origin of this ship</li>
     *   <li>{@code flag} is the nautical flag of this ship</li>
     * </ul>
     * For example: <pre>BulkCarrier Evergreen from Australia [BRAVO]</pre>
     *
     * @return string representation of this Ship
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s %s from %s [%s]",
            this.getClass().getSimpleName(),
            this.name,
            this.originFlag,
            this.flag);
    }

    /**
     * Returns the machine-readable string representation of this Ship.
     * The format of the string to return is
     *
     * ShipClass:imoNumber:name:origin:flag
     * Where:
     * ShipClass is the Ship class name
     * imoNumber is the IMO number of the ship
     * name is the name of this ship
     * origin is the country of origin of this ship
     * flag is the nautical flag of this ship
     * For example:
     * Ship:1258691:Evergreen:Australia:BRAVO
     * Specified by:
     * encode in interface Encodable
     * @return encoded string representation of this Ship
     */
    public String encode(){
        return String.format("%s:%d:%s:%s:%s",
                this.getClass().getSimpleName(),
                this.imoNumber,
                this.name,
                this.originFlag,
                this.flag);
    }

    //toDo:fromString
    /**
     * Resets the global ship registry.
     * This utility method is for the testing suite.
     *
     * @given
     */
    public static void resetShipRegistry() {
        Ship.shipRegistry = new HashMap<>();
    }
}
