package nl.rug.aoop.traderapp.stockinteractor;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.mqproducer.NetworkProducer;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.traderapp.registrator.Registrator;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestStockInteractor {
    private NetworkProducer mockNetworkProducer;
    private Registrator mockRegistrator;
    private LocalStockExchange mockExchange;
    private StockInteractor stockInteractor;
    private ArgumentCaptor<NetworkMessage> NetworkMessageCaptor;

    @BeforeEach
    public void setUp() {
        mockNetworkProducer = Mockito.mock(NetworkProducer.class);
        mockRegistrator = Mockito.mock(Registrator.class);
        mockExchange = Mockito.mock(LocalStockExchange.class);
        stockInteractor = new StockInteractor(mockExchange, mockNetworkProducer, mockRegistrator);
        NetworkMessageCaptor = ArgumentCaptor.forClass(NetworkMessage.class);
    }

    @Test
    public void testSendSellOrder() throws IOException {
        NetworkOrder order = Mockito.mock(NetworkOrder.class);
        Mockito.when(order.getType()).thenReturn("sell");
        Mockito.when(order.getAmount()).thenReturn(2);
        Mockito.when(order.getSymbol()).thenReturn("CMP");
        String convertedOrder = "order";
        try (MockedStatic<Converter> mockFromJson = Mockito.mockStatic(Converter.class)) {
            mockFromJson.when(() -> Converter.toJson(order))
                    .thenReturn(convertedOrder);

            ArgumentCaptor<QueueMessage> messageCaptor = ArgumentCaptor.forClass(QueueMessage.class);
            stockInteractor.sendOrder(order);
            Mockito.verify(mockExchange).updateAwaitingSellOrders("CMP", 2);
            Mockito.verify(mockNetworkProducer).put(messageCaptor.capture());
            assertEquals("sell", messageCaptor.getValue().getHeader());
            assertEquals(convertedOrder, messageCaptor.getValue().getBody());
        }
    }

    @Test
    public void testSendBuyOrder() throws IOException {
        NetworkOrder order = Mockito.mock(NetworkOrder.class);
        Mockito.when(order.getType()).thenReturn("buy");
        Mockito.when(order.getAmount()).thenReturn(2);
        Mockito.when(order.getPrice()).thenReturn(10.0);
        String convertedOrder = "order";
        try (MockedStatic<Converter> mockFromJson = Mockito.mockStatic(Converter.class)) {
            mockFromJson.when(() -> Converter.toJson(order))
                    .thenReturn(convertedOrder);

            ArgumentCaptor<QueueMessage> messageCaptor = ArgumentCaptor.forClass(QueueMessage.class);
            stockInteractor.sendOrder(order);
            Mockito.verify(mockExchange).updateWithHeldFunds(2, 10);
            Mockito.verify(mockNetworkProducer).put(messageCaptor.capture());
            assertEquals("buy", messageCaptor.getValue().getHeader());
            assertEquals(convertedOrder, messageCaptor.getValue().getBody());
        }
    }

    @Test
    public void testSendOrderNull() {
        assertThrows(IOException.class, () -> stockInteractor.sendOrder(null));
    }

    @Test
    public void testRegister() {
        String testId = "testId";
        stockInteractor.register(testId);
        Mockito.verify(mockRegistrator).sendRegistration(NetworkMessageCaptor.capture());
        assertEquals("register", NetworkMessageCaptor.getValue().getHeader());
        assertEquals(testId, NetworkMessageCaptor.getValue().getBody());
    }

    @Test
    public void testDeregister() {
        String testId = "testId";
        stockInteractor.unregister(testId);
        Mockito.verify(mockRegistrator).sendRegistration(NetworkMessageCaptor.capture());
        assertEquals("unregister", NetworkMessageCaptor.getValue().getHeader());
        assertEquals(testId, NetworkMessageCaptor.getValue().getBody());
    }

    @Test
    public void testIsRegistered() {
        stockInteractor.isRegistered();
        Mockito.verify(mockExchange).hasReceivedInfo();
    }

    @Test
    public void testGetFunds() {
        stockInteractor.getFunds();
        Mockito.verify(mockExchange).getFunds();
    }

    @Test
    public void testGetPortfolio() {
        stockInteractor.getPortfolio();
        Mockito.verify(mockExchange).getPortfolio();
    }

    @Test
    public void testGetStockContainer() {
        stockInteractor.getStockContainer();
        Mockito.verify(mockExchange).getStockContainer();
    }
}