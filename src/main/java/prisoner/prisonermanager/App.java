package prisoner.prisonermanager;

import backend.Server;
import backend.User;
import backend.WebSocket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private final List<prisoner.prisonermanager.EventListener> listeners = new ArrayList<>();
    public static Stage primaryStage;
    public void addListener(prisoner.prisonermanager.EventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(prisoner.prisonermanager.EventListener listener) {
        listeners.remove(listener);
    }

    public void emit(String message) throws IOException {
        Event event = new Event(message);
        for(prisoner.prisonermanager.EventListener listener: listeners) {
            listener.onEvent(event);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        Middleware middleware = new Middleware(stage, this);
        addListener(middleware);

        File file = new File(Paths.get("src/main/resources/cookie.json").toUri());
        Map<String, Object> map = new ObjectMapper().readValue(file, Map.class);
        if (map.get("token") == "") {
            loadAuth(stage);
        } else {
            String token = (String) map.get("token");
            String rawVerify = (String) Server.verify(token);
            if(Objects.equals(rawVerify, "Auth failed")) {
                loadAuth(stage);
            } else {
                WebSocket.connect();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode verify = mapper.readTree(rawVerify);

                if(verify.has("success") && verify.get("success").asBoolean()) {
                    User.init(verify.get("id").asText(), verify.get("username").asText(), verify.get("token").asText(), Boolean.valueOf(verify.get("admin").asBoolean()));
                    loadApp(stage);
                }
                else loadAuth(stage);
            }
        }

        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if(WebSocket.ws != null) {
            WebSocket.ws.sendClose(java.net.http.WebSocket.NORMAL_CLOSURE, "Close").join();
        }
        super.stop();
    }

    public void loadAuth(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Auth.fxml"));
        AuthController controller = new AuthController();
        controller.setApp(this);
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
    }

    public void loadApp(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("App.fxml"));
        Parent core = fxmlLoader.load();
        Core.setRoot(core);

        Scene scene = new Scene(core, 1200, 700);
        stage.setTitle("Quản lý nhà tù");
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}