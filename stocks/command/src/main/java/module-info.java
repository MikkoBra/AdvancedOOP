module command {
    requires static lombok;
    requires org.slf4j;
    exports nl.rug.aoop.command.command to messagequeue, networking, stock.app, trader.app;
    exports nl.rug.aoop.command.commandhandler to messagequeue, networking, stock.app, trader.app;
    exports nl.rug.aoop.command.factory;
    // If you want to allow this module to be used in other modules, uncomment the following line:
    //    exports exports nl.rug.aoop.command;
    // Note that this will not include any sub-level packages. If you want to export more, then add those as well:
    //    exports exports nl.rug.aoop.command.example;
}