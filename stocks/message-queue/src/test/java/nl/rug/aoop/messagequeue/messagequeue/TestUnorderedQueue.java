package nl.rug.aoop.messagequeue.messagequeue;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnorderedQueue {
    private UnorderedQueue unorderedQueue;
    private QueueMessage firstMessage;
    private QueueMessage secondMessage;

    @BeforeEach
    public void setUp() {
        unorderedQueue = new UnorderedQueue();
        firstMessage = new QueueMessage("key1", "QueueMessage 1");
        secondMessage = new QueueMessage("key2", "QueueMessage 2");
    }

    @Test
    public void testConstructor() {
        assertEquals(new LinkedList<QueueMessage>(), unorderedQueue.getQueue());
    }

    @Test
    public void testEnqueue() {
        unorderedQueue.enqueue(firstMessage);
        unorderedQueue.enqueue(secondMessage);
        LinkedList<QueueMessage> testQueue = new LinkedList<>();
        testQueue.add(firstMessage);
        testQueue.add(secondMessage);
        assertEquals(testQueue, unorderedQueue.getQueue());
    }

    @Test
    public void testEnqueueWrongOutput() {
        unorderedQueue.enqueue(firstMessage);
        unorderedQueue.enqueue(secondMessage);
        LinkedList<QueueMessage> testQueue = new LinkedList<>();
        testQueue.add(secondMessage);
        testQueue.add(firstMessage);
        assertNotEquals(testQueue, unorderedQueue.getQueue());
    }

    @Test
    public void testGetSizeNull() {
        assertEquals(0, unorderedQueue.getSize());
    }

    @Test
    public void testGetSize() {
        unorderedQueue.enqueue(firstMessage);
        unorderedQueue.enqueue(secondMessage);
        assertEquals(2, unorderedQueue.getSize());
    }

    @Test
    public void testDequeue() {
        unorderedQueue.enqueue(firstMessage);
        unorderedQueue.enqueue(secondMessage);
        assertEquals(firstMessage, unorderedQueue.dequeue());
        assertEquals(secondMessage, unorderedQueue.dequeue());
        assertEquals(0, unorderedQueue.getSize());
    }

    @Test
    public void testDequeueNull() {
        assertNull(unorderedQueue.dequeue());
    }
}
