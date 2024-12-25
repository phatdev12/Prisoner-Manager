package prisoner.prisonermanager.components;

import backend.Server;
import com.fasterxml.jackson.core.JsonProcessingException;
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

public class Login extends VBox {
    public Login(VBox root, App app) {
        super();
        Label label = new Label("Đăng nhập");
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

        Button loginButton = new Button("Đăng nhập");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.getStyleClass().add("btn-primary");
        loginButton.getStyleClass().add("form-btn");
        loginButton.setOnAction(e -> {
            String user = username.getText();
            String pass = password.getText();

            String rawData = Server.login(user, pass);
            System.out.println(rawData);
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode data = mapper.readTree(rawData);
                if(data.get("token") != null) {
                    System.out.println("Login successful!");
                    // write token to cookie.json
                    File file = new File("src/main/resources/cookie.json");
                    mapper.writeValue(file, data);
                    System.out.println("Token: " + data.get("token").asText());
                    app.emit("verify");
                } else {
                    System.out.println("Login failed!");
                }
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        form.getStyleClass().add("form");

        Button forgot = new Button("Quên mật khẩu");
        forgot.getStyleClass().add("transparent-btn");
        forgot.getStyleClass().add("forgot-btn");
        forgot.setOnAction(e -> {
            System.out.println("Forgot password");
        });

        HBox registerText = new HBox();
        Label registerLabel = new Label("Chưa có tài khoản?");
        registerLabel.getStyleClass().add("form-label");
        Button registerButton = new Button("Đăng ký");
        registerButton.getStyleClass().add("transparent-btn");
        registerButton.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().add(new Register(root, app));
        });
        registerText.getChildren().addAll(registerLabel, registerButton);
        registerText.getStyleClass().add("center");
        registerText.setAlignment(Pos.CENTER);
        registerText.getStyleClass().add("register-text");

        form.getChildren().addAll(usernameContainer, passwordContainer, forgot, loginButton, registerText);
        container.getChildren().add(form);
        getChildren().add(container);
    }
}
