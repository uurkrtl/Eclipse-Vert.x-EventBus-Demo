# Vert.x EventBus

Vert.x EventBus is a powerful and flexible messaging system provided by the Vert.x toolkit. It allows different parts of your application to communicate with each other through message passing. The EventBus supports various communication patterns, including publish/subscribe, point-to-point, and request/reply. It can be used for communication within a single JVM or across multiple JVMs and even different physical nodes.

## What you will learn in this repository?
* What is Request/Response Communication
* How to deploy a Verticle
* How to create EventBus for Request
* How to create EventBus for Response
* How to use vertx-unit for tests
* What are the other Types of EventBus Communication

### Request/Response Communication
This pattern allows a sender to send a message and expect a reply. It is useful for scenarios where a response is needed from the receiver.

### Deploy a Verticle
```java
import io.vertx.core.Vertx;

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
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestVerticle extends AbstractVerticle {
  public static final String ADDRESS = "message.address";
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    String message = "Hello Vert.x";
    LOGGER.debug("Sending: {}", message);
    vertx.setPeriodic(1000, id ->
      vertx.eventBus().<String>request(ADDRESS, message, reply ->
        LOGGER.debug("Response: {}", reply.result().body())));
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

### Creating a EventBus for Response
```java
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class RequestVerticleTest {

  @Test
  void SendMessageTest (Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new ResponseVerticle(), testContext.succeeding(id -> {
      vertx.eventBus().consumer(RequestVerticle.ADDRESS, message -> {
        assertEquals("Hello Vert.x", message.body());
        testContext.completeNow();
      });
      vertx.deployVerticle(new RequestVerticle(), testContext.succeeding(id2 -> {
        // Waiting for message to be sent
      }));
    }));
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
