package backend;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIChat extends Application {
    private static final String API_KEY = "AIzaSyAD3OnDauJjGd6pK8-rMGuZRG_kmH1NZAM";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Gson gson = new Gson();

    private static String extractBody(String responseBody) {
        try {
            JsonObject json = gson.fromJson(responseBody, JsonObject.class);
            JsonArray candidates = json.getAsJsonArray("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                JsonArray parts = firstCandidate.getAsJsonObject("content").getAsJsonArray("parts");

                if (parts != null && !parts.isEmpty()) {
                    return parts.get(0).getAsJsonObject().get("text").getAsString();
                }
            }
        } catch (Exception e) {
            return "Error parsing Gemini response.";
        }
        return "No response from Gemini.";
    }
    public static String callAIChat(String userMessage) throws Exception {
        JsonObject requestJson = getJsonObject(userMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return extractBody(response.body());
    }

    private static JsonObject getJsonObject(String userMessage) {
        JsonArray contents = new JsonArray();
        JsonObject userContent = new JsonObject();
        JsonObject contentItem = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject textPart = new JsonObject();

        userContent.addProperty("role", "user");
        textPart.addProperty("text", userMessage);
        parts.add(textPart);
        contentItem.add("parts", parts);
        contents.add(contentItem);

        JsonObject requestJson = new JsonObject();
        requestJson.add("contents", contents);
        return requestJson;
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =new FXMLLoader(AIChat.class.getResource("/org/example/finaltermjava_client/chatAI.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("AI Chat Client");
        stage.show();
    }
}
