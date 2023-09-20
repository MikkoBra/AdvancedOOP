package nl.rug.aoop.networking.client;

import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.command.factory.CommandHandlerFactory;
import nl.rug.aoop.networking.messagehandler.MessageHandler;
import nl.rug.aoop.networking.messagehandler.ServerMessageHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Factory used for creating a Client with a CommandHandler given by a CommandHandlerFactory.
 */
public class ClientFactory {

    /**
     * Creates a Client with a CommandHandler initialized by a passed CommandHandlerFactory.
     * @param address   address to connect to.
     * @param factory   used for initializing CommandHandler with correct commands.
     * @return          the created Client
     * @throws IOException  when connection with the address couldn't be established.
     */
    public static Client createClientWithFactory(InetSocketAddress address, CommandHandlerFactory factory)
            throws IOException {
        CommandHandler commandHandler = factory.create();
        MessageHandler messageHandler = new ServerMessageHandler(commandHandler);
        return new Client(address, messageHandler);
    }
}
