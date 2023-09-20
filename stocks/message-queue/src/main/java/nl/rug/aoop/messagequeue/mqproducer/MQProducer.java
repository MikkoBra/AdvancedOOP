package nl.rug.aoop.messagequeue.mqproducer;

import nl.rug.aoop.messagequeue.message.QueueMessage;

/**
 * MQProducer is an interface describing the functionality of a Producer which can put Messages into a MessageQueue.
 */
public interface MQProducer {

    /**
     * Method that allows the Producer to put a message in a MessageQueue.
     *
     * @param message The QueueMessage object to be put in the MessageQueue
     */
    void put(QueueMessage message);

}
