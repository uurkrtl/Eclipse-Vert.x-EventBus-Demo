package de.ugurkartal.eventbus_demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
