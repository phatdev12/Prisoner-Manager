package prisoner.prisonermanager;

import backend.Server;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Middleware implements EventListener {
    private final Stage stage;
    private final App app;

    public Middleware(Stage stage, App app) {
        this.stage = stage;
        this.app = app;
    }

    @Override
    public void onEvent(Event event) throws IOException {
        if(event.getMessage().equals("verify")) {
            File file = new File("src/main/resources/cookie.json");
            Map<String, Object> map = new ObjectMapper().readValue(file, Map.class);
            if(map.get("token") != "") {
                String token = (String) map.get("token");
                String rawVerify = (String) Server.verify(token);
                if(rawVerify == null) {
                    app.loadAuth(stage);
                    return;
                }
                ObjectMapper mapper = new ObjectMapper();
                JsonNode verify = mapper.readTree(rawVerify);

                if(verify.get("success").asBoolean()) {
                    app.loadApp(stage);
                }
            }
        }
    }
}
