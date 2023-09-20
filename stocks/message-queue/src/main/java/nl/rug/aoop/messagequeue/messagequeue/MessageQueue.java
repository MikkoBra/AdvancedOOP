package nl.rug.aoop.messagequeue.messagequeue;

import nl.rug.aoop.messagequeue.message.QueueMessage;

/**
 * MessageQueue is an interface describing the required functionality of a queue holding QueueMessage objects.
 */
public interface MessageQueue {

    /**
     * Method for entering a message into the queue.
     *
     * @param message the message to be entered into the queue.
     */
    void enqueue(QueueMessage message);

    /**
     * Method for removing the message first in line in the queue and returning it,
     * with ordering being dependent on the implemented queue type.
     *
     * @return The first QueueMessage object in the MessageQueue.
     */
    QueueMessage dequeue();

    /**
     * Simple getter method for the current size of the queue.
     *
     * @return An int denoting the number of Messages in the MessageQueue.
     */
    int getSize();
}
