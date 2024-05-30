package de.ugurkartal.eventbus_demo;

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
