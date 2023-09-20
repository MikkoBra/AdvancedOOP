package nl.rug.aoop.traderapp.trader;

import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.client.ClientFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class TestTraderManager {
    private final InetSocketAddress address = new InetSocketAddress("localhost", 40404);
    private final int numTraders = 4;
    private final TraderManager traderManager = new TraderManager(address, numTraders);
    private static Client mockClient;

    @BeforeAll
    public static void init() {
        mockClient = Mockito.mock(Client.class);
        Mockito.when(mockClient.isRunning()).thenReturn(true);
        Mockito.when(mockClient.isConnected()).thenReturn(true);
    }

    @Test
    public void testConstructor() {
        assertEquals(address, traderManager.getAddress());
    }

    @Test
    public void testSetUpTraders() {
        int initialTraderListSize = traderManager.getTraderList().size();
        int initialClientListSize = traderManager.getClientList().size();
        int numTraders = 4;
        try (MockedStatic<ClientFactory> mockTraderFactory = Mockito.mockStatic(ClientFactory.class)) {
            mockTraderFactory.when(() -> ClientFactory.createClientWithFactory(any(), any())).thenReturn(mockClient);
            traderManager.setUpTraders();
            assertEquals(traderManager.getTraderList().size(), initialTraderListSize + numTraders);
            assertEquals(traderManager.getClientList().size(), initialClientListSize + numTraders);
        }
    }

    @Test
    public void testShutdownTraders() {
        int numTraders = 4;
        try (MockedStatic<ClientFactory> mockTraderFactory = Mockito.mockStatic(ClientFactory.class)) {
            mockTraderFactory.when(() -> ClientFactory.createClientWithFactory(any(), any())).thenReturn(mockClient);
            traderManager.setUpTraders();
            assertEquals(numTraders, traderManager.getTraderList().size());
            assertEquals(numTraders, traderManager.getClientList().size());
            traderManager.shutdownTraders();
            await().atMost(1, TimeUnit.SECONDS).until(() -> traderManager.getTraderList().size() == 0);
            assertEquals(0, traderManager.getTraderList().size());
            assertEquals(0, traderManager.getClientList().size());
        }
    }
}
