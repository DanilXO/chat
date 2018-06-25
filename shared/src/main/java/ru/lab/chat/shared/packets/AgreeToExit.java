package ru.lab.chat.shared.packets;

public class AgreeToExit extends Packet {
    private boolean agree;

    public AgreeToExit(boolean agree) {
        this.agree = agree;
    }

    public boolean isAgree(){
        return agree;
    }
}
