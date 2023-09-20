package nl.rug.aoop.stockapp.commands;

import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.commands.StockCommandHandlerFactory;
import nl.rug.aoop.stockexchange.StockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStockCommandHandlerFactory {

    StockExchange stockExchange;
    StockCommandHandlerFactory factory;

    @BeforeEach
    public void setUp() {
        stockExchange = Mockito.mock(StockExchange.class);
        factory = new StockCommandHandlerFactory(stockExchange);
    }

    @Test
    public void testConstructor() {
        assertEquals(stockExchange, factory.getStockExchange());
    }

    @Test
    public void testInitialize() {
        CommandHandler factoryHandler = factory.create();
        assertEquals(CommandHandler.class, factoryHandler.getClass());
        assertTrue(factoryHandler.getCommandMap().containsKey("buy"));
        assertTrue(factoryHandler.getCommandMap().containsKey("sell"));
    }
}
