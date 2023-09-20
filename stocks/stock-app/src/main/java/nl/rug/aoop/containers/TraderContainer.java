package nl.rug.aoop.containers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.order.LimitOrder;
import nl.rug.aoop.trader.TraderInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * TraderContainer is a class defining the functionality of a container that keeps track of all traders in a
 * stock exchange.
 */
@Slf4j
public class TraderContainer extends Container<TraderInfo> {

    @Getter
    private final Map<String, TraderInfo> traders;

    /**
     * Constructor for initializing the traders map with a predefined map of traders.
     *
     * @param traders Map with Strings for keys, and TraderInfo objects for values.
     */
    public TraderContainer(Map<String, TraderInfo> traders) {
        this.traders = traders;
    }

    /**
     * Default constructor, initializes the traders map with an empty HashMap.
     */
    public TraderContainer() {
        this(new HashMap<>());
    }


    /**
     * Method for retrieving information about a specific entry in the trader container.
     *
     * @param id String reference to the entry that was requested from the TraderContainer.
     * @return TraderInfo object that was requested using String parameter id.
     */
    @Override
    public TraderInfo getInfo(String id) {
        return traders.get(id);
    }

    /**
     * Method for removing a trader from the stock exchange.
     *
     * @param id String reference to the TraderInfo object to be removed from the container.
     */
    @Override
    public void removeEntry(String id) {
        traders.remove(id);
    }

    /**
     * Method for adding a trader to the stock exchange.
     *
     * @param trader TraderInfo object to be added to the container.
     */
    @Override
    public void addEntry(TraderInfo trader) {
        traders.put(trader.getId(), trader);
    }

    /**
     * Method for updating the info of a trader in the stock exchange based on an incoming order. It uses the
     * updateTrader method of Trader and passes the order to it.
     *
     * @param order        LimitOrder object containing relevant information for the update.
     * @param amountTraded Integer value representing the amount of shares traded in the transaction.
     * @param salePrice    Double value representing the price at which shares were sold in the transaction.
     */
    public void update(LimitOrder order, int amountTraded, double salePrice) {
        TraderInfo newInfo = traders.get(order.getTraderId());
        newInfo.updateTrader(order, amountTraded, salePrice);
        traders.put(newInfo.getId(), newInfo);
    }

    /**
     * Method for checking whether a trader container is empty or not.
     *
     * @return Boolean denoting the truth value corresponding to whether the container is empty or not.
     */
    @Override
    public boolean isEmpty() {
        return traders.isEmpty();
    }

    /**
     * Method for retrieving the number of traders in the container.
     *
     * @return Integer value representing the number of Traders in the TraderContainer object.
     */
    @Override
    public int getSize() {
        return traders.size();
    }

    /**
     * Method for checking if a trader id can be found in the container.
     *
     * @param id String id representing the trader in the current TraderContainer.
     * @return True if the id was found, false if not.
     */
    @Override
    public boolean hasEntry(String id) {
        return traders.containsKey(id);
    }
}
