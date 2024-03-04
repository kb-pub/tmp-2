package app;

import app.codec.SerializableMessageCodec;
import app.transport.BlockedTransport;
import app.transport.Transport;
import app.message.EchoRequest;

public class Client {
    private final IO io = new IO();
    private final Transport transport = new BlockedTransport(new SerializableMessageCodec());

    public static void main(String[] args) {
        new Client().commandLoop();
    }

    private void commandLoop() {
        String userInput;
        do {
            io.print(">>> ");
            userInput = io.readln().strip().toLowerCase();
            try {
                switch (userInput) {
                    case "echo" -> echo();
                    case "exit" -> io.println("bye!");
                    default -> io.println("unrecognized command");
                }
            } catch (Exception e) {
                io.println("error: " + e.getMessage());
            }
        } while (!"exit".equals(userInput));
    }

    private void echo() {
        io.print("enter a text: ");
        var text = io.readln();
        try {
            transport.connect();
            transport.send(new EchoRequest(text));
            var response = transport.receive();
            io.println("server response: " + response);
        } finally {
            transport.disconnect();
        }
    }
}
