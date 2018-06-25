package ru.lab.chat.shared.packets;

public class Message extends Packet {
    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "New message{'" + message + '\'' + '}';
    }
}
