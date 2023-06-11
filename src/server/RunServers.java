package server;

import java.io.IOException;

public class RunServers {
    private static Server[] servers = new Server[101];
    public static void main(String[] args) throws IOException {
        for (int i = 9000; i <= 9100; i++){
            servers[i-9000] = new Server(i);
        }

        for (int i = 0; i <= 100; i++){
            servers[i].start();
        }
    }
}
