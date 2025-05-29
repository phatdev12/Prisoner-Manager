package prisoner.prisonermanager.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RoomsPage extends VBox {
    public RoomsPage(HBox root, VBox roomContainer, ArrayList<Object> previousPage, ArrayList<Object> nextPage) throws JsonProcessingException {
        roomContainer.getChildren().add(new Rooms(root, previousPage, nextPage));
    }
}
