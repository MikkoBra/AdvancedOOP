package nl.rug.aoop.stockexchangecore.stocks;

import lombok.Getter;

/**
 * Contains information about a stock, is used for sending this information over the Network.
 */
@Getter
public class NetworkStock {
    private final String symbol;
    private final String company;
    private final long shares;
    private final double price;
    private final double marketCap;

    /**
     * Basic Constructor.
     *
     * @param symbol    identifier.
     * @param company   company.
     * @param shares    number of shares in circulation.
     * @param price     current price.
     * @param marketCap market cap.
     */
    public NetworkStock(String symbol, String company, long shares, double price, double marketCap) {
        this.symbol = symbol;
        this.company = company;
        this.shares = shares;
        this.price = price;
        this.marketCap = marketCap;
    }
}
