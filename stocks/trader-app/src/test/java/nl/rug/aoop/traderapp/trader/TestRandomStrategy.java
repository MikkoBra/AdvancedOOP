package nl.rug.aoop.traderapp.trader;

import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;
import nl.rug.aoop.stockexchangecore.trader.NetworkTraderInfo;
import nl.rug.aoop.traderapp.trader.strategy.RandomStrategy;
import nl.rug.aoop.traderapp.trader.strategy.Strategy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestRandomStrategy {

    @Test
    public void testExecuteStrategy() {
        NetworkTraderInfo mockTraderInfo = Mockito.mock(NetworkTraderInfo.class);
        NetworkStockContainer mockStockContainer = Mockito.mock(NetworkStockContainer.class);

        double traderFunds = 100.0;
        Mockito.when(mockTraderInfo.getFunds()).thenReturn(traderFunds);
        Map<String, NetworkStock> stockMap = new HashMap<>();
        NetworkStock mockStock = Mockito.mock(NetworkStock.class);
        String stockSymbol = "mockSymbol";
        Mockito.when(mockStock.getPrice()).thenReturn(10.0);
        Mockito.when(mockStock.getSymbol()).thenReturn(stockSymbol);
        stockMap.put(stockSymbol, mockStock);
        Mockito.when(mockStockContainer.getStocksForPrice(traderFunds)).thenReturn(stockMap);

        Map<String, Integer> portfolio = new HashMap<>();
        portfolio.put(stockSymbol, 10);
        Mockito.when(mockTraderInfo.getStockPortfolio()).thenReturn(portfolio);
        Mockito.when(mockStockContainer.getStocks()).thenReturn(stockMap);

        Strategy strategy = new RandomStrategy();
        NetworkOrder order = strategy.execute("hi", 20, mockTraderInfo.getStockPortfolio(), mockStockContainer);
        assertEquals(stockSymbol, order.getSymbol());
        System.out.println(order.getAmount());
        assertTrue(1 <= order.getAmount() && order.getAmount() <= 10);
    }

    @Test
    public void testExecuteStrategyNoFundsNoStocks() {
        NetworkTraderInfo mockTraderInfo = Mockito.mock(NetworkTraderInfo.class);
        NetworkStockContainer mockStockContainer = Mockito.mock(NetworkStockContainer.class);

        double traderFunds = 0;
        Mockito.when(mockTraderInfo.getFunds()).thenReturn(traderFunds);
        Map<String, NetworkStock> stockMap = new HashMap<>();
        Mockito.when(mockStockContainer.getStocksForPrice(traderFunds)).thenReturn(stockMap);

        Map<String, Integer> portfolio = new HashMap<>();
        Mockito.when(mockTraderInfo.getStockPortfolio()).thenReturn(portfolio);
        Mockito.when(mockStockContainer.getStocks()).thenReturn(stockMap);

        Strategy strategy = new RandomStrategy();
        assertNull(strategy.execute("hi", 20, mockTraderInfo.getStockPortfolio(), mockStockContainer));
    }

}
