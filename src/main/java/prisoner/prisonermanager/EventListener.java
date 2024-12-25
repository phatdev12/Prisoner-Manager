package prisoner.prisonermanager;

import java.io.IOException;

public interface EventListener {
    void onEvent(Event event) throws IOException;
}
