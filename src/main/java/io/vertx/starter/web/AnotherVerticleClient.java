package io.vertx.starter.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

public class AnotherVerticleClient extends AbstractVerticle {


  @Override
  public void start() throws Exception {
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

    //way2
    client.request(HttpMethod.GET,3000,"localhost","/",res->{
      System.out.println("code"+res.statusCode());

      //handle response
      res.bodyHandler(output->{
        System.out.println(output);
      });
    }).end();

    //way3
    client.get(3000,"localhost","/",res->{
      System.out.println("code"+res.statusCode());

      //handle response
      res.bodyHandler(output->{
        System.out.println(output);
      });
    }).end();


  }
}
