package portsim.port;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import portsim.cargo.Container;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ShipQueueTest {

    private ShipQueue shipQueue, shipQueue2,shipQueue3;
    private BulkCarrier bulkCarrier, bulkCarrier2, bulkCarrier3;
    private ContainerShip containerShip;
    private List<Ship> ship;
    private String results = null;

    @Before
    public void setUp() throws Exception {
        shipQueue = new ShipQueue();
        shipQueue2 = new ShipQueue();
        shipQueue3 = new ShipQueue();
        results = "ShipQueue:4:1234567,1245678,7654321,1345678";

        bulkCarrier = new BulkCarrier(1234567,"BulkCarrier","China", NauticalFlag.BRAVO,120);
        bulkCarrier2 = new BulkCarrier(1245678,"BulkCarrier2","China", NauticalFlag.WHISKEY,120);
        bulkCarrier3 = new BulkCarrier(1345678,"BulkCarrier3","China", NauticalFlag.NOVEMBER,120);
        containerShip = new ContainerShip(7654321,"containerShip","China",NauticalFlag.NOVEMBER,
                120);
        shipQueue.add​(bulkCarrier);
        shipQueue.add​(bulkCarrier2);
        shipQueue.add​(bulkCarrier3);
        shipQueue.add​(containerShip);

        shipQueue2.add​(bulkCarrier);
        shipQueue2.add​(bulkCarrier2);
        shipQueue2.add​(bulkCarrier3);

        shipQueue3.add​(bulkCarrier);
        shipQueue3.add​(bulkCarrier2);
        shipQueue3.add​(bulkCarrier3);
        shipQueue3.add​(containerShip);
        ship = new ArrayList<>();
        ship.add(bulkCarrier);
        ship.add(bulkCarrier2);
        ship.add(containerShip);
        ship.add(bulkCarrier3);





    }
    @Test
    public void testPoll(){
        assertEquals(bulkCarrier,shipQueue.poll());

    }
    @Test
    public void testPeak(){
        assertEquals(bulkCarrier,shipQueue.peek());
    }

    @Test
    public void testGetShipQueue(){
        assertEquals(ship,shipQueue.getShipQueue());
    }

    @Test
    public void testEquals(){
        assertTrue(shipQueue.equals​(shipQueue3));
        assertFalse(shipQueue2.equals​(shipQueue));
    }

    @Test
    public void testHashCode(){
        assertEquals(shipQueue.hashCode(),shipQueue3.hashCode());
        assertNotEquals(shipQueue2.hashCode(),shipQueue.hashCode());
    }


    @Test
    public void testEncode(){

       //assertEquals(shipQueue.getShipQueue().size(),4);
        assertEquals(results,shipQueue.encode());
    }

    @After
    public void tearDown() throws Exception {
    }

}