package nl.rug.aoop.messagequeue.commands;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.MessageQueue;
import nl.rug.aoop.networking.converter.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestMqPutCommand {
    MessageQueue messageQueue;
    MqPutCommand command;
    Map<String, Object> params;
    String testMessage;

    @BeforeEach
    public void setUp() {
        messageQueue = Mockito.mock(MessageQueue.class);
        command = new MqPutCommand(messageQueue);
        params = new HashMap<>();
        testMessage = "jsonString";
    }

    @Test
    public void testConstructor() {
        assertEquals(command.getMessageQueue(), messageQueue);
    }

    @Test
    public void testExecute() throws IOException {
        QueueMessage mockMessage = mock(QueueMessage.class);
        try (MockedStatic<Converter> mockStatic = mockStatic(Converter.class)) {
            mockStatic.when(() -> Converter.fromJson(testMessage, QueueMessage.class))
                    .thenReturn(mockMessage);
            params.put("body", testMessage);
            command.execute(params);
            verify(messageQueue).enqueue(mockMessage);
        }
    }

    @Test
    public void testExecuteWrongParameterString() {
        params.put("not a body", testMessage);
        assertThrows(IOException.class, () -> command.execute(params));
        verify(messageQueue, never()).enqueue(any());
    }
}
