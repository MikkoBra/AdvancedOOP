//package nl.rug.aoop.traderapp.trader;
//
//import nl.rug.aoop.messagequeue.mqproducer.NetworkProducer;
//import nl.rug.aoop.networking.client.Client;
//import nl.rug.aoop.stockexchangecore.networkstockcontainer.NetworkStockContainer;
//import nl.rug.aoop.stockexchangecore.networktraderinfo.NetworkTraderInfo;
//import nl.rug.aoop.traderapp.registrator.Registrator;
//import nl.rug.aoop.traderapp.stockinteractor.StockInteractor;
//import nl.rug.aoop.traderapp.trader.strategy.RandomStrategy;
//
///**
// * Used for creating a Trader.
// */
//public class TraderFactory {
//
//    /**
//     * Creates a Trader.
//     * @param traderId identifier.
//     * @param traderInfo Info of Trader.
//     * @param stockContainer Info on Stock Market.
//     * @param client Client used for communicating through the Network.
//     * @return a Trader.
//     */
//    public static Trader createTraderOnClient(String traderId, NetworkTraderInfo traderInfo,
//                                              NetworkStockContainer stockContainer, Client client) {
//        NetworkProducer networkProducer = new NetworkProducer(client);
//        Registrator registrator = new Registrator(client);
//        StockInteractor stockInteractor = new StockInteractor(networkProducer, registrator);
//
//        return new Trader(traderId, traderInfo, stockInteractor, stockContainer, new RandomStrategy());
//    }
//}
