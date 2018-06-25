package ru.lab.chat.service;


import com.google.inject.Inject;
import ru.lab.chat.model.Client;

import javax.xml.bind.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Сервис сохранения данных для авто-входа
 */
public class RememberMeService {
    private String FILE_NAME;
    private File file;
    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;
    @Inject
    public RememberMeService(Unmarshaller unmarshaller,
                             Marshaller marshaller) {
        FILE_NAME = System.getProperty("user.dir")
                + File.separator + "data.xml";
        file = new File(FILE_NAME);
        this.unmarshaller = unmarshaller;
        this.marshaller = marshaller;
    }

    /**
     * @param client - сохраним клиента в xml файл (пока только имя, можно добавить сохранение пароля и.т.д.)
     */
    public void saveClient(Client client) {
        try {
            Client save = new Client();
            save.setName(client.getName());
            marshaller.marshal(save,file);
            marshaller.marshal(save, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return получаем клиента (опять же, можно расширить, нужно, чтобы получить логин)
     */
    private Client getClient(){
        Client client = null;
        if (!Files.exists(Paths.get(FILE_NAME))) {
            return null;
        }
        try {
            client = (Client) unmarshaller.unmarshal(file);
            return client;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existLogin()
    {
        return true;
       // return getClient().equals(null);
    }

    public String getLogin() {
       return Optional.ofNullable(getClient()).map(Client::getName).orElse("anonim");
    }

}
