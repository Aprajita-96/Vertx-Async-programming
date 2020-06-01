package io.vertx.starter.asyncProgramming;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class TimerApi extends AbstractVerticle {


  //sync call
  private void syncapi(String message){
    System.out.println(message);
  }


  //asyncAPI
  private Future<String> delay(Long time, String message){
    Future future=Future.future();
    //trigger async call
    vertx.setTimer(time,callback->{
      future.complete("Timer is ready"+" "+message);
    });
    return future;
  }


  @Override
  public void start() {
    //sync call
    syncapi("start");
    //async call
    delay((long) 5000,"hello").onComplete(h->{
      String asyncresult=h.succeeded()?h.result():h.cause().getMessage();
      System.out.println(asyncresult);
    });
    //sync call
    syncapi("going");
  }
}
