package io.vertx.starter.asyncProgramming;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class CallbackHellAvoidUsingCompose extends AbstractVerticle {

  private Future<Void> startHttpServer(){
    System.out.println("Http server!!....");
    Future<Void> future =Future.future();
    future.complete();
    return future;
  }

  private Future<String> sayHello(String name){
    Future<String> future =Future.future();
    vertx.setTimer(100,h->future.complete("Hello"+" "+name));
    return future;
  }
  private Future<String> sayWorld(){
    Future<String> future =Future.future();
    vertx.setTimer(100,h->future.complete("World"));
    return future;
  }

  private Future<Void> prepareDatabase(){
    System.out.println("Database is ready");
    Future<Void> future =Future.future();
    future.complete();
    return future;
  }

  @Override
  public void start() throws Exception {
    //traditional
    prepareDatabase().onComplete(h->{
      if(h.succeeded()){
        startHttpServer().onComplete(http->{
          System.out.println("server is up.");
        });
      }
    });

    //using compose call
    prepareDatabase().compose(m->startHttpServer()).onComplete(h->{
      if(h.succeeded())
      System.out.println("server up inside compose");
    });
    //async call
    sayWorld().onComplete(worls->{
      if(worls.succeeded()){
        sayHello(worls.result()).onComplete(hello->{
          System.out.println(hello.result());
        });
      }
    });

    //using compose

    sayWorld().compose(m->sayHello(m)).onComplete(world->{
      if(world.succeeded())
      System.out.println(world.result());
      else
        System.out.println(world.cause());
    });

  }
}
