package nl.rug.aoop.messagequeue.mqconsumer;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.MessageQueue;
import nl.rug.aoop.messagequeue.messagequeue.OrderedQueue;
import nl.rug.aoop.messagequeue.messagequeue.UnorderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSimpleConsumer {
    private MessageQueue unorderedQueue;
    private MessageQueue orderedQueue;
    private QueueMessage message1;
    private QueueMessage message2;

    @BeforeEach
    public void setUp() throws InterruptedException {
        unorderedQueue = new UnorderedQueue();
        orderedQueue = new OrderedQueue();
        message1 = new QueueMessage("header1", "body1");
        Thread.sleep(5);
        message2 = new QueueMessage("header2", "body2");
    }

    @Test
    public void testConstructorUnordered() {
        SimpleConsumer mqConsumer = new SimpleConsumer(unorderedQueue);
        assertEquals(unorderedQueue, mqConsumer.getQueue());
    }

    @Test
    public void testConstructorOrdered() {
        SimpleConsumer mqConsumer = new SimpleConsumer(orderedQueue);
        assertEquals(orderedQueue, mqConsumer.getQueue());
    }

    @Test
    public void testPollUnordered() {
        MessageQueue testQueue = new UnorderedQueue();
        testQueue.enqueue(message1);
        testQueue.enqueue(message2);
        unorderedQueue.enqueue(message1);
        unorderedQueue.enqueue(message2);
        SimpleConsumer mqConsumer = new SimpleConsumer(unorderedQueue);
        assertEquals(testQueue.dequeue().getBody(), mqConsumer.poll().getBody());
        assertEquals(testQueue.dequeue().getBody(), mqConsumer.poll().getBody());
    }

    @Test
    public void testPollOrdered() {
        MessageQueue testQueue = new OrderedQueue();
        testQueue.enqueue(message2);
        testQueue.enqueue(message1);
        orderedQueue.enqueue(message2);
        orderedQueue.enqueue(message1);
        SimpleConsumer mqConsumer = new SimpleConsumer(orderedQueue);
        assertEquals(testQueue.dequeue().getBody(), mqConsumer.poll().getBody());
        assertEquals(testQueue.dequeue().getBody(), mqConsumer.poll().getBody());
    }
}