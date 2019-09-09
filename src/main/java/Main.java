import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {
        HttpServer server;
        try{
            server = HttpServer.create(new InetSocketAddress(8001), 0);
            server.createContext("/", new GuestBookHandler());
            server.createContext("/static", new Static());
            server.setExecutor(null);
            server.start();
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }
}
