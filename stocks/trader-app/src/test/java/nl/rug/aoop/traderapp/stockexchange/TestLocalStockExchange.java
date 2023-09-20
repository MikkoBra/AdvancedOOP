package nl.rug.aoop.traderapp.stockexchange;

import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestLocalStockExchange {

    private NetworkStockContainer container;
    private NetworkTraderInfo trader;
    private LocalStockExchange exchange;

    @BeforeEach
    public void setUp() {
        container = Mockito.mock(NetworkStockContainer.class);
        trader = Mockito.mock(NetworkTraderInfo.class);
        exchange = new LocalStockExchange(trader, container);
    }

    @Test
    public void testLocalStockExchangeConstructor() {
        assertEquals(container, exchange.getStockContainer());
        assertEquals(trader, exchange.getTraderInfo());
        assertEquals(0, exchange.getWithHeldFunds());
        assertEquals(new HashMap<String, Integer>(), exchange.getAwaitingSellOrders());
    }

    @Test
    public void testUpdateAwaitingSellOrders() {
        exchange.updateAwaitingSellOrders("CMP", 3);
        assertEquals(3, exchange.getAwaitingSellOrders().get("CMP"));
        exchange.updateAwaitingSellOrders("CMP", -2);
        assertEquals(1, exchange.getAwaitingSellOrders().get("CMP"));
    }

    @Test
    public void testUpdateTraderInfo() {
        Map<Long, NetworkOrder> transactionHistory = new HashMap<>();
        NetworkOrder mockOrder = Mockito.mock(NetworkOrder.class);
        Mockito.when(mockOrder.getSymbol()).thenReturn("CMP");
        Mockito.when(mockOrder.getAmount()).thenReturn(5);
        Mockito.when(mockOrder.getPrice()).thenReturn(100.0);
        Mockito.when(mockOrder.getType()).thenReturn("SELL");
        transactionHistory.put(1L, mockOrder);
        NetworkTraderInfo trader = Mockito.mock(NetworkTraderInfo.class);
        Mockito.when(trader.getTransactionHistory()).thenReturn(transactionHistory);
        exchange.updateAwaitingSellOrders("CMP", 6);
        exchange.updateTraderInfo(trader);
        assertEquals(1, exchange.getAwaitingSellOrders().get("CMP"));
        assertEquals(trader, exchange.getTraderInfo());
    }

    @Test
    public void testUpdateStockContainer() {
        NetworkStockContainer newContainer = Mockito.mock(NetworkStockContainer.class);
        exchange.updateStockContainer(newContainer);
        assertEquals(newContainer, exchange.getStockContainer());
    }

    @Test
    public void updateWithheldFunds() {
        exchange.updateWithHeldFunds(3, 20);
        assertEquals(60, exchange.getWithHeldFunds());
    }

    @Test
    public void testGetFunds() {
        Mockito.when(trader.getFunds()).thenReturn(30.0);
        exchange.updateWithHeldFunds(3, 5);
        assertEquals(15, exchange.getFunds());
    }

    @Test
    public void testGetPortfolio() {
        Map<String, Integer> traderPortfolio = new HashMap<>();
        traderPortfolio.put("CMP", 3);
        Mockito.when(trader.getStockPortfolio()).thenReturn(traderPortfolio);
        exchange.updateAwaitingSellOrders("CMP", 1);
        Map<String, Integer> exchangePortfolio = new HashMap<>();
        exchangePortfolio.put("CMP", 2);
        assertEquals(exchangePortfolio, exchange.getPortfolio());
    }

    @Test
    public void testHasNotReceivedInfo() {
        LocalStockExchange emptyExchange = new LocalStockExchange(null, null);
        assertFalse(emptyExchange.hasReceivedInfo());
    }

    @Test
    public void testHasReceivedInfo() {
        LocalStockExchange emptyExchange = new LocalStockExchange(null, null);
        emptyExchange.updateTraderInfo(trader);
        emptyExchange.updateStockContainer(container);
        assertTrue(emptyExchange.hasReceivedInfo());
    }

}
