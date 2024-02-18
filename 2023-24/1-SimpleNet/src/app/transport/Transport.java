package app.transport;

public interface Transport {
    void connect() throws TransportException;
    void disconnect() throws TransportException;
    void send(Object o) throws TransportException;
    Object receive() throws TransportException;
}
