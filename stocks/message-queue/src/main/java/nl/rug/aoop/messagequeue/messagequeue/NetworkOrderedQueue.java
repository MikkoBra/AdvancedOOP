package nl.rug.aoop.messagequeue.messagequeue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.QueueMessage;

import java.util.Observable;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * NetworkOrderedQueue is a class that implements the functionality of a MessageQueue over a network.
 */
@Slf4j
public class NetworkOrderedQueue extends Observable implements MessageQueue {

    @Getter
    private final PriorityBlockingQueue<QueueMessage> queue;

    /**
     * Constructor, initializes the private PriorityBlockingQueue field, queue, with an empty queue.
     */
    public NetworkOrderedQueue() {
        queue = new PriorityBlockingQueue<>();
    }

    /**
     * Method for entering a message into the queue.
     *
     * @param message the QueueMessage object to be entered into the private PriorityBlockingQueue field, queue.
     */
    @Override
    public void enqueue(QueueMessage message) {
        synchronized (this) {
            try {
                queue.add(message);
                setChanged();
                notifyObservers();
            } catch (NullPointerException e) {
                log.error("Null objects cannot be added to the queue.");
            }
        }
    }

    /**
     * Method for retrieving and removing a message from the queue.
     *
     * @return The first QueueMessage in the PriorityBlockingQueue.
     */
    @Override
    public QueueMessage dequeue() {
        synchronized (this) {
            return queue.poll();
        }
    }

    /**
     * Method that returns the current size of the queue.
     *
     * @return An int value representing the size of the private PriorityBlockingQueue field, queue.
     */
    @Override
    public int getSize() {
        return queue.size();
    }
}
