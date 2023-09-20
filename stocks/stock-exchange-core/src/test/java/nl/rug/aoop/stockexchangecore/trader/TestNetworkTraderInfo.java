package nl.rug.aoop.stockexchangecore.trader;

import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNetworkTraderInfo {

    private NetworkTraderInfo trader;
    private Map<String, Integer> stockPortfolio;
    private Map<Long, NetworkOrder> transactionHistory;
    private NetworkOrder mockOrder;

    @BeforeEach
    public void setUp() {
        stockPortfolio = new HashMap<>();
        stockPortfolio.put("CMP", 30);
        stockPortfolio.put("CRP", 10);
        transactionHistory = new HashMap<>();
        mockOrder = Mockito.mock(NetworkOrder.class);
        transactionHistory.put(1L, mockOrder);
        trader = new NetworkTraderInfo("bob", "Bob Bob", 200, stockPortfolio, transactionHistory);
    }

    @Test
    public void testNetworkTraderInfoConstructor() {
        assertEquals("bob", trader.getId());
        assertEquals("Bob Bob", trader.getName());
        assertEquals(200, trader.getFunds());
        assertEquals(stockPortfolio, trader.getStockPortfolio());
        assertEquals(transactionHistory, trader.getTransactionHistory());
    }

    @Test
    public void testUpdate() {
        Map<String, Integer> tempPortfolio = new HashMap<>(stockPortfolio);
        tempPortfolio.remove("CRP");
        trader.update("john", "John John", 100, tempPortfolio);
        assertEquals("john", trader.getId());
        assertEquals("John John", trader.getName());
        assertEquals(100, trader.getFunds());
        assertEquals(tempPortfolio, trader.getStockPortfolio());
    }
}
