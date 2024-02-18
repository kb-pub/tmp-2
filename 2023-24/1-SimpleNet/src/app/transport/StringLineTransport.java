package app.transport;

import app.Settings;

import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class StringLineTransport implements Transport {
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    public StringLineTransport() {
    }

    public StringLineTransport(Socket socket) {
        try {
            this.socket = socket;
            writer = new PrintWriter(socket.getOutputStream(), false, StandardCharsets.UTF_8);
            scanner = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void connect() throws TransportException {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(Settings.HOST, Settings.PORT);
                writer = new PrintWriter(socket.getOutputStream(), false, StandardCharsets.UTF_8);
                scanner = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void disconnect() throws TransportException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void send(Object o) throws TransportException {
        checkIsConnected();
        try {
            writer.println(o);
            writer.flush();
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public Object receive() throws TransportException {
        checkIsConnected();
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    private void checkIsConnected() {
        if (socket == null || socket.isClosed())
            throw new TransportException("transport closed");
    }
}
