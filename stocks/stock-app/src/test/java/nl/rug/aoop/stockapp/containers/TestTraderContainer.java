package nl.rug.aoop.stockapp.containers;

import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.trader.TraderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestTraderContainer {

    TraderContainer traderContainer;
    Map<String, TraderInfo> traderMap;
    Map<String, Integer> stockMap;
    TraderInfo testTrader;

    @BeforeEach
    public void setUp() {
        traderContainer = new TraderContainer();
        traderMap = new HashMap<>();
        testTrader = Mockito.mock(TraderInfo.class);
        Mockito.when(testTrader.getId()).thenReturn("john");
    }

    @Test
    public void testTraderMapConstructor() {
        traderMap.put(testTrader.getId(), testTrader);
        TraderContainer mapTraderContainer = new TraderContainer(traderMap);
        assertEquals(traderMap, mapTraderContainer.getTraders());
    }

    @Test
    public void testTraderContainerDefaultConstructor() {
        assertEquals(new HashMap<String, TraderInfo>(), traderContainer.getTraders());
    }

    @Test
    public void testAddTrader() {
        traderMap.put(testTrader.getId(), testTrader);
        traderContainer.addEntry(testTrader);
        assertEquals(traderMap, traderContainer.getTraders());
    }

    @Test
    public void testGetTraderInfo() {
        traderContainer.addEntry(testTrader);
        assertEquals(testTrader, traderContainer.getInfo("john"));
    }

    @Test
    public void testRemoveTrader() {
        traderContainer.addEntry(testTrader);
        traderContainer.removeEntry("john");
        Map<String, TraderInfo> emptyMap = new HashMap<>();
        assertEquals(traderContainer.getTraders(), emptyMap);
    }

    @Test
    public void testUpdateTrader() {
        traderContainer.addEntry(testTrader);
        LimitOrder limitOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(limitOrder.getTraderId()).thenReturn("john");
        traderContainer.update(limitOrder, 20, 40);
        Mockito.verify(testTrader).updateTrader(limitOrder, 20, 40);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(traderContainer.isEmpty());
        traderContainer.addEntry(testTrader);
        assertFalse(traderContainer.isEmpty());
    }

    @Test
    public void testGetSize() {
        assertEquals(0, traderContainer.getSize());
        traderContainer.addEntry(testTrader);
        assertEquals(1, traderContainer.getSize());
        TraderInfo newTrader = Mockito.mock(TraderInfo.class);
        Mockito.when(newTrader.getId()).thenReturn("CRP");
        traderContainer.addEntry(newTrader);
        assertEquals(2, traderContainer.getSize());
    }
}
