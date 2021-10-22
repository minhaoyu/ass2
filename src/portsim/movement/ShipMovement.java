package portsim.movement;

import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.Encodable;

/**
 * The movement of a ship coming into or out of the port.
 *
 * @ass1_partial
 */
public class ShipMovement extends Movement implements Encodable {

    /**
     * The ship entering of leaving the Port
     */
    private Ship ship;

    /**
     * Creates a new ship movement with the given action time and direction
     * to be undertaken with the given ship.
     *
     * @param time      the time the movement should occur
     * @param direction the direction of the movement
     * @param ship      the ship which that is waiting to move
     * @throws IllegalArgumentException if time &lt; 0
     * @ass1
     */
    public ShipMovement(long time, MovementDirection direction, Ship ship)
        throws IllegalArgumentException {
        super(time, direction);
        this.ship = ship;
    }

    /**
     * Returns the ship undertaking the movement.
     *
     * @return movements ship
     * @ass1
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Returns the human-readable string representation of this ShipMovement.
     * <p>
     * The format of the string to return is
     * <pre>
     * DIRECTION ShipMovement to occur at time involving the ship name </pre>
     * Where:
     * <ul>
     *   <li>{@code DIRECTION} is the direction of the movement </li>
     *   <li>{@code time} is the time the movement is meant to occur </li>
     *   <li>{@code name} is the name of the ship that is being moved</li>
     * </ul>
     * For example:
     * <pre>
     * OUTBOUND ShipMovement to occur at 135 involving the ship Voyager </pre>
     *
     * @return string representation of this ShipMovement
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s involving the ship %s",
            super.toString(),
            this.ship.getName());
    }

    /**
     * Returns the String representation of the current state of this object.
     *
     * @return encoded String representation
     */
    @Override
    public String encode() {
        return super.encode() + ":" + getShip().getImoNumber();

    }

    /**
     * Creates a cargo movement from a string encoding.
     * The format of the string should match the encoded representation of a cargo movement, as
     * described in encode().
     *
     * The encoded string is invalid if any of the following conditions are true:
     *
     * The number of colons (:) detected was more/fewer than expected.
     * The given string is not a CargoMovement encoding
     * The time is not a long (i.e. cannot be parsed by Long.parseLong(String)).
     * The time is less than zero (0).
     * The movementDirection is not one of the valid directions (See MovementDirection).
     * The number of ids is not a int (i.e. cannot be parsed by Integer.parseInt(String)).
     * The number of ids is less than one (1).
     * An id is not a int (i.e. cannot be parsed by Integer.parseInt(String)).
     * An id is less than zero (0).
     * There is no cargo that exists with a specified id.
     * The number of id's does not match the number specified.
     * @param string - string containing the encoded CargoMovement
     * @return decoded CargoMovement instance
     * @throws BadEncodingException n - if the format of the given string is invalid according to the rules above
     */
    public static CargoMovement fromString​(String string) throws BadEncodingException {
        CargoMovement cargoMovement = null;
        return cargoMovement;
    }

}
