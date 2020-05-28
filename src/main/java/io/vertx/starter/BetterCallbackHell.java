package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class BetterCallbackHell extends AbstractVerticle {
 //Solution is not to return hell and take method as parameter using handler<AsynResult<String>>
  private void getUser(Handler<AsyncResult<String>> asyncResultHandler){
    String username="Aprajita";
    if(username!=""){
      //handle success
      asyncResultHandler.handle(Future.succeededFuture(username));
    }
    else asyncResultHandler.handle(Future.failedFuture("No user found"));

  }
  private void login(String username,Handler<AsyncResult<String>> asyncResultHandler){
    if(username.equals("Aprajita")){
      //handle success
      asyncResultHandler.handle(Future.succeededFuture("Login success"));
    }
    else asyncResultHandler.handle(Future.failedFuture("failed login"));

  }


  @Override
  public void start() {
  getUser(e->{
    if(e.succeeded()){
      System.out.println(e.result());
      login(e.result(),login->{
        String result=login.succeeded()? login.result():login.cause().getMessage();
        System.out.println(result);
      });
    }
  });
  }
}
