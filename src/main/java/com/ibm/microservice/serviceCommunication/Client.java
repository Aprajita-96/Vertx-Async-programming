package com.ibm.microservice.serviceCommunication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class Client extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    HttpClientOptions options=new HttpClientOptions();
    HttpClient client=vertx.createHttpClient();

    //to talk to server there are ways
    //way1
    client.getNow(3000,"localhost","/",result->{
      System.out.println("Response code"+result.statusCode());

      //handle resposne
      result.bodyHandler(res->{
        System.out.println(res);
      });
    });

  }
}
