package backend;

import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;

public class WebSocket {
    private static String url = "ws://localhost:3000";
    public static HttpClient client = HttpClient.newHttpClient();
    public static java.net.http.WebSocket ws;

    public static void connect() {
        ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url+"?userId="+User.getId()), new WebSocketEvent())
                .join();
    }

    public static void sendNotification(String message) {
        if(ws != null) {
            ws.sendText(message, true);
        }
    }
}
