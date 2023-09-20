package nl.rug.aoop.messagequeue.mqproducer;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.MessageQueue;
import nl.rug.aoop.messagequeue.messagequeue.OrderedQueue;
import nl.rug.aoop.messagequeue.messagequeue.UnorderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSimpleProducer {

    private MessageQueue orderedQueue;
    private MessageQueue unorderedQueue;
    private QueueMessage message;
    private SimpleProducer mqProducer;

    @BeforeEach
    public void setUp() {
        orderedQueue = new OrderedQueue();
        unorderedQueue = new UnorderedQueue();
        message = new QueueMessage("header", "body");
    }

    @Test
    public void testConstructorOrdered() {
        mqProducer = new SimpleProducer(orderedQueue);
        assertEquals(orderedQueue, mqProducer.getQueue());
    }

    @Test
    public void testConstructorUnordered() {
        mqProducer = new SimpleProducer(unorderedQueue);
        assertEquals(unorderedQueue, mqProducer.getQueue());
    }

    @Test
    public void testPutUnordered() {
        mqProducer = new SimpleProducer(unorderedQueue);
        int originalSize = mqProducer.getQueue().getSize();
        mqProducer.put(message);
        assertEquals(originalSize + 1, mqProducer.getQueue().getSize());
    }

    @Test
    public void testPutOrdered() {
        mqProducer = new SimpleProducer(orderedQueue);
        int originalSize = mqProducer.getQueue().getSize();
        mqProducer.put(message);
        assertEquals(originalSize + 1, mqProducer.getQueue().getSize());
    }
}
