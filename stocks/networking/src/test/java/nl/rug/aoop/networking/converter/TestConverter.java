package nl.rug.aoop.networking.converter;

import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConverter {
    @Test
    public void testFromJson() {
        NetworkMessage networkMessage = Converter.fromJson("{\"header\":\"a\",\"body\":\"b\"}",
                NetworkMessage.class);
        assertEquals(networkMessage.getHeader(), "a");
        assertEquals(networkMessage.getBody(), "b");
    }

    @Test
    public void testToJson() {
        NetworkMessage networkMessage = new NetworkMessage("a", "b");
        assertEquals("{\"header\":\"a\",\"body\":\"b\"}", Converter.toJson(networkMessage));
    }
}
