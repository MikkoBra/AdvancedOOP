package nl.rug.aoop.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.networking.converter.Converter;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.orderresolver.OrderResolver;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;

import java.io.IOException;
import java.util.Map;

/**
 * LimitOrderCommand is a class containing functionality for executing a limit order sent to a stock exchange.
 */
@Slf4j
public class LimitOrderCommand implements Command {

    @Getter
    private final OrderResolver orderResolver;

    /**
     * Constructor, sets field orderResolver to a passed OrderResolver object containing the implementation of resolving
     * an order.
     *
     * @param orderResolver OrderResolver object containing the stock exchange the LimitOrder should be passed to.
     */
    public LimitOrderCommand(OrderResolver orderResolver) {
        this.orderResolver = orderResolver;
    }

    /**
     * Method for executing the resolving of a limit order. It takes the json representation of a NetworkOrder object
     * from a parameter map, converts it to a LimitOrder object, and sends it to the Command's order resolver.
     *
     * @param params Map containing a String representation of a method parameter as a key, and the actual value of the
     *               intended parameter as a value.
     * @throws IOException When the parameter map was not correctly initialized or contains the wrong information.
     */
    @Override
    public void execute(Map<String, Object> params) throws IOException {
        if (!params.containsKey("body")) {
            throw new IOException("LimitOrderCommand was executed without the \"body\" parameter " +
                    "in the parameter map.");
        }
        try {
            String jsonMessage = (String) params.get("body");
            convertAndResolve(jsonMessage);
        } catch (ClassCastException e) {
            log.error("Parameter object was not castable to String object.");
            throw new IOException();
        }
    }

    /**
     * Helper method for execute(), takes a json string representation of a network order, converts it to a limit order
     * and sends it to the appropriate order resolver.
     *
     * @param jsonMessage Json String representation of a NetworkOrder object to be resolved.
     */
    public void convertAndResolve(String jsonMessage) {
        NetworkOrder order = Converter.fromJson(jsonMessage, NetworkOrder.class);
        LimitOrder limitOrder = new LimitOrder();
        limitOrder.setInformation(order);
        orderResolver.resolveOrder(limitOrder);
    }
}
