package nl.rug.aoop.stockapp.stockexchange;

import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import nl.rug.aoop.stockexchange.ConnectionManager;
import nl.rug.aoop.stockexchange.StockExchange;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import nl.rug.aoop.trader.TraderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

public class TestConnectionManager {

    private ConnectionManager manager;
    private TraderContainer traderContainer;
    private StockContainer stockContainer;
    private StockExchange exchange;

    private TraderInfo mockInfo;
    private NetworkTraderInfo networkTraderInfo;
    private Communicator mockCommunicator;
    private String traderJsonMessage;
    private String stockJsonMessage;

    @BeforeEach
    public void setUp() {
        traderContainer = Mockito.mock(TraderContainer.class);
        stockContainer = Mockito.mock(StockContainer.class);
        exchange = new StockExchange(stockContainer, traderContainer);
        manager = new ConnectionManager(exchange);
        mockInfo = Mockito.mock(TraderInfo.class);
        mockCommunicator = Mockito.mock(Communicator.class);
    }

    @Test
    public void testConnectTrader() {
        Communicator mockCommunicator = Mockito.mock(Communicator.class);
        Mockito.when(traderContainer.hasEntry("john")).thenReturn(true);
        manager.connectTrader("john", mockCommunicator);
        assertEquals(mockCommunicator, manager.getConnectedTraders().get("john"));
    }

    @Test
    public void testConnectUnregisteredTrader() {
        Mockito.when(traderContainer.hasEntry("john")).thenReturn(false);
        Communicator mockCommunicator = Mockito.mock(Communicator.class);
        assertThrows(IllegalArgumentException.class, () ->
                manager.connectTrader("john", mockCommunicator));
    }

    @Test
    public void testDisconnectTrader() {
        Communicator mockCommunicator = Mockito.mock(Communicator.class);
        Mockito.when(traderContainer.hasEntry("john")).thenReturn(true);
        manager.connectTrader("john", mockCommunicator);
        assertTrue(manager.getConnectedTraders().containsKey("john"));
        manager.disconnectTrader("john");
        assertFalse(manager.getConnectedTraders().containsKey("john"));
    }

    @Test
    public void testDisconnectUnregisteredTrader() {
        assertThrows(IllegalArgumentException.class, () -> manager.disconnectTrader("john"));
    }

    public void runTestSetup() {
        Mockito.when(traderContainer.getInfo(anyString())).thenReturn(mockInfo);
        Map<String, Integer> portfolio = new HashMap<>();
        Map<Long, NetworkOrder> transactionHistory = new HashMap<>();
        portfolio.put("CMP", 5);
        networkTraderInfo = new NetworkTraderInfo("john", "john john", 200, portfolio,
                transactionHistory);
        Mockito.when(mockInfo.toNetworkTraderInfo()).thenReturn(networkTraderInfo);
        traderJsonMessage = Converter.toJson(new NetworkMessage("updateTraderInfo",
                Converter.toJson(networkTraderInfo)));

        NetworkStock stock = new NetworkStock("CMP", "company", 200, 20, 300);
        Map<String, NetworkStock> stocks = new HashMap<>();
        stocks.put("CMP", stock);
        NetworkStockContainer netStocks = new NetworkStockContainer(stocks);
        Mockito.when(stockContainer.toNetworkStockContainer()).thenReturn(netStocks);
        stockJsonMessage = Converter.toJson(new NetworkMessage("updateStockContainer",
                Converter.toJson(netStocks)));

        Mockito.when(traderContainer.hasEntry("john")).thenReturn(true);
        manager.connectTrader("john", mockCommunicator);
    }

    @Test
    public void testSendMessages() {
        runTestSetup();
        manager.sendMessages(mockCommunicator, mockInfo);
        Mockito.verify(mockCommunicator).sendMessage(traderJsonMessage);
        Mockito.verify(mockCommunicator).sendMessage(stockJsonMessage);
    }

    @Test
    public void testRun() throws InterruptedException {
        runTestSetup();
        new Thread(manager).start();
        await().atMost(1, TimeUnit.SECONDS).until(() -> manager.isRunning());
        Thread.sleep(4000);
        Mockito.verify(mockCommunicator, Mockito.atLeast(4)).sendMessage(traderJsonMessage);
        Mockito.verify(mockCommunicator, Mockito.atMost(6)).sendMessage(traderJsonMessage);
        Mockito.verify(mockCommunicator, Mockito.atLeast(4)).sendMessage(stockJsonMessage);
        Mockito.verify(mockCommunicator, Mockito.atMost(6)).sendMessage(stockJsonMessage);
    }

    @Test
    public void testTerminate() {
        runTestSetup();
        new Thread(manager).start();
        await().atMost(1, TimeUnit.SECONDS).until(() -> manager.isRunning());
        assertTrue(manager.isRunning());
        manager.terminate();
        await().atMost(1, TimeUnit.SECONDS).until(() -> !manager.isRunning());
        assertFalse(manager.isRunning());
    }

}
