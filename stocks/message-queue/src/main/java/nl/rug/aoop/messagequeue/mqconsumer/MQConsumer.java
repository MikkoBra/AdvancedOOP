package nl.rug.aoop.messagequeue.mqconsumer;

import nl.rug.aoop.messagequeue.message.QueueMessage;

/**
 * MQConsumer is an interface describing the functionality of a Consumer which can poll a MessageQueue.
 */
public interface MQConsumer {

    /**
     * Method for retrieving the first message in line in a MessageQueue.
     *
     * @return The first QueueMessage object in the MessageQueue.
     */
    QueueMessage poll();
}
