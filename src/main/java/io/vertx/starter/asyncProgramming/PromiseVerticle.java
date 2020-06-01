package io.vertx.starter.asyncProgramming;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class PromiseVerticle extends AbstractVerticle {


  private Future<String> getname1(){
    Promise<String> prom=Promise.promise();
    String username="Aprajita";
      prom.complete(username);
    return prom.future();
  }
  private Future<String> getname2(){
    Promise<String> prom=Promise.promise();
    String username="Akansha";
    prom.complete(username);
    return prom.future();
  }
  private Future<String> getname3(){
    Promise<String> prom=Promise.promise();
    String username="Anamika";
    prom.complete(username);
    return prom.future();
  }

//returns promise
  private Promise<String> getUserPromise(){
    Promise<String> prom=Promise.promise();
    String username="Aprajita";
    if(username.equals("Aprajita"))
      prom.complete(username);
    else prom.fail("No user found");
    return prom;
  }

  //get future
  private Future<String> getuserFuture(){
    Promise<String> prom=Promise.promise();
    String username="Aprajita";
    if(username.equals("Aprajita"))
      prom.complete(username);
    else prom.fail("No user found");
    return prom.future();
  }

  @Override
  public void start() throws Exception {
    //covert promise to future because prom doesnt have oncomplete
    Future future=getUserPromise().future();
    future.onComplete(h-> System.out.println("Using future"));

    getuserFuture().onComplete(h->{
      System.out.println(h.result());
    });


    //below example when we calll so many api's and want result in consolidated way
    //grab all results from getname1 2 and 3
    List<Future> futures= Arrays.asList(getname1(),getname2(),getname3());
    CompositeFuture.all(futures).onComplete(handle->{
      handle.result().list().forEach(System.out::println);
    });
  }
}
