package nl.rug.aoop.stockapp.commands;

import nl.rug.aoop.commands.UnregisterCommand;
import nl.rug.aoop.containers.StockContainer;
import nl.rug.aoop.stockexchange.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUnregisterCommand {

    private ConnectionManager connectionManager;
    private UnregisterCommand command;
    private Map<String, Object> params;

    @BeforeEach
    public void setUp() {
        connectionManager = Mockito.mock(ConnectionManager.class);
        command = new UnregisterCommand(connectionManager);
        params = new HashMap<>();
    }

    @Test
    public void testConstructor() {
        assertEquals(connectionManager, command.getConnectionManager());
    }

    @Test
    public void testExecute() throws IOException {
        String traderId = "bob";
        params.put("body", traderId);
        command.execute(params);
        Mockito.verify(connectionManager).disconnectTrader(traderId);
    }

    @Test
    public void testExecuteMissingParamKey() {
        assertThrows(IOException.class, () -> command.execute(params));
    }

    @Test
    public void testExecuteWrongObject() {
        StockContainer mockStocks = Mockito.mock(StockContainer.class);
        params.put("body", mockStocks);
        assertThrows(IOException.class, () -> command.execute(params));
    }
}
