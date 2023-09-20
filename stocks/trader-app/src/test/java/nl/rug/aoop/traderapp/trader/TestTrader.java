package nl.rug.aoop.traderapp.trader;

import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.traderapp.stockinteractor.StockInteractor;
import nl.rug.aoop.traderapp.trader.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

public class TestTrader {
    private String id;
    private NetworkStockContainer mockStockContainer;
    private StockInteractor mockInteractor;
    private Strategy mockStrategy;
    private Trader trader;

    @BeforeEach
    public void setUp() {
        id = "testId";
        mockStockContainer = Mockito.mock(NetworkStockContainer.class);
        mockInteractor = Mockito.mock(StockInteractor.class);
        Mockito.when(mockInteractor.isRegistered()).thenReturn(true);
        mockStrategy = Mockito.mock(Strategy.class);
        trader = new Trader(id, mockStrategy, mockInteractor);
    }


    @Test
    public void testConstructor() {
        assertEquals(trader.getId(), id);
        assertEquals(trader.getStockInteractor(), mockInteractor);
        assertEquals(trader.getStrategy(), mockStrategy);
    }

    @Test
    public void testTerminate() {
        new Thread(trader).start();
        await().atMost(1, TimeUnit.SECONDS).until(() -> trader.isRunning());
        assertTrue(trader.isRunning());
        trader.terminate();
        await().atMost(1, TimeUnit.SECONDS).until(() -> !trader.isRunning());
        assertFalse(trader.isRunning());
        Mockito.verify(mockInteractor).unregister(id);
    }

    @Test
    public void testRun() {
        Mockito.when(mockInteractor.isRegistered()).thenReturn(true);
        Mockito.when(mockInteractor.getFunds()).thenReturn(20.0);
        Mockito.when(mockInteractor.getPortfolio()).thenReturn(new HashMap<>());
        Mockito.when(mockInteractor.getStockContainer()).thenReturn(mockStockContainer);
        NetworkOrder mockOrder = Mockito.mock(NetworkOrder.class);
        Mockito.when(mockStrategy.execute(id, mockInteractor.getFunds(), mockInteractor.getPortfolio(),
                mockInteractor.getStockContainer())).thenReturn(mockOrder);
        new Thread(trader).start();
        await().atMost(5, TimeUnit.SECONDS).until(() -> trader.isRunning());
        assertTrue(trader.isRunning());
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                Mockito.verify(mockInteractor, Mockito.atLeast(1)).sendOrder(mockOrder));
        trader.terminate();
    }
}
