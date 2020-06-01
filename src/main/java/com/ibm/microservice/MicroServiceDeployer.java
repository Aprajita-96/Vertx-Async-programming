package com.ibm.microservice;

import com.ibm.microservice.circuitBreaker.CircuitBreakerExample;
import com.ibm.microservice.serviceCommunication.ThirdPartyRestCalls;
import com.ibm.microservice.serviceDiscovery.ServiceDiscoveryExample;
import com.ibm.microserviceArchitecture.ClusterVerticle;
import com.ibm.microserviceArchitecture.HTTPEndPointWithBase;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class MicroServiceDeployer  extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    super.start();
//    vertx.deployVerticle(new FileSystemConfigVerticle());


    //this is sclable to all the veticles
    //below is the code whre we are setting the configurations outside the veticle and not similar to FileSystemCongig class where all the stuff was written inside the java class only
    ConfigStoreOptions options =new ConfigStoreOptions();
    options.setType("file");
    options.setConfig(new JsonObject().put("path","application.json"));


    ConfigRetriever retriever=ConfigRetriever.create(vertx,new ConfigRetrieverOptions().addStore(options));


    retriever.getConfig(config->{
      //once config ready: deploy the verticle
      JsonObject jsonObject=config.result().getJsonObject("application");
//      vertx.deployVerticle(new FileSystemConfigOutsideVerticle(),new DeploymentOptions().setConfig(jsonObject));
    });



    //YAML READER
   // NOTE: to read a property file of yaml type
    //options.setType("file")
    //options.setFormat("yaml")
    //YAML Reader
    ConfigStoreOptions optionsYaml = new ConfigStoreOptions();
    optionsYaml.setType("file");
    optionsYaml.setFormat("yaml");
    optionsYaml.setConfig(new JsonObject().put("path", "application.yaml"));
    ConfigRetriever retrieverYaml = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(optionsYaml));

    retrieverYaml.getConfig(yamlProps -> {
      System.out.println("Yaml Properties");
      System.out.println((yamlProps.result()));
      System.out.println(yamlProps.result().getInteger("port"));
    });


    //deploy service call
//    vertx.deployVerticle(new ThirdPartyRestCalls());
  vertx.deployVerticle(new ThirdPartyRestCalls());
  }
}
