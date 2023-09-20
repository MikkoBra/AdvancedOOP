package nl.rug.aoop.stockapp.orderresolver;

import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.orderresolver.BuyLimitOrderResolver;
import nl.rug.aoop.stockexchange.StockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TestBuyLimitOrderResolver {
    private StockExchange stockExchange;
    private StockContainer mockStocks;
    private TraderContainer mockTraders;
    private BuyLimitOrderResolver orderResolver;
    private LimitOrder buyOrder;
    private LimitOrder sellOrder;

    @BeforeEach
    public void setUp() {
        mockStocks = Mockito.mock(StockContainer.class);
        mockTraders = Mockito.mock(TraderContainer.class);
        stockExchange = new StockExchange(mockStocks, mockTraders);
        orderResolver = new BuyLimitOrderResolver(stockExchange);
        buyOrder = new LimitOrder("CMP", 20, 50, "bob", LimitOrder.Type.BUY);
        sellOrder = new LimitOrder("CMP", 20, 40, "john", LimitOrder.Type.SELL);
    }

    @Test
    public void testBuyLimitOrderResolverConstructor() {
        assertEquals(stockExchange, orderResolver.getStockExchange());
    }

    @Test
    public void testMatchOrder() {
        stockExchange.addOrder(sellOrder);
        assertEquals(orderResolver.matchOrder(buyOrder), sellOrder);
    }

    @Test
    public void testMatchOrderNoEntry() {
        assertNull(orderResolver.matchOrder(buyOrder));
    }

    @Test
    public void testMatchOrderNoMatch() {
        sellOrder = new LimitOrder("CMP", 20, 60, "john", LimitOrder.Type.SELL);
        stockExchange.addOrder(sellOrder);
        assertNull(orderResolver.matchOrder(buyOrder));
    }

    @Test
    public void testUpdateOrderMoreOnSale() {
        sellOrder = new LimitOrder("CMP", 30, 40, "john", LimitOrder.Type.SELL);
        stockExchange.addOrder(sellOrder);
        orderResolver.updateOrder(buyOrder, sellOrder, 20);
        assertEquals(0, buyOrder.getAmount());
        assertEquals(10, sellOrder.getAmount());
        assertTrue(orderResolver.getStockExchange().getSellOrderMap().get("CMP").contains(sellOrder));
    }

    @Test
    public void testUpdateOrderLessOnSale() {
        sellOrder = new LimitOrder("CMP", 10, 40, "john", LimitOrder.Type.SELL);
        stockExchange.addOrder(sellOrder);
        orderResolver.updateOrder(buyOrder, sellOrder, 10);
        assertEquals(10, buyOrder.getAmount());
        assertEquals(0, sellOrder.getAmount());
        assertNull(orderResolver.getStockExchange().getSellOrderMap().get("CMP"));
    }

    @Test
    public void testUpdateOrderEqualAmount() {
        stockExchange.addOrder(sellOrder);
        orderResolver.updateOrder(buyOrder, sellOrder, 20);
        assertEquals(0, buyOrder.getAmount());
        assertEquals(0, sellOrder.getAmount());
        assertNull(orderResolver.getStockExchange().getSellOrderMap().get("CMP"));
    }

    @Test
    public void testResolveOrderNoMatch() {
        orderResolver.resolveOrder(buyOrder);
        assertTrue(orderResolver.getStockExchange().getBuyOrderMap().get("CMP").contains(buyOrder));
    }

    @Test
    public void testResolveOrder() {
        stockExchange.addOrder(sellOrder);
        orderResolver.resolveOrder(buyOrder);
        Mockito.verify(mockStocks).update(sellOrder);
        Mockito.verify(mockTraders).update(buyOrder, 20, 40);
        Mockito.verify(mockTraders).update(sellOrder, 20, 40);
        assertNull(orderResolver.getStockExchange().getBuyOrderMap().get("CMP"));
        assertNull(orderResolver.getStockExchange().getSellOrderMap().get("CMP"));
    }

    @Test
    public void testResolveOrderMultipleMatches() {
        stockExchange.addOrder(sellOrder);
        stockExchange.addOrder(new LimitOrder("CMP", 20, 41, "john", LimitOrder.Type.SELL));
        stockExchange.addOrder(new LimitOrder("CMP", 20, 42, "john", LimitOrder.Type.SELL));
        LimitOrder bestOrder = new LimitOrder("CMP", 20, 39, "john", LimitOrder.Type.SELL);
        stockExchange.addOrder(bestOrder);
        orderResolver.resolveOrder(buyOrder);
        Mockito.verify(mockStocks).update(bestOrder);
        Mockito.verify(mockTraders).update(bestOrder, 20, 39);
        assertFalse(orderResolver.getStockExchange().getSellOrderMap().get("CMP").contains(bestOrder));
    }

    @Test
    public void testResolveMultipleTransactions() {
        stockExchange.addOrder(new LimitOrder("CMP", 10, 41, "john", LimitOrder.Type.SELL));
        stockExchange.addOrder(new LimitOrder("CMP", 5, 42, "john", LimitOrder.Type.SELL));
        orderResolver.resolveOrder(buyOrder);
        assertNull(orderResolver.getStockExchange().getSellOrderMap().get("CMP"));
        assertTrue(orderResolver.getStockExchange().getBuyOrderMap().get("CMP").contains(buyOrder));
        assertEquals(5, buyOrder.getAmount());
    }
}
