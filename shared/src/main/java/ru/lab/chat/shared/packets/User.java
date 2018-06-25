package ru.lab.chat.shared.packets;

import java.io.Serializable;

public class User extends Packet {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login: '" + login + '\'' +
                ", password: '" + password + '\'' +
                '}';
    }
}
