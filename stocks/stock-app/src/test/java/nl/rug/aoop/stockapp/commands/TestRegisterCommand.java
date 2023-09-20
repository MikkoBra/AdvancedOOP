package nl.rug.aoop.stockapp.commands;

import nl.rug.aoop.commands.RegisterCommand;
import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.networking.Communicator;
import nl.rug.aoop.stockexchange.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestRegisterCommand {

    private ConnectionManager connectionManager;
    private RegisterCommand command;
    private Map<String, Object> params;

    @BeforeEach
    public void setUp() {
        connectionManager = Mockito.mock(ConnectionManager.class);
        command = new RegisterCommand(connectionManager);
        params = new HashMap<>();
    }

    @Test
    public void testConstructor() {
        assertEquals(connectionManager, command.getConnectionManager());
    }

    @Test
    public void testExecute() throws IOException {
        Communicator communicator = Mockito.mock(Communicator.class);
        String traderId = "bob";
        params.put("communicator", communicator);
        params.put("body", traderId);
        command.execute(params);
        Mockito.verify(connectionManager).connectTrader(traderId, communicator);
        Mockito.verify(connectionManager).run();
    }

    @Test
    public void testExecuteMissingParamKey() {
        Communicator communicator = Mockito.mock(Communicator.class);
        params.put("communicator", communicator);
        assertThrows(IOException.class, () -> command.execute(params));
    }

    @Test
    public void testExecuteWrongObject() {
        params.put("communicator", new StockContainer());
        assertThrows(IOException.class, () -> command.execute(params));
    }
}
