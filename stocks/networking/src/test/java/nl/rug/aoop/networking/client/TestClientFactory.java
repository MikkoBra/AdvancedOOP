package nl.rug.aoop.networking.client;

import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.command.factory.CommandHandlerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientFactory {
    private InetSocketAddress mockAddress;
    private CommandHandlerFactory mockFactory;

    @BeforeEach
    public void setUp() {
        mockAddress = Mockito.mock(InetSocketAddress.class);
        mockFactory = Mockito.mock(CommandHandlerFactory.class);
        CommandHandler mockCommandHandler = Mockito.mock(CommandHandler.class);
        Mockito.when(mockFactory.create()).thenReturn(mockCommandHandler);
    }

    @Test
    public void testCreateClient() throws IOException {
        try (MockedConstruction<Client> ignrored = Mockito.mockConstruction(Client.class)) {
            assertNotNull(ClientFactory.createClientWithFactory(mockAddress, mockFactory));
            Mockito.verify(mockFactory).create();
        }
    }
}
