package ru.lab.chat.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import ru.lab.chat.controller.ServerController;
import ru.lab.chat.controller.ServerControllerImpl;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ServerController.class).to(ServerControllerImpl.class).in(Singleton.class);
    }
}
