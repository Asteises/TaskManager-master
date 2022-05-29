package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer httpServer;
    private static Handler handler;
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException, InterruptedException {
        httpServer = HttpServer.create();
        handler = new Handler();
    }

    public static void main(String[] args) throws IOException {
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", handler);
        httpServer.start();
    }
}
