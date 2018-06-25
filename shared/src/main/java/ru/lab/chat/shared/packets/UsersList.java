package ru.lab.chat.shared.packets;

import java.util.List;

public class UsersList<E> extends Packet {
    private List<E> list;

    public UsersList(List<E> list) {
        this.list = list;
    }

    public List<E> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "UsersList now: {" + list + '}';
    }
}
