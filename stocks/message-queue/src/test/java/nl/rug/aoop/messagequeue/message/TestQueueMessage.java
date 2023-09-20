package nl.rug.aoop.messagequeue.message;

import nl.rug.aoop.networking.converter.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestQueueMessage {
    private QueueMessage defaultMessage;
    private QueueMessage message;
    private Date timestamp;
    private QueueMessage timestampedMessage;

    @BeforeEach
    public void setUp() {
        defaultMessage = new QueueMessage();
        message = new QueueMessage("header", "body");
        timestamp = new Date();
        timestampedMessage = new QueueMessage("key", "test", timestamp);
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals("", defaultMessage.getHeader());
        assertEquals("", defaultMessage.getBody());
    }

    @Test
    public void testConstructorHeaderBody() {
        assertEquals("header", message.getHeader());
        assertEquals("body", message.getBody());
    }

    @Test
    public void testConstructorTimestamp() {
        assertEquals("key", timestampedMessage.getHeader());
        assertEquals("test", timestampedMessage.getBody());
        assertEquals(timestamp, timestampedMessage.getTimestamp());
    }

    @Test
    public void testCompareTo() {
        Date earlyOffset = new Date(message.getTimestamp().getTime() - 10);
        Date lateOffset = new Date(message.getTimestamp().getTime() + 10);
        QueueMessage earlierMessage = new QueueMessage("early header", "early body", earlyOffset);
        QueueMessage laterMessage = new QueueMessage("late header", "late body", lateOffset);
        assertTrue(message.compareTo(earlierMessage) >= 0);
        assertTrue(message.compareTo(laterMessage) < 0);
    }
}
