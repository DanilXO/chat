package ru.lab.chat.model;

import ru.lab.chat.controller.ServerController;
import ru.lab.chat.shared.packets.Packet;

import java.io.IOException;

public class ModulePacketTransmitter {
    private boolean needToLogging = true;
    private ServerController serverController;

    public ModulePacketTransmitter(ServerController serverController){
        this.serverController = serverController;
    }

    void sendPacket(Packet packet) throws IOException {
        logging(packet.toString());
        synchronized (Server.connections) {
            for (Connection connection : Server.connections)
                (connection).outputStream.writeObject(packet);
        }
    }

    void logging(String msg){
        if(needToLogging)
            serverController.addLog(msg);
    }
}
