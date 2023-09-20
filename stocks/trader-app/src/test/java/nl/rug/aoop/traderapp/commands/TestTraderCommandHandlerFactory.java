package nl.rug.aoop.traderapp.commands;

import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTraderCommandHandlerFactory {

    private LocalStockExchange localStockExchange;
    private TraderCommandHandlerFactory factory;

    @BeforeEach
    public void setUp() {
        localStockExchange = Mockito.mock(LocalStockExchange.class);
        factory = new TraderCommandHandlerFactory(localStockExchange);
    }

    @Test
    public void testTraderCommandHandlerFactoryConstructor() {
        assertEquals(localStockExchange, factory.getLocalStockExchange());
    }

    @Test
    public void testCreate() {
        CommandHandler commandHandler = factory.create();

        assertTrue(commandHandler.getCommandMap().containsKey("updateStockContainer"));
        assertTrue(commandHandler.getCommandMap().containsKey("updateTraderInfo"));
    }
}
