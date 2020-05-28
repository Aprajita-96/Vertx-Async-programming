package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class NestedCallbackVerticle extends AbstractVerticle {


//get user: which is returning user information
  private Future<String> getUser(){
    Future future=Future.future();
    //biz logic
    String fakerUser="Aprajita";
    if(fakerUser!=""){
      future.complete(fakerUser);
    }
    else future.fail("User Not found");
    return future;
  }
  private Future<String> login(String name){
    Future future=Future.future();
    if(name=="Aprajita"){
      future.complete("LoginSuccess");
    }
    else future.fail("Login failed");
    return future;
  }
  //navigate to dashboard
  private Future<String> page(String page){
    Future future=Future.future();
    if(page.equals("LoginSuccess")){
      future.complete("Welcome to dashboard");
    }
    else future.fail("No page found");
    return future;
  }
  @Override
  public void start() {

    //nested callback
    getUser().onComplete(h->{
      if(h.succeeded()){
        System.out.println("User "+h.result());
        //you get user :continue with login
        login(h.result()).onComplete(loginhandler->{
          if(loginhandler.succeeded()) {
            System.out.println(loginhandler.result());
            page(loginhandler.result()).onComplete(pagehandler->{
              if(pagehandler.succeeded()) System.out.println(pagehandler.result());
              else System.out.println(pagehandler.cause());
            });
          }
          else System.out.println(loginhandler.cause());
        });
      }
      else{
        //get error:if error , stop the flow
        System.out.println(h.cause());
      }
    });

    //the above code cannot be understood and is called callback hell hence see bettercallback .class
  }
}
