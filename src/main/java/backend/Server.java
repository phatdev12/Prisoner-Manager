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
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRooms(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/rooms"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    //public
    public static void main(String[] args) {
        // test
        // System.out.println(verify("cGhhc2FDhA==-cGhhc2FDhA==-MTczNTEyMzAxODMxMw=="));
    }

    public static String getPrisoners(String token, String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/prisoner/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    public static String addPrisoner(String token, String name, String age, String room, String type, String startDate, String endDate) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/prisoner"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"" + name + "\", \"age\": " + age + ", \"roomID\": " + room + ", \"type\": \"" + type + "\", \"startDate\": \"" + startDate + "\", \"endDate\": \"" + endDate + "\"}"))
                .build();
        System.out.println("{\"name\": \"" + name + "\", \"age\": " + age + ", \"room\": " + room + ", \"type\": \"" + type + "\", \"startDate\": \"" + startDate + "\", \"endDate\": \"" + endDate + "\"}");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    public static String deletePrisoner(String token, String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/prisoner/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    public static String updatePrisoner(String token, String id, String name, String age, String room, String type, String startDate, String endDate) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/prisoner/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .PUT(HttpRequest.BodyPublishers.ofString("{\"name\": \"" + name + "\", \"age\": " + age + ", \"roomID\": " + room + ", \"type\": \"" + type + "\", \"startDate\": \"" + startDate + "\", \"endDate\": \"" + endDate + "\"}"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getStaff(String token, String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + "/staff/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return null;
        }
    }
}
