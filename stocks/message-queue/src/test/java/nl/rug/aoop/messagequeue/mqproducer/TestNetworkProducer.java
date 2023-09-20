package nl.rug.aoop.messagequeue.mqproducer;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
public class TestNetworkProducer {
    private Client mockClient;
    private NetworkProducer networkProducer;

    @BeforeEach
    public void setUp() {
        mockClient = Mockito.mock(Client.class);
        Mockito.when(mockClient.isConnected()).thenReturn(true);
        Mockito.when(mockClient.isRunning()).thenReturn(true);
        networkProducer = new NetworkProducer(mockClient);
    }

    @Test
    public void testConstructor() {
        assertEquals(networkProducer.getClient(), mockClient);
    }

    @Test
    public void testPut() {
        QueueMessage testMessage = new QueueMessage("header", "body");
        NetworkMessage testNetworkMessage = new NetworkMessage("MqPut", Converter.toJson(testMessage));
        String jsonMessage = Converter.toJson(testNetworkMessage);

        networkProducer.put(testMessage);
        Mockito.verify(mockClient).sendMessage(jsonMessage);
    }

    @Test
    public void testPutNullMessage() {
        networkProducer.put(null);
        Mockito.verify(mockClient, Mockito.never()).sendMessage(any());
    }
}
