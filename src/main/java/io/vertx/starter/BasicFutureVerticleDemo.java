package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class BasicFutureVerticleDemo extends AbstractVerticle {
  //methods which return future

  private Future<Void> getEmpty(){
    //create Future Object
    Future future=Future.future();
    //Invoke asyn completion event
    future.complete();
    return future;
  }


//method: return basic future with payload
  private Future<String> getDataFuture(){
    Future future=Future.future();
    future.complete("Succeeded the asyn result");
    return future;
  }


  //method to return only error
  private Future<String> fail(){
    Future future=Future.future();
    future.fail("Failed , Something went wrong");
    return future;
  }

  //business logic for data or error
  private Future<String> validate(String name, String passowrd){
    Future<String> future=Future.future();
    //biz logic
    if(name.equals("admin") && passowrd.equals("admin")){
      future.complete("Login success");
    }
    else future.fail("Login fail");
    return future;
  }

  @Override
  public void start() {
    System.out.println("Future");
    //Declare Future Refernce
    Future future=null;
    future=getEmpty();
    //future returns empty result
    if(future.succeeded()){
      System.out.println("future returns sucess message");
    }
    else System.out.println("Future not returned anything");

    //get the future data
    future=getDataFuture();
    //old java style
    future.onComplete(new Handler<AsyncResult>() {
      @Override
      public void handle(AsyncResult event) {

        if(event.succeeded()){
          System.out.println(event.result());
        }
      }
    });
    //lambda style
    getDataFuture().onComplete(handler->{
      if(handler.succeeded()){
        System.out.println(handler.result());
      }
    });
    //replacer for onCompleteg
    getDataFuture().onSuccess(result-> System.out.println(result));
    getDataFuture().onSuccess(System.out::println);

    //Failed method calling
    fail().onComplete(handler->{
      if(handler.failed()){
        System.out.println(handler.cause());
      }
    });
    fail().onFailure(System.out::println);

    //validate method

    validate("admin","admin").onComplete(h->{
      if(h.succeeded()){
        System.out.println(h.result());
      }
      else System.out.println(h.cause());
    });
  }
}
