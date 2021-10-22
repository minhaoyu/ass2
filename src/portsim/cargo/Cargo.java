package portsim.cargo;

import portsim.util.BadEncodingException;
import portsim.util.Encodable;
import portsim.util.NoSuchCargoException;

import java.util.HashMap;
import java.util.Map;

/**
 * Denotes a cargo whose function is to be transported via a Ship or land
 * transport.
 * <p>
 * Cargo is kept track of via its ID.
 *
 * @ass1_partial
 */
public abstract class Cargo implements Encodable {
    /**
     * The ID of the cargo instance
     */
    private int id;

    /**
     * Destination for this cargo
     */
    private String destination;

    /**
     * Database of all cargo currently active in the simulation
     */
    private static Map<Integer, Cargo> cargoRegistry = new HashMap<>();

    /**
     * Creates a new Cargo with the given ID and destination port.
     * <p>
     * When a new piece of cargo is created, it should be added to the cargo registry.
     * @param id          cargo ID
     * @param destination destination port
     * @throws IllegalArgumentException if a cargo already exists with the
     *                                  given ID or ID &lt; 0
     * @ass1_partial
     */
    public Cargo(int id, String destination) throws IllegalArgumentException {
        if (cargoRegistry.containsKey(id)|| id < 0) {
            throw new IllegalArgumentException("Cargo ID must be greater than"
                + " or equal to 0: " + id);
        }else{
            this.id = id;
            this.destination = destination;
            cargoRegistry.put(id,this);
        }


    }

    /**
     * Retrieve the ID of this piece of cargo.
     *
     * @return the cargo's ID
     * @ass1
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the destination of this piece of cargo.
     *
     * @return the cargo's destination
     * @ass1
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the human-readable string representation of this cargo.
     * <p>
     * The format of the string to return is
     * <pre>CargoClass id to destination</pre>
     * Where:
     * <ul>
     *   <li>{@code CargoClass} is the cargo class name</li>
     *   <li>{@code id} is the id of this cargo </li>
     *   <li>{@code destination} is the destination of the cargo </li>
     * </ul>
     * <p>
     * For example: <pre>Container 55 to New Zealand</pre>
     *
     * @return string representation of this Cargo
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s %d to %s",
            this.getClass().getSimpleName(),
            this.id,
            this.destination);
    }

    /**
     *Returns the global registry of all pieces of cargo, as a mapping from cargo IDs to Cargo
     * instances.
     * Adding or removing elements from the returned map should not affect the original map.
     * @return cargo registry
     */
    public static Map<Integer, Cargo> getCargoRegistry(){
        return new HashMap<>(cargoRegistry);
    }

    /**
     * Checks if a cargo exists in the simulation using its ID.
     * @param id- unique key to identify cargo
     * @return true if there is a cargo stored in the registry with key id; false otherwise
     */
    public static boolean cargoExists​(int id){
        return cargoRegistry.containsKey(id);
    }

    /**
     * Returns the cargo specified by the given ID.
     * @param id - unique key to identify cargo
     * @return cargo specified by the id
     * @throws NoSuchCargoException-if the cargo does not exist in the registry
     */
    public static Cargo getCargoById​(int id) throws NoSuchCargoException {
        if(cargoRegistry.get(id) == null){
            throw new NoSuchCargoException();
        }else{
            return cargoRegistry.get(id);
        }
    }


    /**
     * Returns true if and only if this cargo is equal to the other given cargo.
     * For two cargo to be equal, they must have the same ID and destination.
     * @param o - other object to check equality
     * @return true if equal, false otherwise
     */
    public boolean equals​(Object o){
        if(this == o){
            return true;
        }
        if(o == null || this.getClass() != o.getClass()){
            return false;
        }
        return this.getId() == ((Cargo) o).getId() && this.getDestination().equals(((Cargo) o).getDestination());
    }

    /**
     * Returns the hash code of this cargo.
     * Two cargo are equal according to equals(Object) method should have the same hash code.
     * @return hash code of this cargo.
     */
    @Override
    public int hashCode(){
        String hashCode = getId() + getDestination();
        return hashCode.hashCode();
    }

    /**
     * Returns the machine-readable string representation of this Cargo.
     * The format of the string to return is
     *
     * CargoClass:id:destination
     * Where:
     * CargoClass is the Cargo class name
     * id is the id of this cargo
     * destination is the destination of this cargo
     * For example:
     *
     * Container:3:Australia
     * OR
     * BulkCargo:2:France
     * Specified by:
     * encode in interface Encodable
     * @return encoded string representation of this Cargo
     */
    @Override
    public String encode(){
        return String.format("%s:%d:%s",
                this.getClass().getSimpleName(),
                this.id,
                this.destination);
    }

    /**
     * Reads a piece of cargo from its encoded representation in the given string.
     * The format of the given string should match the encoded representation of a Cargo, as
     * described in encode() (and subclasses).
     *
     * The encoded string is invalid if any of the following conditions are true:
     *
     * The number of colons (:) detected was more/fewer than expected.
     * The cargo's type specified is not one of Container or BulkCargo
     * The cargo id is not an integer (i.e. cannot be parsed by Integer.parseInt(String)).
     * The cargo id is less than zero (0).
     * A piece of cargo with the specified ID already exists
     * The cargo type specified is not one of BulkCargoType or ContainerType
     * If the cargo type is a BulkCargo:
     * The cargo weight in tonnes is not an integer (i.e. cannot be parsed by Integer.parseInt
     * (String)).
     * The cargo weight in tonnes is less than zero (0).
     * @param string string - string containing the encoded cargo
     * @return decoded cargo instance
     * @throws BadEncodingException BadEncodingException - if the format of the given string is invalid according to the rules above
     */

    public static Cargo fromString​(String string) throws BadEncodingException {
        String[]encoded = string.split(":");
        try {
            String stringId = encoded[1];
            String destination = encoded[2];
            int id;
            try{
                id = Integer.parseInt(stringId);
            }catch (Exception e){
                throw new BadEncodingException();
            }
            if(cargoExists​(id)){
                throw new BadEncodingException();
            }
            if(encoded[0].equals("Container")){
                return new Container(id,destination,ContainerType.valueOf(encoded[3]));
            }else if(encoded[0].equals("BulkCargo")){
                return new BulkCargo(id,destination,Integer.parseInt(encoded[4]),
                        BulkCargoType.valueOf(encoded[3]));
            }

        }catch (Exception e){
            throw new BadEncodingException();
        }


        throw new BadEncodingException();
    }


    /**
     * Resets the global cargo registry.
     * This utility method is for the testing suite.
     *
     * @given
     */
    public static void resetCargoRegistry() {
        Cargo.cargoRegistry = new HashMap<>();
    }
}
