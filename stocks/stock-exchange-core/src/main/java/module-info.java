module stock.exchange.core {
    opens nl.rug.aoop.stockexchangecore.order to com.google.gson;
    opens nl.rug.aoop.stockexchangecore.trader to com.google.gson;
    opens nl.rug.aoop.stockexchangecore.stocks to com.google.gson;
    opens nl.rug.aoop.stockexchangecore.containers to com.google.gson;
    exports nl.rug.aoop.stockexchangecore.order;
    exports nl.rug.aoop.stockexchangecore.stocks;
    exports nl.rug.aoop.stockexchangecore.containers;
    exports nl.rug.aoop.stockexchangecore.trader;
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires util;
    requires networking;
    requires com.google.gson;

}