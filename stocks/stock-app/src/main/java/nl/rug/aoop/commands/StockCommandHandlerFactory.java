package nl.rug.aoop.commands;

import lombok.Getter;
import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.command.factory.CommandHandlerFactory;
import nl.rug.aoop.orderresolver.BuyLimitOrderResolver;
import nl.rug.aoop.orderresolver.SellLimitOrderResolver;
import nl.rug.aoop.stockexchange.StockExchange;

/**
 * StockCommandHandlerFactory is a class that contains functionality for creating a CommandHandler object, which
 * processes commands related to processing orders sent to a stock exchange.
 */
public class StockCommandHandlerFactory implements CommandHandlerFactory {

    @Getter
    private final StockExchange stockExchange;

    /**
     * Constructor, sets stockExchange field to the passed parameter value.
     *
     * @param stockExchange StockExchange object to be manipulated through a command handler.
     */
    public StockCommandHandlerFactory(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * Method for creating a command handler with commands related to processing orders sent to the CommandHandler's
     * stock exchange.
     *
     * @return The created CommandHandler object that has a map containing the relevant Command objects.
     */
    public CommandHandler create() {
        CommandHandler handler = new CommandHandler();
        handler.registerCommand("buy", new LimitOrderCommand(new BuyLimitOrderResolver(stockExchange)));
        handler.registerCommand("sell", new LimitOrderCommand(new SellLimitOrderResolver(stockExchange)));
        return handler;
    }
}
