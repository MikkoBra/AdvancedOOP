# Question 1

In the assignment, you had to create a `MessageHandler` interface. Please answer the following two questions:

1. Describe the benefits of using this `MessageHandler` interface. (~50-100 words)
2. Instead of creating an implementation of `MessageHandler` that invokes a command handler, we could also pass the command handler to the client/server directly without the middle man of the `MessageHandler` implementation. What are the implications of this? (~50-100 words)

___

**Answer**:
1. By using the MessageHandler interface, the functionality of what happens with the messages is decoupled from the networking. This means, that with different implementations of the interface, different functionality can be achieved while using the same networking implementation. The advantage of this, is that the same code can be used for multiple applications.
2. The implication of this is that the CommandHandler then is tasked with both converting the messages and executing the right commands. By splitting up this functionality, it is easier to test that both tasks are executed correctly. By using a MessageHandler as middle man, you also leave open the possibility for messages containing something other than a command.
___

# Question 2

One of your colleagues wrote the following class:

```java
public class RookieImplementation {

    private final Car car;

    public RookieImplementation(Car car) {
        this.car = car;
    }

    public void carEventFired(String carEvent) {
        if("steer.left".equals(carEvent)) {
            car.steerLeft();
        } else if("steer.right".equals(carEvent)) {
            car.steerRight();
        } else if("engine.start".equals(carEvent)) {
            car.startEngine();
        } else if("engine.stop".equals(carEvent)) {
            car.stopEngine();
        } else if("pedal.gas".equals(carEvent)) {
            car.accelerate();
        } else if("pedal.brake".equals(carEvent)) {
            car.brake();
        }
    }
}
```

This code makes you angry. Briefly describe why it makes you angry and provide the improved code below.

___

**Answer**:
Once more possible actions are required, the code is going to be hard to read and maintain with an ever-increasing if-else statement. To solve this, we can make use of a command pattern. In this case, the shown class would act as the commandHandler, the actions will be performed by separate Command classes.

Improved code:

```java
public class RookieImplementation {
    
    private final Map<String, CarEvent> carEvents;

    public RookieImplementation() {
        this.carEvents = new HashMap<>();
    }

    public void carEventFired(String carEvent) {
		if (carEvents.containsKey(carEvent)) {
			carEvents.get(carEvent).execute();
		} else {
			log.error("CarEvent not found.");
		}
    }

	public void registerCommand(String name, CarEvent carEvent) {
		if (carEvent != null) {
			carEvents.put(name, carEvent);
		} else {
			log.error("CarEvent was null, therefore not registered.");
		}
	}
}
```
___

# Question 3

You have the following exchange with a colleague:

> **Colleague**: "Hey, look at this! It's super handy. Pretty simple to write custom experiments."

```java
class Experiments {
    public static Model runExperimentA(DataTable dt) {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new RemoveCorrelatedColumnsCommand())
            .setNext(new TrainSVMCommand());

        Config config = new Options();
        config.set("broadcast", true);
        config.set("svmdatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("svmmodel");
    }

    public static Model runExperimentB() {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new TrainSGDCommand());

        Config config = new Options();
        config.set("broadcast", true);
        config.set("sgddatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("sgdmodel");
    }
}
```

> **Colleague**: "I could even create this method to train any of the models we have. Do you know how Jane did it?"

```java
class Processor {
    public static Model getModel(String algorithm, DataTable dt) {
        CommandHandler commandSequence = new TrainSVMCommand()
            .setNext(new TrainSDGCommand())
            .setNext(new TrainRFCommand())
            .setNext(new TrainNNCommand());

        Config config = new Options();
        config.set("broadcast", false);
        config.set(algorithm + "datatable", dt);

        commandSequence.handle(config);

        return (Model) config.get(algorithm + "model");
    }
}
```

> **You**: "Sure! She is using the command pattern. Easy indeed."
>
> **Colleague**: "Yeah. But look again. There is more; she uses another pattern on top of it. I wonder how it works."

1. What is this other pattern? What advantage does it provide to the solution? (~50-100 words)

2. You know the code for `CommandHandler` has to be a simple abstract class in this case, probably containing four methods:
- `CommandHandler setNext(CommandHandler next)` (implemented in `CommandHandler`),
- `void handle(Config config)` (implemented in `CommandHandler`),
- `abstract boolean canHandle(Config config)`,
- `abstract void execute(Config config)`.

Please provide a minimum working example of the `CommandHandler` abstract class.

___

**Answer**:

1. Chain of Responsibility is the other pattern used. It is useful for this implementation, because the commands need to be executed in a certain order. For example, you want to make sure that you clean your data before you train your model on it.

2. 
```java
abstract class CommandHandler {
    protected CommandHandler next;

    public CommandHandler setNext(CommandHandler next) {
        this.next = next;
        return this.next;
    }

    public void handle(Config config) {
        if(canHandle(config)) {
            execute(config);
        }
        if(next != null) {
            next.handle(config);
        }
    }

    abstract boolean canHandle(Config config);

    abstract void execute(Config config);
}
```
___
