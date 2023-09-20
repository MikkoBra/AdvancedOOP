package nl.rug.aoop.traderapp.trader.strategy;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.stockexchangecore.containers.NetworkStockContainer;
import nl.rug.aoop.stockexchangecore.order.NetworkOrder;
import nl.rug.aoop.stockexchangecore.stocks.NetworkStock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A Random Strategy, buys or sells stocks randomly at prices that are generated randomly.
 */
@Slf4j
public class RandomStrategy implements Strategy {
    private static final Random R = new Random();

    /**
     * Method for executing the random strategy. It checks for affordable stocks within a fifth of the trader's
     * current funds, and all sellable stocks. Then, it randomly creates either a buy or sell order depending on
     * which option is available.
     *
     * @param id             String id of the trader making the order.
     * @param funds          Double representing the funds available to the trader.
     * @param portfolio      Map containing String stock symbols and integer amounts representing the trader's
     *                       owned stocks.
     * @param stockContainer NetworkStockContainer object containing info on the Stock Market.
     * @return NetworkOrder object containing information about the created order.
     */
    @Override
    public NetworkOrder execute(String id, double funds, Map<String, Integer> portfolio,
                                NetworkStockContainer stockContainer) {
        Map<String, NetworkStock> affordableStocks = stockContainer.getStocksForPrice(funds / 5);
        Map<String, Integer> sellableStocks = new HashMap<>();
        portfolio.forEach((symbol, amount) -> {
            if (amount > 0) {
                sellableStocks.put(symbol, amount);
            }
        });
        boolean canBuy = !affordableStocks.isEmpty();
        boolean canSell = !sellableStocks.isEmpty();
        if (!canBuy && !canSell) {
            return null;
        }
        String action = canBuy && (R.nextBoolean() || !canSell) ? "buy" : "sell";
        if (action.equals("buy")) {
            return createBuyOrder(id, funds, affordableStocks);
        }
        return createSellOrder(id, stockContainer, sellableStocks);
    }

    /**
     * Method for creating a buy order for a trader. It randomly chooses an affordable stock to buy. The buy price
     * is randomly chosen within 5% of the current market price. Then, it randomly chooses an amount to buy based
     * on a formula.
     *
     * @param id               String id representing the trader making the order.
     * @param funds            Double value representing the funds available to the trader.
     * @param affordableStocks Map containing string symbols as keys and the corresponding NetworkStock objects as
     *                         values.
     * @return The created NetworkOrder object.
     */
    private NetworkOrder createBuyOrder(String id, double funds, Map<String, NetworkStock> affordableStocks) {
        Object[] values = affordableStocks.values().toArray();
        NetworkStock stock = (NetworkStock) values[R.nextInt(values.length)];
        double price = stock.getPrice();
        price += R.nextDouble(0.05) * price;
        int numberAbleToBuy = (int) (funds / price);
        int amount = funds < 5 * price ? numberAbleToBuy : R.nextInt(0, (int) (funds / (5 * price))) + 1;
        return new NetworkOrder(stock.getSymbol(), amount, price, id, "buy");
    }

    /**
     * Method for creating a sell order for a trader. It randomly chooses a stock to sell. The sell price
     * is randomly chosen within 5% of the current market price. Then, it randomly chooses an amount to sell
     * between 1 and the total amount the trader owns of that stock.
     *
     * @param id             String id representing the trader making the order.
     * @param stockContainer NetworkStockContainer object containing info on the Stock Market.
     * @param sellableStocks Map containing string symbols as keys and integer values representing the amount of shares
     *                       the trader has of each stock.
     * @return The created NetworkOrder object.
     */
    private NetworkOrder createSellOrder(String id, NetworkStockContainer stockContainer,
                                         Map<String, Integer> sellableStocks) {
        Object[] ownedStockSymbols = sellableStocks.keySet().toArray();
        String chosenStockSymbol = (String) ownedStockSymbols[R.nextInt(ownedStockSymbols.length)];
        NetworkStock chosenStock = stockContainer.getStocks().get(chosenStockSymbol);
        double price = chosenStock.getPrice();
        price -= R.nextDouble(0.05) * price;
        int ownedSharesAmount = sellableStocks.get(chosenStock.getSymbol());
        int amount = ownedSharesAmount == 1 ? 1 : R.nextInt(1, ownedSharesAmount);
        return new NetworkOrder(chosenStockSymbol, amount, price, id, "sell");
    }
}
