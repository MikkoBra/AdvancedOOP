package nl.rug.aoop.stockapp.stockexchange;

import nl.rug.aoop.stockexchange.StockExchange;
import nl.rug.aoop.stockexchange.StockExchangeFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStockExchangeFactory {

    @Test
    public void testInitialize() throws IOException {
        StockExchange exchange = new StockExchangeFactory().initialize("manual");
        assertEquals(0, exchange.getStockContainer().getSize());
        assertEquals(0, exchange.getTraderContainer().getSize());
    }

//    @Test
//    public void testInitializeYaml() throws IOException {
//        StockExchange exchange = new StockExchangeFactory().initialize("yaml");
//        assertEquals(11, exchange.getStockContainer().getSize());
//        assertEquals(9, exchange.getTraderContainer().getSize());
//    }
}
