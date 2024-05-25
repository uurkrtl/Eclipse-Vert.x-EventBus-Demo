package de.ugurkartal.eventbus_demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
