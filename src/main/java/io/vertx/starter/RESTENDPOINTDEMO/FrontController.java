package io.vertx.starter.RESTENDPOINTDEMO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

import java.awt.print.Book;


//  mongodb+srv://admin:<admin>@cluster0-bq2vy.mongodb.net/test

//ApplicationService

class BooksService{
  MongoClient mongoClient;
  BooksService(){}
  BooksService(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

    //api to get data from mongo
    public Future<String> findAll() {
      Promise promise=Promise.promise();
      //document lookup
      mongoClient.find("books",new JsonObject(),lookup->{

        if(lookup.failed()) {
          //send err
          promise.fail(lookup.cause());
          return;
        }
        //else populate documents in JSONArray
        JsonArray array =new JsonArray();
        for(JsonObject o:lookup.result()) {
          array.add(o);
        }
        promise.complete(array.encode());
      });
      return promise.future();
    }

    }


//ApplicationController
class BooksController extends AbstractVerticle{

  BooksService booksService;
    BooksController(){}
    BooksController(MongoClient mongoClient){
    booksService=new BooksService(mongoClient);
    }


  public  Router configBooksEndpoint(){
    Router bookrouter=Router.router(vertx);
    //end points ::::: BELOW REQUEST IS CALLED ROUTING CONTEXT
    bookrouter.get("/list").handler(request->{
      booksService.findAll().onComplete(handler->{
        if(handler.succeeded()){

          //send response :JSON to client
          //set HTTP headers
          request.response().putHeader(HttpHeaders.CONTENT_TYPE,"application/json");

          //send response
          request.response().end(handler.result());
        }
      });
      request.response().end("books lists");
    });
    return bookrouter;

  }
}



//Front controller
public class FrontController extends AbstractVerticle {
//create all the server functionalities
  HttpServer server;
  HttpServerOptions options;
  MongoClient mongoClient;

  @Override
  public void start() throws Exception {
    options=new HttpServerOptions();
    options.setPort(3000);
    options.setHost("localhost");
    server=vertx.createHttpServer(options);

    mongoClient= MongoClient.createShared(vertx,new JsonObject().put("db_name","BooksDb"));



    //create applicationController object
    BooksController booksController=new BooksController(mongoClient);

    //create a router : this is a Main Router
    Router applicationRouter=Router.router(vertx);

    //main Url where Web server handles the request and redirect to other controllers
    applicationRouter.mountSubRouter("/api/books",booksController.configBooksEndpoint());

    //requestHandler
    server.requestHandler(applicationRouter);


    //start the server
    server.listen(server->{
      if(server.succeeded()) System.out.println("Server is up!!");
    });



  }
}
