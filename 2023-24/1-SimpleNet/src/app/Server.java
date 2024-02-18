package app;

import app.transport.StringLineTransport;
import app.transport.Transport;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private final IO io = new IO();
    public static void main(String[] args) throws Exception {
        new Server().listenLoop();
    }

    private void listenLoop() throws IOException {
        try (var ss = new ServerSocket(Settings.PORT)) {
            while (true) {
                var clientSocket = ss.accept();
                io.println("client connected: " + clientSocket);
                var transport = new StringLineTransport(clientSocket);
                echo(transport);
            }
        }
    }

    private void echo(Transport transport) {
        transport.send("echo " + transport.receive());
    }
}
