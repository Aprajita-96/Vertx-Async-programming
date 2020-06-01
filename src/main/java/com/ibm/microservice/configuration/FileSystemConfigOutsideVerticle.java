package com.ibm.microservice.configuration;

import io.vertx.core.AbstractVerticle;

public class FileSystemConfigOutsideVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    //below super.start should be called to get the vertcle data , if we dont use super.start, we dont get the data
    super.start();
    System.out.println(config().getString("name")+ config().getString("version"));
  }

}
