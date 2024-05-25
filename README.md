# Vert.x EventBus

Vert.x EventBus is a powerful and flexible messaging system provided by the Vert.x toolkit. It allows different parts of your application to communicate with each other through message passing. The EventBus supports various communication patterns, including publish/subscribe, point-to-point, and request/reply. It can be used for communication within a single JVM or across multiple JVMs and even different physical nodes.

## What you will learn in this repository?
* What is Request/Response Communication
* How to deploy a Verticle
* How to create EventBus for Request
* How to create EventBus for Response
* What are the other Types of EventBus Communication

### Request/Response Communication
This pattern allows a sender to send a message and expect a reply. It is useful for scenarios where a response is needed from the receiver.

### Deploy a Verticle
```java
public class Main {
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }
}
```

### Creating a EventBus for Request
```java
public class RequestVerticle extends AbstractVerticle {
  public static final String ADDRESS = RequestVerticle.class.getName();
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    var eventBus = vertx.eventBus();
    String message = "Hello Vert.x";
    LOGGER.debug("Sending: {}", message);
    eventBus.<String>request(ADDRESS, message, reply -> {
      LOGGER.debug("Response: {}", reply.result().body());
    });
  }
}
```

### Creating a EventBus for Response
```java
public class ResponseVerticle extends AbstractVerticle{
  public static final Logger LOGGER = LoggerFactory.getLogger(ResponseVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message -> {
      LOGGER.debug("Received Message: {}", message.body());
      message.reply("Received your message. Thanks!");
    });
  }
}
```
## Other Types of EventBus Communication
### Point-to-Point Communication
In this pattern, a message is sent from one sender to one receiver. It is useful for direct messaging scenarios where a specific component needs to handle the message.
```java
// Sender
vertx.eventBus().send("address", "Hello, World!");

// Receiver
vertx.eventBus().consumer("address", message -> {
  System.out.println("Received message: " + message.body());
  });
```
### Publish/Subscribe Communication
In this pattern, a message is published to a specific address, and all subscribers to that address receive the message. This is useful for broadcasting events to multiple components.
```java
// Publisher
vertx.eventBus().publish("address", "Hello, World!");

// Subscriber
vertx.eventBus().consumer("address", message -> {
  System.out.println("Received message: " + message.body());
  });
```
