package nl.rug.aoop.stockapp.stocks;

import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;
import nl.rug.aoop.stocks.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStock {
    Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock("AAA", "triple A", 20, 50);
    }

    @Test
    public void testStockConstructor() {
        assertEquals("AAA", stock.getSymbol());
        assertEquals("triple A", stock.getCompany());
        assertEquals(20, stock.getShares());
        assertEquals(50, stock.getPrice());
        assertEquals(20 * 50, stock.getMarketCap());
    }

    @Test
    public void testUpdate() {
        stock.update(70);
        assertEquals(stock.getPrice(), 70);
        assertEquals(stock.getMarketCap(), 20 * 70);
    }

    @Test
    public void testToNetworkStock() {
        NetworkStock networkStock = new NetworkStock("AAA", "triple A", 20, 50,
                20 * 50);
        assertEquals(networkStock.getShares(), stock.toNetworkStock().getShares());
        assertEquals(networkStock.getCompany(), stock.toNetworkStock().getCompany());
        assertEquals(networkStock.getPrice(), stock.toNetworkStock().getPrice());
        assertEquals(networkStock.getMarketCap(), stock.toNetworkStock().getMarketCap());
        assertEquals(networkStock.getSymbol(), stock.toNetworkStock().getSymbol());
    }

    @Test
    public void testGetName() {
        assertEquals("triple A", stock.getName());
    }

    @Test
    public void testGetSharesOutstanding() {
        assertEquals(20, stock.getSharesOutstanding());
    }

}
