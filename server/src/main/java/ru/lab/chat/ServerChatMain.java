package ru.lab.chat;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Module;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.lab.chat.config.ServerModule;
import ru.lab.chat.controller.ServerController;
import ru.lab.chat.controller.ServerControllerImpl;

import javax.inject.Inject;
import java.util.List;

public class ServerChatMain extends GuiceApplication {
    @Inject
    private GuiceFXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = fxmlLoader.load(getClass().getResource("/view/application.fxml")).getRoot();
        primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.show();
    }

    @Override
    public void init(List<Module> list) throws Exception {
        list.add(new ServerModule());
    }

    @Override
    public void stop() throws Exception {
        ServerControllerImpl.getCurrentServer().closeAll();
    }
}
