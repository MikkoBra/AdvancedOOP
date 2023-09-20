package nl.rug.aoop.commands;

import lombok.Getter;
import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.command.factory.CommandHandlerFactory;
import nl.rug.aoop.messagequeue.commands.MqPutCommand;
import nl.rug.aoop.messagequeue.messagequeue.NetworkOrderedQueue;
import nl.rug.aoop.stockexchange.ConnectionManager;

/**
 * ConnectionCommandHandlerFactory is a class that contains functionality for creating a CommandHandler object, which
 * processes commands related to communication between traders and a stock exchange.
 */
public class ConnectionCommandHandlerFactory implements CommandHandlerFactory {
    @Getter
    private final ConnectionManager connectionManager;
    @Getter
    private final NetworkOrderedQueue queue;

    /**
     * Constructor, sets connectionManager and queue fields to passed parameter values for each.
     *
     * @param connectionManager ConnectionManager object to be manipulated through a command handler.
     * @param queue             MessageQueue object to be manipulated through a command handler.
     */
    public ConnectionCommandHandlerFactory(ConnectionManager connectionManager, NetworkOrderedQueue queue) {
        this.connectionManager = connectionManager;
        this.queue = queue;
    }

    /**
     * Method for creating a command handler with commands related to processing requests coming over a network.
     *
     * @return The created CommandHandler object that has a map containing the relevant Command objects.
     */
    @Override
    public CommandHandler create() {
        CommandHandler handler = new CommandHandler();
        handler.registerCommand("register", new RegisterCommand(connectionManager));
        handler.registerCommand("unregister", new UnregisterCommand(connectionManager));
        handler.registerCommand("MqPut", new MqPutCommand(queue));
        return handler;
    }
}
