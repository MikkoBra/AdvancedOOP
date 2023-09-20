package nl.rug.aoop.stockexchangecore.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNetworkOrder {

    @Test
    public void testNetworkOrderConstructor() {
        NetworkOrder order = new NetworkOrder("CMP", 20, 40.0, "bob", "buy");
        assertEquals("CMP", order.getSymbol());
        assertEquals(20, order.getAmount());
        assertEquals(40.0, order.getPrice());
        assertEquals("bob", order.getTraderId());
        assertEquals("buy", order.getType());
    }
}
