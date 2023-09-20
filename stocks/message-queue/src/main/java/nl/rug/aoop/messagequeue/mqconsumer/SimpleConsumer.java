package nl.rug.aoop.messagequeue.mqconsumer;

import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.MessageQueue;

/**
 * SimpleConsumer is an implementation of MQConsumer.
 * The MessageQueue to be manipulated by the Consumer is passed to the constructor and put in a field variable.
 */
public class SimpleConsumer implements MQConsumer {

    private final MessageQueue queue;

    /**
     * Constructor, initializes the private MessageQueue object field, queue, with an empty MessageQueue.
     *
     * @param queue The MessageQueue object to be handled by the Consumer.
     */
    public SimpleConsumer(MessageQueue queue) {
        this.queue = queue;
    }

    /**
     * Method that allows the Consumer to dequeue the MessageQueue.
     *
     * @return The QueueMessage object first in line in the MessageQueue.
     */
    @Override
    public QueueMessage poll() {
        return queue.dequeue();
    }

    /**
     * Simple getter method for the whole queue manipulated by the Consumer.
     *
     * @return The MessageQueue object containing all enqueued QueueMessage objects.
     */
    public MessageQueue getQueue() {
        return queue;
    }
}
