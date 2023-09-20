package nl.rug.aoop.messagequeue.messagequeue;

import nl.rug.aoop.messagequeue.message.QueueMessage;

import java.util.LinkedList;

/**
 * OrderedQueue is an implementation of MessageQueue using a LinkedList for its queue object field, taking QueueMessage
 * objects. It is a FIFO queue implementation, meaning the first QueueMessage to be enqueued is the first to be
 * dequeued.
 */
public class UnorderedQueue implements MessageQueue {

    private final LinkedList<QueueMessage> queue;

    /**
     * Constructor, initializes the private LinkedList field, queue, with an empty LinkedList.
     */
    public UnorderedQueue() {
        this.queue = new LinkedList<>();
    }

    /**
     * Method for entering a message into the queue.
     *
     * @param message the QueueMessage object to be entered into the private LinkedList field, queue.
     */
    @Override
    public void enqueue(QueueMessage message) {
        this.queue.add(message);
    }

    /**
     * Method for removing the first message in line in the queue, and returning it.
     *
     * @return The first message in the queue.
     */
    @Override
    public QueueMessage dequeue() {
        return this.queue.poll();
    }

    /**
     * Method for returning the queue's current size.
     *
     * @return An int denoting the number of QueueMessage objects in the private LinkedList field, queue.
     */
    @Override
    public int getSize() {
        return this.queue.size();
    }

    /**
     * Simple getter method for the whole queue.
     *
     * @return The LinkedList object containing all enqueued QueueMessage objects.
     */
    public LinkedList<QueueMessage> getQueue() {
        return queue;
    }
}
