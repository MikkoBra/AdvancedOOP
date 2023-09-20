package nl.rug.aoop.messagequeue.messagequeue;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.QueueMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class TestNetworkOrderedQueue {
    private NetworkOrderedQueue networkOrderedQueue;
    private QueueMessage firstMessage;
    private QueueMessage secondMessage;

    @BeforeEach
    public void setUp() {
        networkOrderedQueue = new NetworkOrderedQueue();
        firstMessage = new QueueMessage("first header", "first body");
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        secondMessage = new QueueMessage("second header", "second body");
    }

    @Test
    public void testConstructor() {
        PriorityBlockingQueue<QueueMessage> testQueue = new PriorityBlockingQueue<>();
        assertEquals(testQueue.getClass(), networkOrderedQueue.getQueue().getClass());
        assertEquals(testQueue.toString(), networkOrderedQueue.getQueue().toString());
    }

    @Test
    public void testEnqueue() {
        networkOrderedQueue.enqueue(firstMessage);
        QueueMessage firstEntry = networkOrderedQueue.getQueue().peek();
        assertEquals(firstMessage.getHeader(), firstEntry.getHeader());
        assertEquals(firstMessage.getBody(), firstEntry.getBody());
    }

    @Test
    public void testDequeue() {
        networkOrderedQueue.enqueue(firstMessage);
        assertEquals(firstMessage, networkOrderedQueue.dequeue());
    }

    @Test
    public void testEnqueueOrder() {
        networkOrderedQueue.enqueue(secondMessage);
        networkOrderedQueue.enqueue(firstMessage);
        assertEquals(firstMessage.getHeader(), networkOrderedQueue.dequeue().getHeader());
        assertEquals(secondMessage.getHeader(), networkOrderedQueue.dequeue().getHeader());
    }

    @Test
    public void testGetSizeEmptyQueue() {
        assertEquals(0, networkOrderedQueue.getSize());
    }

    @Test
    public void testGetSizeAfterEnqueue() {
        networkOrderedQueue.enqueue(firstMessage);
        assertEquals(1, networkOrderedQueue.getSize());
    }

    @Test
    public void testDequeueEmpties() {
        networkOrderedQueue.enqueue(firstMessage);
        networkOrderedQueue.dequeue();
        assertEquals(0, networkOrderedQueue.getSize());
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertNull(networkOrderedQueue.dequeue());
    }
}
