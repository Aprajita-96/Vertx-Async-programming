package io.vertx.starter;

import io.vertx.core.AbstractVerticle;

public class BlockingVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {

    vertx.executeBlocking(blocking->{
      //blocking codee will go inside the callback function
      try{
        System.out.println("waiting for thread sleep");
        Thread.sleep(8000);
      }
      catch(InterruptedException ex){
        System.out.println(ex.getMessage());
      }
      //assume this is the ressult of the blocking code.
      String result="Done, handle me to someone else";
      //Wrap the result inside a future so that we read it when completed
      blocking.complete(result);
    },resulthandler->{
      //you can perform non blocking operation
      System.out.println("blocking operation is completed");
      System.out.println(resulthandler.result());
    });

  }
}
