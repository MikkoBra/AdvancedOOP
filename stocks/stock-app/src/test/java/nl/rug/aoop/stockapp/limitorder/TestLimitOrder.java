package nl.rug.aoop.stockapp.limitorder;

import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestLimitOrder {

    @Test
    public void testLimitOrderConstructor() {
        LimitOrder limitOrder = new LimitOrder("CMP", 3, 20, "bob", LimitOrder.Type.SELL);
        assertEquals(limitOrder.getSymbol(), "CMP");
        assertEquals(limitOrder.getAmount(), 3);
        assertEquals(limitOrder.getPrice(), 20);
        assertEquals(limitOrder.getTraderId(), "bob");
        assertEquals(limitOrder.getType(), LimitOrder.Type.SELL);
    }

    @Test
    public void testEmptyConstructor() {
        LimitOrder limitOrder = new LimitOrder();
        assertEquals(limitOrder.getSymbol(), "");
        assertEquals(limitOrder.getAmount(), 0);
        assertEquals(limitOrder.getPrice(), 0);
        assertEquals(limitOrder.getTraderId(), "");
        assertEquals(limitOrder.getType(), LimitOrder.Type.UNDEFINED);
    }

    @Test
    public void testSetInformation() {
        NetworkOrder order = new NetworkOrder("CMP", 3, 20, "bob", "sell");
        LimitOrder limitOrder = new LimitOrder();
        limitOrder.setInformation(order);
        assertEquals(limitOrder.getSymbol(), order.getSymbol());
        assertEquals(limitOrder.getAmount(), order.getAmount());
        assertEquals(limitOrder.getPrice(), order.getPrice());
        assertEquals(limitOrder.getTraderId(), order.getTraderId());
        assertEquals(LimitOrder.Type.SELL, limitOrder.getType());
    }

    @Test
    public void testUpdateLowerAmount() {
        LimitOrder limitOrder = new LimitOrder("CMP", 10, 20, "bob", LimitOrder.Type.BUY);
        limitOrder.updateAmount(6);
        assertEquals(4, limitOrder.getAmount());
    }

    @Test
    public void testUpdateHigherAmount() {
        LimitOrder limitOrder = new LimitOrder("CMP", 10, 20, "bob", LimitOrder.Type.BUY);
        limitOrder.updateAmount(15);
        assertEquals(0, limitOrder.getAmount());
    }

    @Test
    public void testUpdateIllegalAmount() {
        LimitOrder limitOrder = new LimitOrder("CMP", 10, 20, "bob", LimitOrder.Type.BUY);
        assertThrows(IllegalArgumentException.class, () -> limitOrder.updateAmount(-3));
    }
}
