package nl.rug.aoop.stockapp.commands;

import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.commands.ConnectionCommandHandlerFactory;
import nl.rug.aoop.commands.RegisterCommand;
import nl.rug.aoop.commands.UnregisterCommand;
import nl.rug.aoop.messagequeue.commands.MqPutCommand;
import nl.rug.aoop.messagequeue.messagequeue.NetworkOrderedQueue;
import nl.rug.aoop.stockexchange.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestConnectionCommandHandlerFactory {

    private ConnectionManager connectionManager;
    private NetworkOrderedQueue queue;
    private ConnectionCommandHandlerFactory factory;

    @BeforeEach
    public void setUp() {
        connectionManager = Mockito.mock(ConnectionManager.class);
        queue = Mockito.mock(NetworkOrderedQueue.class);
        factory = new ConnectionCommandHandlerFactory(connectionManager, queue);
    }

    @Test
    public void testConstructor() {
        assertEquals(connectionManager, factory.getConnectionManager());
        assertEquals(queue, factory.getQueue());
    }

    @Test
    public void testInitialize() {
        CommandHandler commandHandler = new CommandHandler();
        CommandHandler factoryHandler = factory.create();
        assertEquals(commandHandler.getClass(), factoryHandler.getClass());
        commandHandler.registerCommand("register", new RegisterCommand(connectionManager));
        commandHandler.registerCommand("unregister", new UnregisterCommand(connectionManager));
        commandHandler.registerCommand("MqPut", new MqPutCommand(queue));
        assertTrue(commandHandler.getCommandMap().containsKey("register"));
        assertTrue(commandHandler.getCommandMap().containsKey("unregister"));
        assertTrue(commandHandler.getCommandMap().containsKey("MqPut"));
    }
}
