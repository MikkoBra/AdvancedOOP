package nl.rug.aoop.stockapp.orderresolver;

import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.orderresolver.SellLimitOrderResolver;
import nl.rug.aoop.stockexchange.StockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TestSellLimitOrderResolver {
    private StockExchange stockExchange;
    private StockContainer mockStocks;
    private TraderContainer mockTraders;
    private SellLimitOrderResolver orderResolver;
    private LimitOrder buyOrder;
    private LimitOrder sellOrder;

    @BeforeEach
    public void setUp() {
        mockStocks = Mockito.mock(StockContainer.class);
        mockTraders = Mockito.mock(TraderContainer.class);
        stockExchange = new StockExchange(mockStocks, mockTraders);
        orderResolver = new SellLimitOrderResolver(stockExchange);
        buyOrder = new LimitOrder("CMP", 20, 50, "bob", LimitOrder.Type.BUY);
        sellOrder = new LimitOrder("CMP", 20, 40, "john", LimitOrder.Type.SELL);
    }

    @Test
    public void testSellLimitOrderResolverConstructor() {
        assertEquals(stockExchange, orderResolver.getStockExchange());
    }

    @Test
    public void testMatchOrder() {
        stockExchange.addOrder(buyOrder);
        assertEquals(orderResolver.matchOrder(sellOrder), buyOrder);
    }

    @Test
    public void testMatchOrderNoEntry() {
        assertNull(orderResolver.matchOrder(sellOrder));
    }

    @Test
    public void testMatchOrderNoMatch() {
        buyOrder = new LimitOrder("CMP", 20, 30, "john", LimitOrder.Type.BUY);
        stockExchange.addOrder(buyOrder);
        assertNull(orderResolver.matchOrder(sellOrder));
    }

    @Test
    public void testUpdateOrderMoreOnSale() {
        buyOrder = new LimitOrder("CMP", 30, 50, "john", LimitOrder.Type.BUY);
        stockExchange.addOrder(buyOrder);
        orderResolver.updateOrder(sellOrder, buyOrder, 20);
        assertEquals(10, buyOrder.getAmount());
        assertEquals(0, sellOrder.getAmount());
        assertTrue(orderResolver.getStockExchange().getBuyOrderMap().get("CMP").contains(buyOrder));
    }

    @Test
    public void testUpdateOrderLessOnSale() {
        buyOrder = new LimitOrder("CMP", 10, 50, "john", LimitOrder.Type.BUY);
        stockExchange.addOrder(buyOrder);
        orderResolver.updateOrder(sellOrder, buyOrder, 10);
        assertEquals(0, buyOrder.getAmount());
        assertEquals(10, sellOrder.getAmount());
        assertNull(orderResolver.getStockExchange().getBuyOrderMap().get("CMP"));
    }

    @Test
    public void testUpdateOrderEqualAmount() {
        stockExchange.addOrder(buyOrder);
        orderResolver.updateOrder(sellOrder, buyOrder, 20);
        assertEquals(0, buyOrder.getAmount());
        assertEquals(0, sellOrder.getAmount());
        assertNull(orderResolver.getStockExchange().getBuyOrderMap().get("CMP"));
    }

    @Test
    public void testResolveOrderNoMatch() {
        orderResolver.resolveOrder(sellOrder);
        assertTrue(orderResolver.getStockExchange().getSellOrderMap().get("CMP").contains(sellOrder));
    }

    @Test
    public void testResolveOrder() {
        stockExchange.addOrder(buyOrder);
        orderResolver.resolveOrder(sellOrder);
        Mockito.verify(mockStocks).update(buyOrder);
        Mockito.verify(mockTraders).update(sellOrder, 20, 50);
        Mockito.verify(mockTraders).update(buyOrder, 20, 50);
        assertNull(orderResolver.getStockExchange().getBuyOrderMap().get("CMP"));
        assertNull(orderResolver.getStockExchange().getSellOrderMap().get("CMP"));
    }

    @Test
    public void testResolveOrderMultipleMatches() {
        stockExchange.addOrder(buyOrder);
        stockExchange.addOrder(new LimitOrder("CMP", 20, 41, "john", LimitOrder.Type.BUY));
        stockExchange.addOrder(new LimitOrder("CMP", 20, 42, "john", LimitOrder.Type.BUY));
        LimitOrder bestOrder = new LimitOrder("CMP", 20, 52, "john", LimitOrder.Type.BUY);
        stockExchange.addOrder(bestOrder);
        orderResolver.resolveOrder(sellOrder);
        Mockito.verify(mockStocks).update(bestOrder);
        Mockito.verify(mockTraders).update(bestOrder, 20, 52);
        assertFalse(orderResolver.getStockExchange().getBuyOrderMap().get("CMP").contains(bestOrder));
    }

    @Test
    public void testResolveMultipleTransactions() {
        stockExchange.addOrder(new LimitOrder("CMP", 10, 51, "john", LimitOrder.Type.BUY));
        stockExchange.addOrder(new LimitOrder("CMP", 5, 52, "john", LimitOrder.Type.BUY));
        orderResolver.resolveOrder(sellOrder);
        assertNull(orderResolver.getStockExchange().getBuyOrderMap().get("CMP"));
        assertTrue(orderResolver.getStockExchange().getSellOrderMap().get("CMP").contains(sellOrder));
        assertEquals(5, sellOrder.getAmount());
    }
}
