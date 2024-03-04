package app.message;

import app.message.Message;

public class EchoResponse extends Message {
    private final String message;
    public EchoResponse(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
