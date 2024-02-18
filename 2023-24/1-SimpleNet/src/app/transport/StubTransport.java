package app.transport;

import app.IO;

public class StubTransport implements Transport {
    private final IO io = new IO();

    @Override
    public void connect() throws TransportException {
        io.println("transport connecting");
    }

    @Override
    public void disconnect() throws TransportException {
        io.println("transport disconnecting");
    }

    @Override
    public void send(Object o) throws TransportException {
        io.println("transport sending " + o);
    }

    @Override
    public Object receive() throws TransportException {
        io.println("transport receiving an object");
        return "stub transport object";
    }
}
