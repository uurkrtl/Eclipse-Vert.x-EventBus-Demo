package de.ugurkartal.eventbus_demo;

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
