package ru.lab.chat.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.lab.chat.controller.ChatController;
import ru.lab.chat.controller.ChatControllerImpl;
import ru.lab.chat.model.Client;
import ru.lab.chat.service.RememberMeService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ChatModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RememberMeService.class).in(Singleton.class);
        bind(ChatController.class).to(ChatControllerImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public Marshaller createMarshaller() throws JAXBException
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(Client.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        return jaxbMarshaller;
    }

    @Provides
    @Singleton
    public Unmarshaller createUnmarshaller() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Client.class);
        return jaxbContext.createUnmarshaller();
    }
}
