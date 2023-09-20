module trader.app {
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires awaitility;
    requires command;
    requires messagequeue;
    requires networking;
    requires stock.exchange.core;
}