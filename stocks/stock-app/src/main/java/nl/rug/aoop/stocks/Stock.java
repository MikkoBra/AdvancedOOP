package nl.rug.aoop.stocks;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;

/**
 * Stock is a class containing functionality to store and manipulate information about the stock of a company in a
 * stock exchange.
 */
@Slf4j
public class Stock implements StockDataModel {

    @Getter
    private final String symbol;
    @Getter
    private final String company;
    @Getter
    private final long shares;
    @Getter
    private double price;
    @Getter
    private double marketCap;

    /**
     * Constructor, sets field variables to passed parameter values and calculates the market capitalization.
     *
     * @param symbol  String symbol representing the company in the stock exchange.
     * @param company String containing the actual name of the company.
     * @param shares  Long value denoting the amount of shares outstanding of the company's stock.
     * @param price   Double value denoting the initial sale price of one share.
     */
    public Stock(String symbol, String company, long shares, double price) {
        this.symbol = symbol;
        this.company = company;
        this.shares = shares;
        this.price = price;
        this.marketCap = shares * price;
    }

    /**
     * Method for updating the price of a stock with the value of a passed double parameter, and
     * adjusting the market capitalization accordingly.
     *
     * @param price Double representing the new price of the Stock object.
     */
    public void update(double price) {
        this.price = price;
        marketCap = this.shares * this.price;
    }

    /**
     * Method for converting the current Stock object into a NetworkStock object.
     *
     * @return A NetworkStock representation of the current Stock object with the same field values.
     */
    public NetworkStock toNetworkStock() {
        return new NetworkStock(symbol, company, shares, price, marketCap);
    }

    /**
     * Getter for field variable company.
     *
     * @return String representation of the name of the company who the stock belongs to.
     */
    @Override
    public String getName() {
        return company;
    }

    /**
     * Getter for field variable shares.
     *
     * @return Long value representing the amount of shares outstanding of the stock.
     */
    @Override
    public long getSharesOutstanding() {
        return shares;
    }
}
