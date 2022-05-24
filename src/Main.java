import server.KVServer;
import server.KVTaskClient;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");
    }
}