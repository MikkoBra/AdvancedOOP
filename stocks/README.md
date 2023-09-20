<br />
<p align="center">
  <h1 align="center">Stock Market Simulation</h1>

  <p align="center">
    Libraries with functionality for running a stock exchange app with traders.
  </p>

## Table of Contents

* [About the Project](#about-the-project)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running](#running)
* [Modules](#modules)
  * [message-queue](#message-queue)
  * [networking](#networking)
  * [command](#command)
  * [stock-app](#stock-app)
  * [trader-app](#trader-app)
  * [stock-exchange-core](#stock-exchange-core)
  * [stock-market-ui](#stock-market-ui)
  * [util](#util)
* [Design](#design)
  * [Command Pattern](#command-pattern)
  * [Factory Pattern](#factory-pattern)
  * [Observer Pattern](#observer-pattern)
  * [Strategy Pattern](#strategy-pattern)
* [Evaluation](#evaluation)
* [Extras](#extras)

## About The Project

The project contains two applications:

#### Stock App

An app running a stock exchange with internal models for trader info, stock info and information about incoming orders from traders.

#### Trader App

An app that creates a number of trader bots that periodically send orders to the stock exchange over a network.

As mentioned, these apps run over a network. Network functionality is also contained within the project separately, along with queue implementations and interfaces allowing for the use of the command pattern.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
* [Maven 3.6](https://maven.apache.org/download.cgi) or higher
* [Multirun 1.3](https://plugins.jetbrains.com/plugin/7248-multirun) or higher

### Installation

1. Navigate to the `stocks` directory
2. Clean and build the project using:
```sh
mvn install
```

### Running

1. Navigate to the run configurations tab in the top right of IntelliJ
2. Click "Edit Configurations..." 
3. Click the + in the top left corner
4. Create a new Multirun configuration
5. Add the respective main methods of stock-app and trader-app to the multirun configuration, in that order (traders need something to connect to)
6. Run the multirun configuration

## Modules

### message-queue

When a trader wants to purchase or sell something in a stock market, they need to 
somehow communicate this to the stock market. We require such communication between our traders and stock exchange as
well. We implemented this with `messages`. `message-queue` is a module containing classes defining functionality of such
messages. Messages may not be able to be processed after communicating them immediately; it may be the case that the
receiver is busy with another task, or another message in the case of multiple connected traders. As such, `message-queue`
also contains classes for message queues that store messages, and allow for them to be retrieved in specified orders.
Additionally, to outline intended functionality and adhere to the single-responsibility-principle, `message-queue`
contains classes for the objects that interact with the queues in a singular way: `Producer` and `Consumer`.

### networking

Since the app running the traders and the app running the stock exchange are completely separate, there needs to be a
way for the two apps to pass messages to each other, without requiring local references to each other's objects. This is
implemented through classes defined in the `networking` module. It provides functionality for setting up a client-server
connection, with classes `Client` and `Server` taking the roles of the two involved sides. We want
to be able to connect multiple clients (i.e., multiple traders), and we do not want one server instance to take the
responsibility of handling them all. As such, `Server` creates `ClientHandlers` that manage a singular connection with a
`Client`. Furthermore, `networking` contains functionality required for handling messages sent over the network, and
converting them from Strings to the appropriate message type.

### command

In some methods, there is a fork in the road for which method to call next. This especially pertains to message handling,
seeing as there are multiple kinds of messages that can come in over the network (e.g. register, unregister, put message),
and multiple kinds of orders that should be able to be processed (buy order, sell order). If the project is further
expanded, these options can become quite numerous. If we implement the fork through a switch statement, the switch
statement could also potentially become huge, making it hard to read and maintain. The command pattern implements the
fork through a `CommandHandler`. Module `command` contains interfaces and classes that can be used when implementing the
command pattern. It contains the `Command` interface outlining a command, the `CommandHandler` interface, which should be
applicable without augmentation, and the `CommandHandlerFactory` interface, which should put the correct commands in a
`CommandHandler` object when implemented.

### stock-app

Module `stock-app` has classes that define the stock exchange, and a main method that runs the stock exchange app.
The main model of the stock app is the class `StockExchange`. It has a `TraderContainer`, which holds data about
all `Traders` known to the stock exchange, a `StockContainer`, which holds information about all `Stocks` known to the
stock exchange, and maps for unresolved buy and sell `Orders`. Additionally, it has functionality for updating these
when a new order comes in. Communication over the network happens through `ConnectionManager`, which contains
functionality for sending information to traders, and `StockConsumer`, which handles the messages put in its networked
queue by the network (i.e. incoming messages from the trader). Incoming messages that are passed to the stock exchange
are always `QueueMessage` representations of `Orders`, so the `Commands` contained in `stock-app` are tasked with
converting them. Then, the `Orders` must be processed by the stock exchange. This is done by `OrderResolvers`, with
dedicated subclasses for each type. The main app creates instances of required classes, sets up the server and runs it
along with the `ConnectionManager`.

### trader-app

With a stock-app running, we now need traders that use it and modify its contents. However, we do not want to do this
directly from within the stock-app. Instead, we make use of the order functionality by sending orders from a trader app.
The functionality for doing this is contained in module `trader-app`. The main app sets up trader bots, and has them
send orders periodically. The module has been structured such that traders could also be set up by a user, which can
then send orders manually. Class `Trader` represents a trader, which runs and sends orders to the stock app through its
`StockInteractor`, which handles the actual communication with the stock exchange over a network. Each `Trader` is set
up with a buy/sell `Strategy` by the `TraderManager`, and each has a separate client connection to the server.
Since `Traders` also receive information from the stock exchange about the current state of both the exchange and
themselves, they need a local representation of this information in the form of `LocalStockExchange`, which only
holds information and allows minimal augmentation. The module implements the `command` pattern, and has `Commands` for
handling incoming messages from the network.

### stock-exchange-core

The trader needs a local representation of information contained within the stock exchange to make its decisions.
However, we do not want the trader to be able to augment this information too much, much less so the actual information
contained in the stock exchange directly. As such, we need classes that contain the relevant information of each
object required by the trader, and not much else. These classes are contained in module `stock-exchange-core`, and used
exclusively in network communication and by the trader for decision-making purposes. The classes are each named after the
class they mimic from `stock-app`, except with `Network` prepended.

### stock-market-ui

To make the apps user-friendly and to make it easier to track what is going on internally, we need an external
representation of the state of the stock exchange. To this end, we created a user interface, using the provided
`stock-market-ui` module. It contains interfaces for a model representation of the shown components in the UI, and
classes for creating a very standard and simple UI through `SimpleViewFactory`. If we desired so, we could implement our
own view using interface `AbstractViewFactory`. We chose against doing so due to time constraints.

### util

Some functionality is not explicitly supposed to be part of the stock exchange, but is required nonetheless, for example
for object initialization or conversion. Classes containing such functionality are contained in module `util`. Currently,
it contains functionality for loading an object from a .yaml file, which is currently only used for initializing traders
and stocks into the stock exchange in `stock-app`.

## Design

### Command Pattern

We utilize the command pattern in both `stock-app` and `trader-app`. They are used for handling messages and orders
sent over the network. Concretely, network messages can be of types `MqPut`, `register` and `unregister`, and orders can
be of type `buy` and sell. As explained in [command](#command), extending the code could give rise to unreadable and
overly complex code. Even with just these types of messages, a switch statement would be unnecessarily long. The command
pattern makes sure that our code is more generalizable, and easier to read. The addition of separate `CommandHandlers`
also allows for a clear separation of `Commands`, and thus a clearer overview of intended functionality per handler.

### Factory Pattern

We utilize the factory pattern in both `stock-app` and `trader-app`. They are used for initializing `CommandHandlers`,
the `StockExchange`, and the UI. Each of these require several lines of code to set up, or even entire methods, as with
initializing a `StockExchange` from a .yaml file. To ensure that we keep the code readable and decouple responsibilities,
we chose to implement the factory pattern. Even though each current `Factory` class is only used once, they save code
space in whichever classes utilize them.

### Observer Pattern

We utilize the observer pattern exclusively in `StockConsumer` of `stock-app`. It is used for automatically polling from
the `NetworkedMessageQueue` when a message is enqueued. We considered running a method on a thread, which would
continuously dequeue the message queue. However, it would be polling an empty queue most of the time, and we would have
to make sure it was able to do so safely. To save resources, we instead opted to implement the observer pattern, make
the queue observable, and make the `StockConsumer` the observer. This ensures that the queue is only polled and dequeued
whenever there is a message present in the queue, as the poll method is only called upon entering a message in the queue.

### Strategy Pattern

We utilize the strategy pattern exclusively in `trader-app`. It is used for creating a trading strategy for each trader.
Currently, we only have a single strategy, where a trader randomly chooses to buy or sell, and for what amount. However,
we may want to extend the bots to be able to apply different strategies. To ensure that new strategies follow the
intended functionality and pattern, we used the strategy pattern and created an interface `Strategy`, from which we now
only extend `RandomStrategy`. Now, if we want to create a new strategy, we need to extend `Strategy` first, making sure
that no unintended functionality makes its way in.

## Evaluation

After bug testing, the app seems to run quite stably. It is possible that there are undiscovered bugs present in the apps, but we have not had the time to thoroughly test for
them. As such, this section may expand with further use of the apps.

A point of slight concern is the fact that the apps can only run
simultaneously if the stock app is run first. This is due to the fact that the traders need their clients to be able to
connect to something, which they will only try for a seccond. We could let the trader bots wait longer before giving up
on connecting, or manually shut down the client setup process through a termination signal. However, we believe that
it makes more sense to simply manually try to run the trader app again once the stock-app is up and running. After all,
it would make no sense to run bots for something that may never exist. However, if we were
to continue working on the project, this might be a point of improvement.

## Extras

We tried to implement the deployment bonus. 

We created a project diagram, which can be found in the `diagram` folder in
`stocks`. It only contains `stock-app`, `trader-app` and `stock-exchange-core`, as it would become extremely big and
hard to read with the other modules involved.

___


<!-- Below you can find some sections that you would normally put in a README, but we decided to leave out (either because it is not very relevant, or because it is covered by one of the added sections) -->

<!-- ## Usage -->
<!-- Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources. -->

<!-- ## Roadmap -->
<!-- Use this space to show your plans for future additions -->

<!-- ## Contributing -->
<!-- You can use this section to indicate how people can contribute to the project -->

<!-- ## License -->
<!-- You can add here whether the project is distributed under any license -->


<!-- ## Contact -->
<!-- If you want to provide some contact details, this is the place to do it -->

<!-- ## Acknowledgements  -->
