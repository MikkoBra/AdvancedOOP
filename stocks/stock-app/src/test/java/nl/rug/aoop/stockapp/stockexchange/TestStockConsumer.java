package nl.rug.aoop.stockapp.stockexchange;

import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.NetworkOrderedQueue;
import nl.rug.aoop.stockexchange.StockConsumer;
import nl.rug.aoop.stockexchange.StockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStockConsumer {
    StockExchange stockExchange;
    CommandHandler mockCommandHandler;
    StockConsumer consumer;
    NetworkOrderedQueue queue;

    @BeforeEach
    public void setUp() {
        stockExchange = Mockito.mock(StockExchange.class);
        mockCommandHandler = Mockito.mock(CommandHandler.class);
        queue = new NetworkOrderedQueue();
        consumer = new StockConsumer(stockExchange, mockCommandHandler, queue);
    }

    @Test
    public void testStockExchangeManagerConstructor() {
        assertEquals(stockExchange, consumer.getStockExchange());
    }

    public QueueMessage setUpMessage(String command, String info) {
        QueueMessage message = Mockito.mock(QueueMessage.class);
        Mockito.when(message.getHeader()).thenReturn(command);
        Mockito.when(message.getBody()).thenReturn(info);
        return message;
    }

    @Test
    public void testPoll() {
        NetworkOrderedQueue mockQueue = Mockito.mock(NetworkOrderedQueue.class);
        StockConsumer mockConsumer = new StockConsumer(stockExchange, mockCommandHandler, mockQueue);
        mockConsumer.poll();
        Mockito.verify(mockQueue).dequeue();
    }

    @Test
    public void testUpdateBuy() {
        String jsonString = "{\"NetworkOrder\":{\"symbol\":\"CMP\",\"amount\":20,\"price\"50,\"traderId\":\"bob\"," +
                "\"type\":LimitOrder.BUY}}";
        Map<String, Object> params = new HashMap<>();
        params.put("header", "buy");
        params.put("body", jsonString);
        queue.enqueue(setUpMessage("buy", jsonString));
        Mockito.verify(mockCommandHandler).executeCommand("buy", params);
    }

    @Test
    public void testUpdateSell() {
        String jsonString = "{\"NetworkOrder\":{\"symbol\":\"CMP\",\"amount\":20,\"price\"50,\"traderId\":\"bob\"," +
                "\"type\":LimitOrder.SELL}}";
        Map<String, Object> params = new HashMap<>();
        params.put("header", "sell");
        params.put("body", jsonString);
        queue.enqueue(setUpMessage("sell", jsonString));
        Mockito.verify(mockCommandHandler).executeCommand("sell", params);
    }
}
