package io.vertx.starter.web;

import io.vertx.core.AbstractVerticle;

public class ApplicationVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    //how to depoy verticle
    vertx.deployVerticle(new OneVerticleServer());
    vertx.deployVerticle(new AnotherVerticleClient());
  }
}
