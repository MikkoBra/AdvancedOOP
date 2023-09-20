package nl.rug.aoop.stockexchangecore.stocks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNetworkStock {

    @Test
    public void testNetworkStockConstructor() {
        NetworkStock stock = new NetworkStock("CMP", "company", 200, 50.0, 200 * 50);
        assertEquals("CMP", stock.getSymbol());
        assertEquals("company", stock.getCompany());
        assertEquals(200, stock.getShares());
        assertEquals(50.0, stock.getPrice());
        assertEquals(200 * 50, stock.getMarketCap());
    }
}
