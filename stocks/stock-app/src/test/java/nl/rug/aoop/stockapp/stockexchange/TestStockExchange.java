package nl.rug.aoop.stockapp.stockexchange;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.containers.TraderContainer;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchange.StockExchange;
import nl.rug.aoop.stocks.Stock;
import nl.rug.aoop.trader.TraderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestStockExchange {

    StockExchange stockExchange;
    StockContainer stockContainer;
    TraderContainer traderContainer;
    LimitOrder buyOrder;
    LimitOrder sellOrder;

    @BeforeEach
    public void setUp() {
        stockContainer = Mockito.mock(StockContainer.class);
        traderContainer = Mockito.mock(TraderContainer.class);
        stockExchange = new StockExchange(stockContainer, traderContainer);

        buyOrder = new LimitOrder("CMP", 3, 50, "bob", LimitOrder.Type.BUY);
        sellOrder = new LimitOrder("CMP", 3, 45, "john", LimitOrder.Type.SELL);
    }

    @Test
    public void testStockExchangeConstructor() {
        assertEquals(stockContainer, stockExchange.getStockContainer());
        assertEquals(traderContainer, stockExchange.getTraderContainer());
        assertEquals(new HashMap<String, List<LimitOrder>>(), stockExchange.getBuyOrderMap());
        assertEquals(new HashMap<String, List<LimitOrder>>(), stockExchange.getSellOrderMap());
    }

    @Test
    public void testMatchBuyMap() {
        LimitOrder mockOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(mockOrder.getType()).thenReturn(LimitOrder.Type.BUY);
        assertEquals(stockExchange.getBuyOrderMap(), stockExchange.matchMap(mockOrder));
    }

    @Test
    public void testMatchSellMap() {
        LimitOrder mockOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(mockOrder.getType()).thenReturn(LimitOrder.Type.SELL);
        assertEquals(stockExchange.getSellOrderMap(), stockExchange.matchMap(mockOrder));
    }

    @Test
    public void testMatchUnknownMap() {
        LimitOrder mockOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(mockOrder.getType()).thenReturn(LimitOrder.Type.UNDEFINED);
        assertNull(stockExchange.matchMap(mockOrder));
    }

    @Test
    public void testAddBuyOrder() {
        stockExchange.addOrder(buyOrder);
        assertTrue(stockExchange.getBuyOrderMap().containsKey("CMP"));
        List<LimitOrder> list = stockExchange.getBuyOrderMap().get("CMP");
        assertTrue(list.contains(buyOrder));
    }

    @Test
    public void testAddSellOrder() {
        stockExchange.addOrder(sellOrder);
        assertTrue(stockExchange.getSellOrderMap().containsKey("CMP"));
        List<LimitOrder> list = stockExchange.getSellOrderMap().get("CMP");
        assertTrue(list.contains(sellOrder));
    }

    @Test
    public void testAddEmptyOrder() {
        LimitOrder emptyOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(emptyOrder.getAmount()).thenReturn(0);
        Mockito.when(emptyOrder.getType()).thenReturn(LimitOrder.Type.SELL);
        Mockito.when(emptyOrder.getSymbol()).thenReturn("CMP");
        stockExchange.addOrder(emptyOrder);
        assertFalse(stockExchange.getSellOrderMap().containsKey("CMP"));
    }

    @Test
    public void testRemoveOrder() {
        LimitOrder mockOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(mockOrder.getType()).thenReturn(LimitOrder.Type.BUY);
        Mockito.when(mockOrder.getSymbol()).thenReturn("CMP");
        Mockito.when(mockOrder.getAmount()).thenReturn(3);
        stockExchange.addOrder(mockOrder);
        stockExchange.addOrder(buyOrder);
        assertTrue(stockExchange.getBuyOrderMap().get("CMP").contains(buyOrder));
        stockExchange.removeOrder(buyOrder);
        assertFalse(stockExchange.getBuyOrderMap().get("CMP").contains(buyOrder));
    }

    @Test
    public void testRemoveUnregisteredOrder() {
        assertThrows(IllegalArgumentException.class, () -> stockExchange.removeOrder(buyOrder));
    }

    @Test
    public void testRemoveSymbolFromOrders() {
        LimitOrder mockOrder = Mockito.mock(LimitOrder.class);
        Mockito.when(mockOrder.getType()).thenReturn(LimitOrder.Type.BUY);
        Mockito.when(mockOrder.getSymbol()).thenReturn("CMP");
        Mockito.when(mockOrder.getAmount()).thenReturn(3);
        stockExchange.addOrder(mockOrder);
        assertTrue(stockExchange.getBuyOrderMap().containsKey("CMP"));
        stockExchange.removeOrder(mockOrder);
        assertNull(stockExchange.getBuyOrderMap().get("CMP"));
    }

    @Test
    public void testUpdateAmount() {
        stockExchange.addOrder(buyOrder);
        stockExchange.updateOrder(buyOrder, 2);
        assertEquals(1, stockExchange.getBuyOrderMap().get("CMP").get(0).getAmount());
    }

    @Test
    public void testUpdateUnregisteredStock() {
        stockExchange.updateOrder(buyOrder, 2);
        assertNull(stockExchange.getBuyOrderMap().get("CMP"));
    }

    @Test
    public void testUpdateIllegalAmount() {
        stockExchange.addOrder(buyOrder);
        stockExchange.updateOrder(buyOrder, -2);
        assertNull(stockExchange.getBuyOrderMap().get("CMP"));
    }

    @Test
    public void testGetStocksByIndex() {
        Stock stock = Mockito.mock(Stock.class);
        Map<String, Stock> stockMap = new HashMap<>();
        stockMap.put("CMP", stock);
        Mockito.when(stockContainer.getStocks()).thenReturn(stockMap);
        assertEquals(stock, stockExchange.getStockByIndex(0));
    }

    @Test
    public void testGetNumberOfStocks() {
        stockExchange.getNumberOfStocks();
        Mockito.verify(stockContainer).getSize();
    }

    @Test
    public void testGetTraderByIndex() {
        TraderInfo trader = Mockito.mock(TraderInfo.class);
        Map<String, TraderInfo> traderMap = new HashMap<>();
        traderMap.put("bob", trader);
        Mockito.when(traderContainer.getTraders()).thenReturn(traderMap);
        assertEquals(trader, stockExchange.getTraderByIndex(0));
    }

    @Test
    public void testGetNumberOfTraders() {
        stockExchange.getNumberOfTraders();
        Mockito.verify(traderContainer).getSize();
    }

}
