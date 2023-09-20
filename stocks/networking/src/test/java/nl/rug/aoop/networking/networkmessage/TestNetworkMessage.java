package nl.rug.aoop.networking.networkmessage;

import nl.rug.aoop.networking.converter.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNetworkMessage {

    private String header;
    private String body;
    private NetworkMessage networkMessage;
    private NetworkMessage defaultMessage;

    @BeforeEach
    public void setUp() {
        header = "mqput";
        body = "message json";
        networkMessage = new NetworkMessage(header, body);
        defaultMessage = new NetworkMessage();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals("", defaultMessage.getHeader());
        assertEquals("", defaultMessage.getBody());
    }

    @Test
    public void testConstructor() {
        NetworkMessage networkMessage = new NetworkMessage(header, body);
        assertEquals(header, networkMessage.getHeader());
        assertEquals(body, networkMessage.getBody());
    }
}
