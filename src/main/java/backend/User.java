package backend;

public class User {
    private static String id;
    private static String username;
    private static Boolean isAdmin;
    private static String token;

    public static void init(String id, String username, String token, Boolean isAdmin) {
        User.id = id;
        User.username = username;
        User.isAdmin = isAdmin;
        User.token = token;
    }

    public static String getId() {
        return id;
    }

    public static String getUsername() {
        return username;
    }

    public static Boolean getIsAdmin() {
        return isAdmin;
    }

    public static String getToken() {
        return token;
    }

}
