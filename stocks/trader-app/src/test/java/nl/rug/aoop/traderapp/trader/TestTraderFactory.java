//package nl.rug.aoop.traderapp.trader;
//
//import nl.rug.aoop.networking.client.Client;
//import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
//import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.concurrent.TimeUnit;
//
//import static org.awaitility.Awaitility.await;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class TestTraderFactory {
//    @Test
//    public void testCreateTraderOnClient() {
//        String id = "id";
//        Client mockClient = Mockito.mock(Client.class);
//        NetworkStockContainer mockStockContainer = Mockito.mock(NetworkStockContainer.class);
//        NetworkTraderInfo mockTraderInfo = Mockito.mock(NetworkTraderInfo.class);
//        Mockito.when(mockClient.isConnected()).thenReturn(true);
//        Mockito.when(mockClient.isRunning()).thenReturn(true);
//        Trader trader = TraderFactory.createTraderOnClient(id, mockTraderInfo, mockStockContainer, mockClient);
//        assertEquals(id, trader.getId());
//    }
//}
