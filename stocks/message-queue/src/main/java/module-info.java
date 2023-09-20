module messagequeue {
    // Needed for gson to work. If your message queue resides in a sub-package,
    // be sure to open this to com.google.gson as well.
    opens nl.rug.aoop.messagequeue.message to com.google.gson;
    exports nl.rug.aoop.messagequeue.message;
    exports nl.rug.aoop.messagequeue.mqconsumer;
    exports nl.rug.aoop.messagequeue.messagequeue;
    exports nl.rug.aoop.messagequeue.commands;
    exports nl.rug.aoop.messagequeue.mqproducer;
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires com.google.gson;
    requires command;
    requires networking;
}