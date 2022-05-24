package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {


    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer httpServer = HttpServer.create();
        Handler handler = new Handler();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", handler);
        httpServer.start();
    }


}
