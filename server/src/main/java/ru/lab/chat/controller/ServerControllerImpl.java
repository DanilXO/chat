package ru.lab.chat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ru.lab.chat.model.Server;

import javax.inject.Inject;

public class ServerControllerImpl implements ServerController{
    @FXML
    private TextArea appLogTextArea;

    private Stage primaryStage;
    private static Server server;

    @Inject
    public ServerControllerImpl(Stage primaryStage) {
        this.primaryStage = primaryStage;
        server = new Server(this);
    }

    public void handleLogClear(){
        appLogTextArea.clear();
    }

    public void handleStopServer(){
        server.closeAll();
        primaryStage.close();
    }

    public static Server getCurrentServer() {
        return server;
    }
    @Override
    public void addLog(String msg){
        appLogTextArea.appendText(msg+"\r\n");
    }
}
