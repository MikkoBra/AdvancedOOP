package nl.rug.aoop.stockexchangecore.trader;

import lombok.Getter;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;

import java.util.Map;

/**
 * Contains information of a Trader.
 */
@Getter
public class NetworkTraderInfo {
    private String id;
    private String name;
    private double funds;
    private Map<String, Integer> stockPortfolio;
    private Map<Long, NetworkOrder> transactionHistory;

    /**
     * Empty constructor, fields stay null.
     */
    public NetworkTraderInfo() {
    }

    /**
     * Basic Constructor.
     *
     * @param id                    identifier
     * @param name                  name
     * @param funds                 funds
     * @param stockPortfolio        stock portfolio
     * @param transactionHistory    transactionHistory
     */
    public NetworkTraderInfo(String id, String name, double funds, Map<String, Integer> stockPortfolio,
                             Map<Long, NetworkOrder> transactionHistory) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.stockPortfolio = stockPortfolio;
        this.transactionHistory = transactionHistory;
    }

    /**
     * Used for updating information on a trader after receiving update from Network.
     *
     * @param id             identifier
     * @param name           name
     * @param funds          funds
     * @param stockPortfolio stock portfolio
     */
    public void update(String id, String name, double funds, Map<String, Integer> stockPortfolio) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.stockPortfolio = stockPortfolio;
    }
}

