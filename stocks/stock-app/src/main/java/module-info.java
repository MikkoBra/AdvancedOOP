module stock.app {
    opens nl.rug.aoop.trader to com.fasterxml.jackson.databind;
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires util;
    requires stock.exchange.core;
    requires networking;
    requires command;
    requires messagequeue;
    requires awaitility;
    requires stock.market.ui;
}