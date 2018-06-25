package ru.lab.chat.model;

import ru.lab.chat.controller.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс сервака.
 */
public class Server {
    // Порт пока один. Потом, когда серверов будет не один нужно будет изменить эту часть.
    private static final int PORT = 12021;

    static List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;
    private ConnectionController connectionController = new ConnectionController();

    static ModuleLogin moduleLogin;
    static ModulePacketTransmitter modulePacketTransmitter;

    public Server(ServerController serverController){
        moduleLogin = new ModuleLogin(serverController);
        modulePacketTransmitter = new ModulePacketTransmitter(serverController);
        connectionController.start();
    }

    /**
     * Закрываем сервак.
     * Вместе с ним и все коннекты.
     */
    public void closeAll() {
        connectionController.work = false;
        try {
            server.close();

            synchronized(connections) {
                for (Connection connection : connections)
                    (connection).close();
            }
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

    /**
     * Отвечает за подклчения клиентов.
     * В отдельном потоке, чтобы не мешать работать UI.
     */
    class ConnectionController extends Thread {
        boolean work = true;

        @Override
        public void run(){
            try {
                server = new ServerSocket(PORT);

                while (work) {
                    Socket socket = server.accept();

                    Connection con = new Connection(socket);
                    connections.add(con);

                    con.start();
                }
            } catch (SocketException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeAll();
            }
        }
    }
}
