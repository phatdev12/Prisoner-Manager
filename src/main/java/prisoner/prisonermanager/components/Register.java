package prisoner.prisonermanager.components;

import backend.Server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.App;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Register extends VBox {
    public Register(VBox root, App app) {
        super();
        Label label = new Label("Đăng ký");
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        // make label to be centered
        label.getStyleClass().add("center");
        label.setFont(new javafx.scene.text.Font(30));
        VBox container = new VBox();
        // make all the children nodes of the container to be centered
        container.getStyleClass().add("center");
        container.setAlignment(javafx.geometry.Pos.CENTER);
        container.setSpacing(10);
        VBox.setMargin(container, new javafx.geometry.Insets(100, 300, 100, 300));
        container.getChildren().add(label);

        VBox form = new VBox();
        Label usernameLabel = new Label("Tên đăng nhập");
        usernameLabel.getStyleClass().add("form-label");
        TextField username = new TextField();
        username.setPromptText("Nhập tên đăng nhập");
        username.getStyleClass().add("form-control");

        VBox usernameContainer = new VBox();
        usernameContainer.getChildren().addAll(usernameLabel, username);
        usernameContainer.setSpacing(5);

        Label passwordLabel = new Label("Mật khẩu");
        passwordLabel.getStyleClass().add("form-label");
        PasswordField password = new PasswordField();
        password.setPromptText("Nhập mật khẩu");
        password.getStyleClass().add("form-control");

        VBox passwordContainer = new VBox();
        passwordContainer.getChildren().addAll(passwordLabel, password);
        passwordContainer.setSpacing(5);

        VBox buttonContainer = new VBox();
        Button registerButton = new Button("Đăng ký");
        registerButton.setPrefWidth(Double.MAX_VALUE);
        registerButton.getStyleClass().add("btn-primary");
        registerButton.getStyleClass().add("form-btn");
        registerButton.setOnAction(e -> {
            String user = username.getText();
            String pass = password.getText();

            String rawData = Server.register(user, pass);
            System.out.println(rawData);
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode data = mapper.readTree(rawData);
                if(data.get("token") != null) {
                    System.out.println("Register successful!");
                    File file = new File("src/main/resources/cookie.json");
                    Map<String, Object> map = new ObjectMapper().readValue(file, Map.class);
                    mapper.writeValue(file, data);
                    System.out.println("Token: " + data.get("token").asText());
                    app.emit("verify");
                }
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        form.getStyleClass().add("form");
        buttonContainer.getStyleClass().add("button-container");
        VBox.setMargin(buttonContainer, new javafx.geometry.Insets(20, 0, 0, 0));
        buttonContainer.getChildren().add(registerButton);

        HBox loginText = new HBox();
        Label registerLabel = new Label("Quay trở lại");
        registerLabel.getStyleClass().add("form-label");
        Button loginButton = new Button("Đăng nhập");
        loginButton.getStyleClass().add("transparent-btn");
        loginButton.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().add(new Login(root, app));
        });
        loginText.getChildren().addAll(registerLabel, loginButton);
        loginText.getStyleClass().add("center");
        loginText.setAlignment(Pos.CENTER);
        loginText.getStyleClass().add("register-text");
        form.getChildren().addAll(usernameContainer, passwordContainer, buttonContainer, loginText);
        container.getChildren().add(form);
        getChildren().add(container);

    }
}
