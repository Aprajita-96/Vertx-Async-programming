package io.vertx.starter.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

public class RequestFromClient extends AbstractVerticle {


  @Override
  public void start() throws Exception {

    //server configuration
    HttpServerOptions config=new HttpServerOptions();
    config.setPort(3000);

    HttpServer server=vertx.createHttpServer(config);
    //handling client request
    server.requestHandler(request->{
      //************************************************
      // how to read the client request
      request.handler(chunk->{
        System.out.println(chunk);


        //send a response: get response object
        HttpServerResponse response=request.response();
        response.end(chunk);


        //or simply write
        request.response().end(chunk);
      });
      });


    //start the httpserver
    server.listen(serverhandler->{
      if(serverhandler.succeeded()){
        System.out.println("server is up" + serverhandler.result());
      }
      else System.out.println(serverhandler.cause());
    });


  }
}

