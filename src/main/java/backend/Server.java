package backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Server {
    private static final String api = "http://localhost:3000";
    private static  final HttpClient client = HttpClient.newHttpClient();

    public static String register(String username, String password) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String login(String username, String password) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String verify(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/auth/verify"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Prisoner " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // test
        // System.out.println(verify("cGhhc2FDhA==-cGhhc2FDhA==-MTczNTEyMzAxODMxMw=="));
    }

}
