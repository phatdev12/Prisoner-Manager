package prisoner.prisonermanager;

public class Event {
    private final String message;

    public Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
