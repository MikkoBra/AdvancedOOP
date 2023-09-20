package nl.rug.aoop.stockapp.containers;


import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stocks.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestStockContainer {

    StockContainer stockContainer;
    Map<String, Stock> stockMap;
    Stock testStock;
    LimitOrder order;

    @BeforeEach
    public void setUp() {
        stockContainer = new StockContainer();
        stockMap = new HashMap<>();

        testStock = Mockito.mock(Stock.class);
        Mockito.when(testStock.getSymbol()).thenReturn("CMP");
        Mockito.when(testStock.getPrice()).thenReturn(40.0);

        order = Mockito.mock(LimitOrder.class);
        Mockito.when(order.getSymbol()).thenReturn("CMP");
        Mockito.when(order.getAmount()).thenReturn(40);
    }

    @Test
    public void testStockContainerConstructor() {
        assertEquals(new HashMap<String, Stock>(), stockContainer.getStocks());
    }

    @Test
    public void testStockMapConstructor() {
        stockMap.put(testStock.getSymbol(), testStock);
        StockContainer mapStockContainer = new StockContainer(stockMap);
        assertEquals(stockMap, mapStockContainer.getStocks());
    }

    @Test
    public void testAddStock() {
        stockMap.put("CMP", testStock);
        stockContainer.addEntry(testStock);
        assertEquals(stockMap, stockContainer.getStocks());
    }

    @Test
    public void testGetStockInfo() {
        stockContainer.addEntry(testStock);
        assertEquals(testStock, stockContainer.getInfo("CMP"));
    }

    @Test
    public void testRemoveStock() {
        stockContainer.addEntry(testStock);
        stockContainer.removeEntry("CMP");
        Map<String, Stock> emptyMap = new HashMap<>();
        assertEquals(stockContainer.getStocks(), emptyMap);
    }

    @Test
    public void testHasEntry() {
        stockContainer.addEntry(testStock);
        assertTrue(stockContainer.hasEntry("CMP"));
        assertFalse(stockContainer.hasEntry("CRP"));
    }

    @Test
    public void testUpdateStock() {
        LimitOrder order = Mockito.mock(LimitOrder.class);
        Mockito.when(order.getSymbol()).thenReturn("CMP");
        Mockito.when(order.getPrice()).thenReturn(40.0);
        stockContainer.addEntry(testStock);
        stockContainer.update(order);
        Mockito.verify(testStock).update(40);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(stockContainer.isEmpty());
        stockContainer.addEntry(testStock);
        assertFalse(stockContainer.isEmpty());
    }

    @Test
    public void testGetSize() {
        assertEquals(0, stockContainer.getSize());
        stockContainer.addEntry(testStock);
        assertEquals(1, stockContainer.getSize());
        Stock cheapStock = Mockito.mock(Stock.class);
        Mockito.when(cheapStock.getSymbol()).thenReturn("CRP");
        stockContainer.addEntry(cheapStock);
        assertEquals(2, stockContainer.getSize());
    }

    @Test
    public void testToNetworkStockContainer() {
        stockContainer.addEntry(testStock);
        NetworkStockContainer netContainer = stockContainer.toNetworkStockContainer();
        Mockito.verify(testStock).toNetworkStock();
        assertNotNull(netContainer);
        assertEquals(NetworkStockContainer.class, netContainer.getClass());
    }

}
