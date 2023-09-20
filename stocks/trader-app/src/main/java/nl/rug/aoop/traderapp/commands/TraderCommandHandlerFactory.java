package nl.rug.aoop.traderapp.commands;

import lombok.Getter;
import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.command.factory.CommandHandlerFactory;
import nl.rug.aoop.traderapp.stockexchange.LocalStockExchange;

/**
 * Factory which creates a commandHandler initialized with the commands available to a trader.
 * Implements CommandHandlerFactory.
 */
public class TraderCommandHandlerFactory implements CommandHandlerFactory {

    @Getter
    private final LocalStockExchange localStockExchange;

    /**
     * Basic constructor. Sets field variable localStockExchange to a passed local representation
     * of the stock exchange.
     *
     * @param localStockExchange LocalStockExchange object that manages interaction between a trader
     *                           and the stock exchange.
     */
    public TraderCommandHandlerFactory(LocalStockExchange localStockExchange) {
        this.localStockExchange = localStockExchange;
    }

    /**
     * Method that creates a new command handler initialized with the commands that should be available to a trader,
     * and returns it.
     *
     * @return The initialized CommandHandler object containing a Map with relevant Commands.
     */
    @Override
    public CommandHandler create() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.registerCommand("updateStockContainer", new UpdateStockContainerCommand(localStockExchange));
        commandHandler.registerCommand("updateTraderInfo", new UpdateTraderInfoCommand(localStockExchange));
        return commandHandler;
    }
}
