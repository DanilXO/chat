package ru.lab.chat.controller;

import java.util.List;

/**
 * Интерфейс чат контроллера.
 */
public interface ChatController {

    void getMsg(String str);
    void updateClientsList(List<String> list);
}
