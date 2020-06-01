package com.ibm.microservice.serviceDiscovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class ServiceDiscoveryExample extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    ServiceDiscoveryOptions serviceDiscoveryOptions=new ServiceDiscoveryOptions();
    //enable discovery server:::::Apache Zookeeper server

    serviceDiscoveryOptions.setBackendConfiguration(new JsonObject()
      .put("connection","127.0.0.1:2181")
      .put("ephemeral",true)
      .put("guaranteed",true)
      .put("basePath","/services/my-backend")
    );

    ServiceDiscovery discovery=ServiceDiscovery.create(vertx,serviceDiscoveryOptions);

    //Record Creation
    Record httpEndpointRecord= HttpEndpoint
      .createRecord("http-posts-service",
        true,
        "jsonplaceholder.typicode.com",
        443,
        "/posts",
        new JsonObject());

    discovery.publish(httpEndpointRecord,HttpResultAfterPublish->{
      if(HttpResultAfterPublish.succeeded())
        System.out.println(HttpResultAfterPublish.result().toJson());
      else
        System.out.println(HttpResultAfterPublish.cause());
    });


    //consume: the record that is published in zookeeper
    vertx.setTimer(5000,wait->{

      //discover service
      HttpEndpoint.getWebClient(discovery,new JsonObject().put("name","http-posts-service"), myresult->{
        WebClient client=myresult.result();
        System.out.println();
        client.get("/posts").send(res->{
            System.out.println(res.result());

          ServiceDiscovery.releaseServiceObject(discovery,client);
        });
      });

    });

  }
}
