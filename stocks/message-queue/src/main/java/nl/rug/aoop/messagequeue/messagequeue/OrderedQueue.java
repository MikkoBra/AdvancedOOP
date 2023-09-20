package nl.rug.aoop.messagequeue.messagequeue;

import nl.rug.aoop.messagequeue.message.QueueMessage;

import java.util.*;

/**
 * OrderedQueue is an implementation of MessageQueue using a TreeMap implementation of SortedMap
 * for its queue object field. The keys of the map are Date objects, the values are ArrayList objects, which
 * take Messages.
 * Ordering is done through TreeMap's inherent comparator, which identifies which Date precedes another,
 * and puts the message with the earliest timestamp first in line.
 */
public class OrderedQueue implements MessageQueue {

    private final SortedMap<Date, List<QueueMessage>> queue;

    /**
     * Constructor, initializes the private SortedMap field, queue, with an empty TreeMap.
     */
    public OrderedQueue() {
        queue = new TreeMap<>();
    }

    /**
     * Method for entering a message into the queue, with the key being the message's timestamp.
     * If the message to be enqueued has the same timestamp as one that already has been enqueued,
     * it will be added to the list located at the same timestamp key.
     *
     * @param message the QueueMessage object to be entered into the private SortedMap field, queue.
     */
    @Override
    public void enqueue(QueueMessage message) {
        Date key = message.getTimestamp();
        if (!queue.containsKey(key)) {
            List<QueueMessage> messageList = new ArrayList<>();
            messageList.add(message);
            queue.put(key, messageList);
        } else {
            queue.get(key).add(message);
        }

    }

    /**
     * Method for removing the message first in line in the queue, and returning it. If the list at the key is empty,
     * the entry is completely removed.
     *
     * @return The first QueueMessage object in the private SortedMap field, queue.
     */
    @Override
    public QueueMessage dequeue() {
        QueueMessage message = queue.get(queue.firstKey()).remove(0);
        if (queue.get(queue.firstKey()).size() == 0) {
            queue.remove(queue.firstKey());
        }
        return message;
    }

    /**
     * Method for returning the queue's current size.
     *
     * @return An int denoting the number of QueueMessage objects in the private SortedMap field, queue.
     */
    @Override
    public int getSize() {
        return queue.size();
    }

    /**
     * Simple getter method for the whole queue.
     *
     * @return The SortedMap object containing all Date:QueueMessage key-value pairs in the queue.
     */
    public SortedMap<Date, List<QueueMessage>> getQueue() {
        return queue;
    }
}
