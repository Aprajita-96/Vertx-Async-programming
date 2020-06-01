package com.ibm.microservice.configuration;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;

public class FileSystemConfigVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    super.start();
  //config store options
    //add storage options:type,format,file path
    ConfigStoreOptions options =new ConfigStoreOptions();
    options.setType("file");
    options.setConfig(new JsonObject().put("path","application.json"));


    ConfigRetriever retriever=ConfigRetriever.create(vertx,new ConfigRetrieverOptions().addStore(options));

    retriever.getConfig(result->{
      if(result.succeeded()){
        JsonObject resultant=result.result();
        System.out.println(resultant.getJsonObject("application")
                                      .getString("name")
        );
      }
      else System.out.println("config error"+result.cause());
    });



    //below code is to start the server with the application property file
    retriever.getConfig(config->{
      if(config.succeeded()){
        JsonObject resultant=config.result();

        HttpServerOptions httpServerOptions=new HttpServerOptions();
        httpServerOptions.setPort(resultant.getJsonObject("application").getInteger("port"));

        HttpServer server=vertx.createHttpServer(httpServerOptions);
        server.requestHandler(context->{
          JsonObject object=new JsonObject();
          object.put("name",resultant.getJsonObject("application").getString("name"));
          object.put("name",resultant.getJsonObject("application").getString("version"));

          context.response().end(object.toString());

        });

      }
    });
  }
}
