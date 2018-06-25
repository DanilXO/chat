package ru.lab.chat;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Module;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.lab.chat.config.ChatModule;

import javax.inject.Inject;
import java.util.List;

public class ClientChatMain extends GuiceApplication {
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
    public void init(List<Module> list) {
        list.add(new ChatModule());
    }
}
