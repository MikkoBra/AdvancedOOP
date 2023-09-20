package nl.rug.aoop.messagequeue.mqproducer;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.MessageQueue;

/**
 * SimpleProducer is an implementation of MQProducer.
 * The MessageQueue to be manipulated by the Producer is passed to the constructor and put in a field variable.
 */
public class SimpleProducer implements MQProducer {

    private final MessageQueue queue;

    /**
     * Constructor, initializes the private MessageQueue field, queue, with an empty MessageQueue.
     *
     * @param queue The MessageQueue object to be handled by the Producer.
     */
    public SimpleProducer(MessageQueue queue) {
        this.queue = queue;
    }

    /**
     * Method that allows the Producer to enqueue a message into the MessageQueue object field, queue.
     */
    @Override
    public void put(QueueMessage message) {
        queue.enqueue(message);
    }

    /**
     * Simple getter method for the whole queue manipulated by the Producer.
     *
     * @return The MessageQueue object containing all enqueued QueueMessage objects.
     */
    public MessageQueue getQueue() {
        return queue;
    }
}
