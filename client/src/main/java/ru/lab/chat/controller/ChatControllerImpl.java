package ru.lab.chat.controller;

import com.cathive.fx.guice.FxApplicationThread;
import com.google.common.base.Strings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.lab.chat.model.Client;
import ru.lab.chat.service.RememberMeService;

import javax.inject.Inject;
import java.util.List;

public class ChatControllerImpl implements ChatController{
    @FXML
    private TextField appNameTextField;
    @FXML
    private TextArea appMsgTextArea;
    @FXML
    private TextArea appChatTextArea;

    @FXML
    private ListView<String> appClientsList;

    @FXML
    private Button appEnterButton;
    @FXML
    private Button appSendButton;
    @FXML
    private CheckBox rememberCheckbox;

    private Stage primaryStage;
    private Client client;
    private RememberMeService rememberMeService;
    @Inject
    public ChatControllerImpl(Stage primaryStage, RememberMeService rememberMeService) {
        this.primaryStage = primaryStage;
        this.rememberMeService = rememberMeService;
        System.out.println(rememberMeService.getLogin());
        //appNameTextField = new TextField();
       // autoLogin();
    }
    @FXML
    public void initialize() {
        autoLogin();
    }


    /**
     * Приватный метод для авто-входа в чат
     */
    private void autoLogin(){
        if (rememberMeService.existLogin()) {
            appNameTextField.setText(rememberMeService.getLogin());
            client = new Client(rememberMeService.getLogin(), this);
            appEnterButton.setDisable(true);
            appNameTextField.setEditable(false);
            rememberCheckbox.setSelected(true);
            rememberCheckbox.setDisable(true);
        }
    }

    /**
     * Обработчик нажатия кнопки войти.
     */
    @FXML
    public void handleEnter(){
        String name = appNameTextField.getText();
        if(name == null)
            name = "random";
        client = new Client(name, this);
        appEnterButton.setDisable(true);
        appNameTextField.setEditable(false);
        rememberCheckbox.setDisable(true);
        if (rememberCheckbox.isSelected()){
            rememberMeService.saveClient(client);
        }
    }

    /**
     * Обработчик нажатия кнопки отправки сообщения.
     */
    public void handleSend(){
        if(client != null) {
            client.println(appMsgTextArea.getText());
            appMsgTextArea.setText("");
        }
    }

    /**
     * Обработчик нажатия кнопки выйти.
     */
    public void handleExit(){
        if(client != null)
            client.exit();
        appEnterButton.setDisable(false);
        appNameTextField.setEditable(true);
        rememberCheckbox.setDisable(false);
    }

    /**
     * Выводит полученное сообщение в окно чата.
     * @param msg - полученное сообщение.
     */
    public void getMsg(String msg){
        appChatTextArea.appendText(msg+"\n");
    }

    /**
     * Обновляет список клиентов.
     * @param list - полученный список клиентов.
     */
    public void updateClientsList(List<String> list){
        appClientsList.setItems(FXCollections.observableArrayList(list));
    }
}
