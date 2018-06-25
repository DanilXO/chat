package ru.lab.chat.model;

import ru.lab.chat.controller.ChatController;
import ru.lab.chat.shared.packets.AgreeToExit;
import ru.lab.chat.shared.packets.Message;
import ru.lab.chat.shared.packets.User;
import ru.lab.chat.shared.packets.UsersList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс клиента.
 */
@XmlRootElement
public class Client {
    private static final int PORT = 12021;
    private static final String IP = "127.0.0.1";

    private static ChatController chatController;
    private Socket socket;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private Receiver receiver;
    private String name;

    public String getName() {
        return name;
    }
    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
    /**
     * пустой конструктор для сохранения в xml
     * */
    public Client(){

    }

    /**
     * @param name - логин клиента (пока что имя).
     * @param chatController - ссылка на контроллер.
     */
    public Client(String name, ChatController chatController){
        this.chatController = chatController;
        this.name = name;
        try {
            socket = new Socket(IP, PORT);
            // Объектные потоки вывода и ввода.
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Отправляем пакет логина
            outputStream.writeObject(new User(name,""));

            receiver = new Receiver();
            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Клиент пожелал на выход.
     */
    public void exit(){
        println("/exit");
        getUsersList(new ArrayList());
    }

    /**
     * Закрываем все потоки.
     */
    public void close(){
        // Прибиваем поток
        receiver.off();
        receiver.stop();
        // Закрываем потоки.
        try {
            getIn("You have left");
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Класс "радист". Принимает пакеты и разбирает их "по полкам".
     */
    class Receiver extends Thread {
        private boolean stopped = false;

        synchronized void off() {
            stopped = true;
        }

        @Override
        public void run() {
            try {
                while (!stopped || !socket.isClosed()) {
                    Object obj = inputStream.readObject();
                    if (obj instanceof Message)
                        getIn(((Message) obj).getMessage());
                    else if (obj instanceof UsersList)
                        getUsersList(((UsersList) obj).getList());
                    else if(obj instanceof AgreeToExit)
                        close();
                    else
                        System.err.println("Принял неизвестный пакет.");
                }
            } catch (SocketException e){
                System.out.println("Закрыли сокет.");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Функция получила сообщение и передаёт его контроллеру.
     * @param str - сообщение
     */
    private void getIn(String str){
        chatController.getMsg(str);
    }

    /**
     * Функция получила список пользователей и передаёт его контроллеру
     * @param list - список пользователей online.
     */
    private static synchronized void getUsersList(List list){
        try {
            chatController.updateClientsList(list);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Функция отправки сообщения серверу.
     * @param str - сообщение
     */
    public void println(String str){
        try {
            outputStream.writeObject(new Message(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
