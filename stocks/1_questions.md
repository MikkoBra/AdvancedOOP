# Question 1

Suppose you are developing a similar (if not identical) project for a company. One teammate poses the following:

> "We do not have to worry about logging. The application is very small and tests should take care of any potential bugs. If we really need it, we can print some important data and just comment it out later."

Do you agree or disagree with the proposition? Please elaborate on your reason to agree or disagree. (~50-100 words)

___

**Answer**:

We disagree with the proposition. Logging requires minimal extra effort compared to print statements, and it is a lot
more extensive in the information it provides. Print statements do not show you what went wrong, where it went wrong,
or when it went wrong, whereas logging does. It is also very cumbersome to comment and uncomment print statements
when needed whereas with logging you can leave the logs in the code and determine if you use them through compiling.

___

# Question 2

One of your requirements is to create a message class where `key` and `value` are strings. How could you modify your class so that the key and value could be any different data types and do not require casting by the developer? Preferably, provide the code of the modified class in the answer.
___

**Answer**:

`key` and `value` (now `header` and `body`) could be of type Object, which would allow them to be defined as any data type.

```java
package nl.rug.aoop.messagequeue.message;

import java.time.LocalDateTime;

/**
 * Message is an immutable object that is used for communicating through a message Queue.
 */
public class Message {
    private final Object header;
    private final Object body;
    private final LocalDateTime timestamp;

    /**
     * Constructor, the field timestamp is set to the time when an object is created.
     *
     * @param header used to identify messages, it is not necessarily unique.
     * @param body   contains content of the message.
     */
    public Message(Object header, Object body) {
        this.header = header;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Simple getter method for the message's header.
     *
     * @return A string containing the header of the message.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Simple getter method for the message's body.
     *
     * @return A string containing the body of the message.
     */
    public String getBody() {
        return body;
    }

    /**
     * Simple getter method for the message's timestamp.
     *
     * @return A LocalDateTime object containing the time at which the message was created.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
```

___

# Question 3

How is Continuous Integration applied to (or enforced on) your assignment? (~30-100 words)

___

**Answer**:

Continuous Integration is the act of frequently merging the work of multiple developers into a main directory. This
works only if changes made by one developer does not break the code of another. To prevent this, code can only be merged
when it passes all unit tests. This is applied in our assignment by only letting us merge our code with the main branch
when all our unit tests pass. Other than that, the code also has to pass all checks for code style violations to ensure
that all code is maintainable and readable.

___

# Question 4

One of your colleagues wrote the following class:

```java
import java.util.*;

public class MyMenu {

    private Map<Integer, PlayerAction> actions;

    public MyMenu() {
        actions = new HashMap<>();
        actions.put(0, DoNothingAction());
        actions.put(1, LookAroundAction());
        actions.put(2, FightAction());
    }

    public void printMenuOptions(boolean isInCombat) {
        List<String> menuOptions = new ArrayList<>();
        menuOptions.add("What do you want to?");
        menuOptions.add("\t0) Do nothing");
        menuOptions.add("\t1) Look around");
        if(isInCombat) {
            menuOptions.add("\t2) Fight!");
        }
    }

    public void doOption() {
        int option = getNumber();
        if(actions.containsKey(option)) {
            actions.get(option).execute();
        }
    }

    public int getNumber() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
```
List at least 2 things that you would improve, how it relates to test-driven development and why you would improve these things. Provide the improved code below.

___

**Answer**:

- The first thing we would improve is `printMenuOptions()`.
  It needs to be improved because it does not actually print or save the menu options.
  This causes the method to be useless and untestable.
  If this method was written using TDD, this mistake would not have been made because you can't create a test
  method for this method. We would improve `printMenuOptions()` by making it actually print the menu options.

- The second thing we would improve, is passing the option to the `doOption()` method as a parameter, instead of calling
  the `getNumber()` method within the `doOption()` method.
  It is better to spread out the functionality in case something goes wrong. You would not be able to construct a proper
  unit test for `doOption()` as it is now, because the method is handling input and taking action at the same time.

Improved code:

```java
import java.util.*;

public class MyMenu {

  private Map<Integer, PlayerAction> actions;

  public MyMenu() {
    actions = new HashMap<>();
    actions.put(0, DoNothingAction());
    actions.put(1, LookAroundAction());
    actions.put(2, FightAction());
  }

  public void printMenuOptions(boolean isInCombat) {
    List<String> menuOptions = new ArrayList<>();
    menuOptions.add("What do you want to?");
    menuOptions.add("\t0) Do nothing");
    menuOptions.add("\t1) Look around");
    if(isInCombat) {
      menuOptions.add("\t2) Fight!");
    }
    for (String option : menuOptions) {
      System.out.println(option);
    }
  }

  public void doOption(int option) {
    if(actions.containsKey(option)) {
      actions.get(option).execute();
    }
  }

  public int getNumber() {
    Scanner scanner = new Scanner(System.in);
    return scanner.nextInt();
  }
}
```
___