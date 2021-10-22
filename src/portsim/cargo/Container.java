package portsim.cargo;

import portsim.util.Encodable;

/**
 * Represents a shipping container, used for holding or transporting something.
 *
 * @ass1_partial
 */
public class Container extends Cargo implements Encodable {
    /**
     * The type of the container
     */
    private ContainerType type;

    /**
     * Creates a new Container of the specified {@link ContainerType},
     * with the given ID and destination.
     *
     * @param id          cargo ID
     * @param destination destination port
     * @param type        type of container
     * @throws IllegalArgumentException if a cargo already exists with the
     *                                  given ID or ID &lt; 0
     * @ass1
     */
    public Container(int id, String destination, ContainerType type)
        throws IllegalArgumentException {
        super(id, destination);
        this.type = type;
    }

    /**
     * Returns the type of this container.
     *
     * @return container type
     * @ass1
     */
    public ContainerType getType() {
        return type;
    }

    /**
     * Returns the human-readable string representation of this Container.
     * <p>
     * The format of the string to return is
     * <pre>Container id to destination [type]</pre>
     * Where:
     * <ul>
     *   <li>{@code id} is the id of this cargo </li>
     *   <li>{@code destination} is the destination of the cargo </li>
     *   <li>{@code type} is the type of cargo</li>
     * </ul>
     * For example: <pre>Container 42 to Brazil [OTHER]</pre>
     *
     * @return string representation of this Container
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s [%s]",
            super.toString(),
            this.type);
    }

    /**
     * Returns true if and only if this Container is equal to the other given Container.
     * For two Containers to be equal, they must have the same ID, destination and type.
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
        return this.getId() == ((Container) o).getId() &&
                this.getDestination().equals(((Container) o).getDestination()) &&
                this.getType().equals(((Container) o).getType());
    }

    /**
     * Returns the hash code of this Container.
     * Two Containers that are equal according to the equals(Object) method should have the same
     * hash code.
     * @return hash code of this Container.
     */

    @Override
    public int hashCode(){
        String hashCode = getId() + getDestination() + getType().toString();
        return hashCode.hashCode();
    }

    /**
     * Returns the machine-readable string representation of this Container.
     * The format of the string to return is
     *
     * Container:id:destination:type
     * Where:
     * id is the id of this cargo
     * destination is the destination of this cargo
     * type is the container type
     * For example:
     * Container:3:Australia:OPEN_TOP
     * Specified by:
     * encode in interface Encodable
     * @return encoded string representation of this Cargo
     */
    @Override
    public String encode(){
        return super.encode() + ":" + this.getType();
    }

}
