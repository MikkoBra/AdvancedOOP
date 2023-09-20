package nl.rug.aoop.messagequeue.messagequeue;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestOrderedQueue {
    private OrderedQueue orderedQueue;
    private QueueMessage firstMessage;
    private QueueMessage secondMessage;

    @BeforeEach
    public void setUp() {
        orderedQueue = new OrderedQueue();
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
        assertEquals(new TreeMap<LocalDateTime, List<QueueMessage>>(), orderedQueue.getQueue());
    }

    @Test
    public void testEnqueue() {
        orderedQueue.enqueue(firstMessage);
        QueueMessage testMessage = new QueueMessage("first header", "first body");
        QueueMessage firstEntry = orderedQueue.getQueue().get(orderedQueue.getQueue().firstKey()).get(0);
        assertEquals(testMessage.getHeader(), firstEntry.getHeader());
        assertEquals(testMessage.getBody(), firstEntry.getBody());
    }

    @Test
    public void testDequeue() {
        orderedQueue.enqueue(firstMessage);
        assertEquals(firstMessage, orderedQueue.dequeue());
    }

    @Test
    public void testEnqueueSameKey() {
        orderedQueue.enqueue(firstMessage);
        orderedQueue.enqueue(firstMessage);

        ArrayList<QueueMessage> messageList = new ArrayList<>();
        messageList.add(firstMessage);
        messageList.add(firstMessage);
        assertEquals(orderedQueue.getQueue().get(orderedQueue.getQueue().firstKey()), messageList);
    }

    @Test
    public void testEnqueueOrder() {
        System.out.println(secondMessage.getTimestamp());
        System.out.println(firstMessage.getTimestamp());
        orderedQueue.enqueue(secondMessage);
        orderedQueue.enqueue(firstMessage);
        QueueMessage firstTestMessage = new QueueMessage("first header", "first body");
        QueueMessage secondTestMessage = new QueueMessage("second header", "second body");
        assertEquals(firstTestMessage.getHeader(), orderedQueue.dequeue().getHeader());
        assertEquals(secondTestMessage.getHeader(), orderedQueue.dequeue().getHeader());
    }

    @Test
    public void testGetSize() {
        orderedQueue.enqueue(firstMessage);
        assertEquals(1, orderedQueue.getSize());
    }

    @Test
    public void testGetSizeEmptyQueue() {
        assertEquals(0, orderedQueue.getSize());
    }

    @Test
    public void testDequeueEmpties() {
        orderedQueue.enqueue(firstMessage);
        orderedQueue.dequeue();
        assertEquals(0, orderedQueue.getSize());
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertThrows(NoSuchElementException.class, () -> orderedQueue.dequeue());
    }

}