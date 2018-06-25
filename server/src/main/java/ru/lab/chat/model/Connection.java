package ru.lab.chat.model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ru.lab.chat.shared.packets.Message;
import ru.lab.chat.shared.packets.User;
import ru.lab.chat.shared.packets.UsersList;

/**
 *  Класс подключения. Наш клиент.
 */
public class Connection extends Thread{
    ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private String name;

    Connection(Socket socket) {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void run() {
        try {
            // Получаем пакет с логином от клиента
            User user = (User)inputStream.readObject();

            // Отправляем пакет в модуль верификации логина и пропускаем, если прошёл проверку.
            if(Server.moduleLogin.verification(user)) {
                name = user.getLogin();

                // Сообщаем всем, что зашёл новый коннект в чатик
                // А так же рассылаем новый список коннектов.
                Server.modulePacketTransmitter.sendPacket(new Message(name + " cames now"));
                Server.modulePacketTransmitter.sendPacket(new UsersList<>(getClientsList()));

                // Данный кусок работает на получении сообщения от других коннектов
                // Если придёт пакет с командой /exit то тормозим
                Message packet;
                while (true) {
                    packet = (Message) inputStream.readObject(); // А если не этот пакет?
                    if (packet.getMessage().equals("/exit"))
                        break;
                    Server.modulePacketTransmitter.sendPacket(new Message(name + ": " + packet.getMessage()));
                }

                // Сообщаем всем, что мы ливнули и отправляем им новый список коннектов.
                Server.modulePacketTransmitter.sendPacket(new Message(name + " has left"));
                Server.modulePacketTransmitter.sendPacket(new UsersList<>(getClientsList()));

                // Отправить клиенту разрешение на выход? Что-то не то )
                // Server.modulePacketTransmitter.sendPacket(new AgreeToExit(true));
            } else {
                Server.modulePacketTransmitter.sendPacket(new Message(user.getLogin()+" не авторизировался."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * Закрываем коннект.
     */
    void close() {
        try {
            inputStream.close();
            outputStream.close();
            Server.connections.remove(this);
        } catch (Exception e) {
            System.err.println("Поток не был закрыт!");
        }
    }

    /**
     * Получаем список имен коннектов.
     */
    List<String> getClientsList(){
        List<String> list = new ArrayList<>();
        for (Connection con: Server.connections)
            list.add(con.name);
        return list;
    }

}
