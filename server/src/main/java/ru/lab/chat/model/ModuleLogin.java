package ru.lab.chat.model;

import ru.lab.chat.controller.ServerController;
import ru.lab.chat.shared.packets.User;

/**
 * Модуль для работы с логинами.
 */
public class ModuleLogin {
    private boolean needToLogging = true;
    private ServerController serverController;

    public ModuleLogin(ServerController serverController){
        this.serverController = serverController;
    }

    public boolean verification(final User user){
        return true;
    }

    void logging(String msg){
        if(needToLogging)
            serverController.addLog(msg);
    }

}
