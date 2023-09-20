package nl.rug.aoop.command.commandhandler;

import nl.rug.aoop.command.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TestCommandHandler {

    CommandHandler handler;
    Command testCommand;
    String name;
    Command mockCommand;
    Map<String, Object> params;

    @BeforeEach
    public void setUp() {
        handler = new CommandHandler();
        testCommand = params -> {

        };
        name = "test command";
        mockCommand = Mockito.mock(Command.class);
        params = new HashMap<>();
    }

    @Test
    public void testConstructor() {
        Map<String, Command> map = new HashMap<>();
        assertEquals(handler.getCommandMap(), map);
    }

    @Test
    public void testRegisterCommand() {
        handler.registerCommand(name, testCommand);
        assertEquals(testCommand, handler.getCommandMap().get(name));
    }

    @Test
    public void testRegisterNullCommand() {
        handler.registerCommand(name, null);
        assertFalse(handler.getCommandMap().containsKey(name));
    }

    @Test
    public void testExecuteCommand() throws IOException {
        handler.registerCommand(name, mockCommand);
        handler.executeCommand(name, params);
        verify(mockCommand).execute(params);
    }

    @Test
    public void testExecuteUnregisteredCommand() throws IOException {
        handler.executeCommand(name, params);
        verify(mockCommand, times(0)).execute(params);
    }
}
