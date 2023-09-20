package nl.rug.aoop.stockapp.commands;

import nl.rug.aoop.commands.LimitOrderCommand;
import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.orderresolver.BuyLimitOrderResolver;
import nl.rug.aoop.orderresolver.OrderResolver;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class TestLimitOrderCommand {

    LimitOrderCommand orderCommand;
    private OrderResolver orderResolver;
    private LimitOrder mockLimitOrder;

    @BeforeEach
    public void setUp() {
        orderResolver = Mockito.mock(BuyLimitOrderResolver.class);
        orderCommand = new LimitOrderCommand(orderResolver);
        mockLimitOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(mockLimitOrder.getType()).thenReturn(LimitOrder.Type.BUY);
    }

    @Test
    public void testLimitOrderCommandConstructor() {
        assertEquals(orderCommand.getOrderResolver(), orderResolver);
    }

    @Test
    public void testExecute() throws IOException {
        Map<String, Object> params = new HashMap<>();
        String networkOrder = Converter.toJson(new NetworkOrder("CMP", 20, 30, "bob",
                "buy"));
        params.put("body", networkOrder);
        orderCommand.execute(params);
        Mockito.verify(orderResolver).resolveOrder(any(LimitOrder.class));
    }

    @Test
    public void testExecuteWrongParamName() {
        Map<String, Object> params = new HashMap<>();
        assertThrows(IOException.class, () -> orderCommand.execute(params));
    }

    @Test
    public void testExecuteWrongObject() {
        Map<String, Object> params = new HashMap<>();
        StockContainer stockContainer = Mockito.mock(StockContainer.class);
        params.put("body", stockContainer);
        assertThrows(IOException.class, () -> orderCommand.execute(params));
    }
}
