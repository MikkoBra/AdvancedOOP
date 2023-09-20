package nl.rug.aoop.stockexchange;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.commandhandler.CommandHandler;
import nl.rug.aoop.messagequeue.message.QueueMessage;
import nl.rug.aoop.messagequeue.messagequeue.NetworkOrderedQueue;
import nl.rug.aoop.messagequeue.mqconsumer.MQConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * StockConsumer is a class containing functionality for automatically polling a NetworkOrderedQueue through the
 * observer pattern, and passing the messages in the queue to a CommandHandler.
 */
@Slf4j
public class StockConsumer implements Observer, MQConsumer {

    @Getter
    private final StockExchange stockExchange;
    @Getter
    private final CommandHandler handler;
    @Getter
    private final NetworkOrderedQueue queue;

    /**
     * Constructor, initializes field variables with passed information, and adds itself as an observer of the
     * passed NetworkOrderedQueue parameter variable.
     *
     * @param stockExchange StockExchange object containing stock exchange information to be manipulated.
     * @param handler       CommandHandler object containing the execution functionality of commands passed to
     *                      the queue.
     * @param queue         NetworkOrderedQueue to be observed and polled from by the StockConsumer.
     */
    public StockConsumer(StockExchange stockExchange, CommandHandler handler, NetworkOrderedQueue queue) {
        this.stockExchange = stockExchange;
        this.handler = handler;
        this.queue = queue;
        queue.addObserver(this);
    }

    /**
     * Method for polling from field variable queue whenever something is added to it over the network, and passing
     * the retrieved message to a command handler which handles it accordingly.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        QueueMessage message = poll();
        Map<String, Object> params = new HashMap<>();
        params.put("header", message.getHeader());
        params.put("body", message.getBody());
        handler.executeCommand(message.getHeader(), params);
    }

    /**
     * Method for retrieving a message from field variable queue.
     *
     * @return The first QueueMessage object in line in the NetworkOrderedQueue.
     */
    @Override
    public QueueMessage poll() {
        return queue.dequeue();
    }
}
