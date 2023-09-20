package nl.rug.aoop.stockexchangecore.containers;

import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestNetworkStockContainer {

    NetworkStockContainer stockContainer;
    Map<String, NetworkStock> stockMap;
    NetworkStock testStock;
    NetworkStock cheapStock;

    @BeforeEach
    public void setUp() {
        stockContainer = new NetworkStockContainer();

        testStock = Mockito.mock(NetworkStock.class);
        Mockito.when(testStock.getSymbol()).thenReturn("CMP");
        Mockito.when(testStock.getPrice()).thenReturn(40.0);
        stockMap = new HashMap<>();
        stockMap.put("CMP", testStock);
        cheapStock = Mockito.mock(NetworkStock.class);
        Mockito.when(cheapStock.getPrice()).thenReturn(1.0);
        Mockito.when(cheapStock.getSymbol()).thenReturn("CRP");
        stockMap.put("CRP", cheapStock);
        stockContainer = new NetworkStockContainer(stockMap);
    }

    @Test
    public void testNetworkStockContainerConstructor() {
        assertEquals(stockMap, stockContainer.getStocks());
    }

    @Test
    public void testEmptyConstructor() {
        NetworkStockContainer newContainer = new NetworkStockContainer();
        assertNull(newContainer.getStocks());
    }

    @Test
    public void testGetStocksForPrice() {
        assertEquals(stockContainer.getStocksForPrice(60), stockMap);
        Map<String, NetworkStock> tempStocks = new HashMap<>();
        tempStocks.put("CRP", cheapStock);
        assertEquals(stockContainer.getStocksForPrice(5), tempStocks);
        assertEquals(stockContainer.getStocksForPrice(1), tempStocks);
    }

    @Test
    public void testGetStocksForFree() {
        Map<String, NetworkStock> tempStocks = new HashMap<>();
        assertEquals(stockContainer.getStocksForPrice(0), tempStocks);
    }

    @Test
    public void testUpdate() {
        assertEquals(stockMap, stockContainer.getStocks());
        Map<String, NetworkStock> tempStocks = new HashMap<>();
        stockContainer.update(tempStocks);
        assertEquals(tempStocks, stockContainer.getStocks());
    }
}
