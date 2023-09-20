package nl.rug.aoop.stockapp.trader;

import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import nl.rug.aoop.trader.TraderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTraderInfo {

    private Map<String, Integer> stockMap;
    private TraderInfo trader;

    @BeforeEach
    public void setUp() {
        stockMap = new HashMap<>();
        stockMap.put("CMP", 5);
        trader = new TraderInfo("bob", "Bob Bob", 2000, stockMap);
    }

    @Test
    public void testTraderInfoConstructor() {
        assertEquals("bob", trader.getId());
        assertEquals("Bob Bob", trader.getName());
        assertEquals(2000, trader.getFunds());
        assertEquals(stockMap, trader.getStockPortfolio());
        assertEquals(new HashMap<Long, NetworkOrder>(), trader.getTransactionHistory());
    }

    @Test
    public void testUpdateTraderBuy() {
        LimitOrder order = Mockito.mock(LimitOrder.class);
        Mockito.when(order.getSymbol()).thenReturn("CMP");
        Mockito.when(order.getType()).thenReturn(LimitOrder.Type.BUY);
        trader.updateTrader(order, 3, 30.0);
        NetworkOrder transaction = trader.getTransactionHistory().get(0L);
        assertEquals(LimitOrder.Type.BUY.name(), transaction.getType());
        assertEquals("CMP", transaction.getSymbol());
    }

    @Test
    public void testUpdateTraderSell() {
        LimitOrder order = Mockito.mock(LimitOrder.class);
        String symbol = "CMP";
        Mockito.when(order.getSymbol()).thenReturn(symbol);
        Mockito.when(order.getType()).thenReturn(LimitOrder.Type.SELL);
        int amountTraded = 3;
        double salePrice = 30;
        trader.updateTrader(order, amountTraded, salePrice);
        assertEquals(trader.getFunds(), 2000 + amountTraded * salePrice);
        assertEquals(trader.getStockPortfolio().get("CMP"), 2);
        NetworkOrder transaction = trader.getTransactionHistory().get(0L);
        assertEquals(LimitOrder.Type.SELL.name(), transaction.getType());
        assertEquals("CMP", transaction.getSymbol());
    }

    @Test
    public void testToNetworkTraderInfo() {
        Map<Long, NetworkOrder> transactionHistory = new HashMap<>();
        NetworkTraderInfo netInfo = new NetworkTraderInfo("bob", "Bob Bob", 2000, stockMap,
                transactionHistory);
        assertEquals(netInfo.getFunds(), trader.getFunds());
        assertEquals(netInfo.getName(), trader.getName());
        assertEquals(netInfo.getId(), trader.getId());
        assertEquals(netInfo.getStockPortfolio(), trader.getStockPortfolio());
        assertEquals(netInfo.getTransactionHistory(), transactionHistory);
    }

    @Test
    public void testGetOwnedStocks() {
        List<String> stocklist = new ArrayList<>();
        stocklist.add("CMP");
        assertEquals(stocklist, trader.getOwnedStocks());
    }
}
