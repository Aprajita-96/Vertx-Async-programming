package io.vertx.starter.ioProgramming;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;

public class FileIoVerticle extends AbstractVerticle {

  static class FileService{
    //read a file method
    static Vertx vertx=Vertx.vertx();
    public static Future<String> readFile(){
      Promise promise=Promise.promise();
      FileSystem file=vertx.fileSystem();
      // read file operation is handled by kernel threads
      file.readFile("assests/hello.txt",fhandler->{
        if(fhandler.succeeded()){
          promise.complete(fhandler.result().toString());
        }else promise.fail(fhandler.cause());
      });
      return promise.future();
    }

    public static Future<String> writeFile(String message){
      Promise promise=Promise.promise();
      FileSystem file=vertx.fileSystem();
      file.writeFile("assests/hello_copy.txt",Buffer.buffer("Welcome"+" "+message),result->{
        if(result.succeeded()){
          promise.complete("File has written successfully");
        }
        else promise.fail("failed");
      });
      return promise.future();
    }

    public static void createBuffer() {

      Buffer buffer = Buffer.buffer();
      buffer.appendString("Hello");
      buffer.appendByte((byte) 127);
      buffer.appendShort((short) 127);
      buffer.appendInt(127);
      buffer.appendLong(127);
      buffer.appendFloat(127.0F);
      buffer.appendDouble(127.0D);
      System.out.println("buffer.length() = " + buffer.length());
      for (int i = 0; i < buffer.length(); i += 4) {
        System.out.println("int value at " + i + " is " + buffer.getInt(i));
      }
    }

    public static Future<JsonObject> processJSON(){
      Promise promise=Promise.promise();

      JsonObject jsonObject=new JsonObject();
      jsonObject.put("id","1");
      jsonObject.put("name","Aprajita");
      jsonObject.put("location","Hyderabad");
      promise.complete(jsonObject);
      return promise.future();
    }
  }

  @Override
  public void start() throws Exception {
    //need file content in start method
    FileService.readFile().onComplete(result->{
        System.out.println(result.result());
      });
    FileService.writeFile("Aprajita").onComplete(res->{
      System.out.println(res.result());
    });
    FileService.readFile().compose(m->FileService.writeFile(m)).onComplete(res->{
      if(res.succeeded()){
        System.out.println(res.result());
      }
      else System.out.println(res.cause());
    });
    FileService.createBuffer();
    FileService.processJSON().onComplete(res->{
      System.out.println(res.result().encodePrettily());
    });
  }
}
