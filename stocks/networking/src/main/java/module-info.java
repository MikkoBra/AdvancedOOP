module networking {
    exports nl.rug.aoop.networking.client to messagequeue, trader.app;
    exports nl.rug.aoop.networking.messagehandler to messagequeue, stock.app, trader.app;
    exports nl.rug.aoop.networking.server;
    exports nl.rug.aoop.networking;
    exports nl.rug.aoop.networking.converter;
    exports nl.rug.aoop.networking.networkmessage;
    opens nl.rug.aoop.networking.networkmessage to com.google.gson;
    // If you are using Mockito in another module to mock a networking item from this package,
    // then add "opens .. to ..". If we are mocking e.g. a NetworkClient interface
    // in the module messagequeue, then we need:
    //    opens nl.rug.aoop.networking to messagequeue;
    // Again, sub-packages have to be explicitly opened as well. Any error messages should indicate this.
    // If you want to allow this module to be used in other modules, uncomment the following line:
    //    exports nl.rug.aoop.networking;
    // Note that this will not include any sub-level packages. If you want to export more, then add those as well:
    //    exports exports nl.rug.aoop.networking.example;
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires command;
    requires com.google.gson;
}