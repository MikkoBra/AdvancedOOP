package nl.rug.aoop.networking.messagehandler;

import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class TestServerMessageHandler {

    private CommandHandler commandHandler;
    private ServerMessageHandler messageHandler;
    private Communicator mockCommunicator;

    @BeforeEach
    public void setUp() {
        commandHandler = Mockito.mock(CommandHandler.class);
        messageHandler = new ServerMessageHandler(commandHandler);
        mockCommunicator = Mockito.mock(Communicator.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(commandHandler, messageHandler.getCommandHandler());
    }

    @Test
    public void testHandleMessage() {
        String jsonMessage = "abc";
        NetworkMessage networkMessage = new NetworkMessage("command", jsonMessage);
        String jsonNetworkMessage = Converter.toJson(networkMessage);

        messageHandler.handleMessage(jsonNetworkMessage, mockCommunicator);

        Map<String, Object> params = new HashMap<>();
        params.put("header", "command");
        params.put("body", jsonMessage);
        params.put("communicator", mockCommunicator);

        verify(commandHandler).executeCommand("command", params);
    }
}
