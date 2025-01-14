package prisoner.prisonermanager.pages;

import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.Core;

import java.util.ArrayList;
import java.util.Map;

public class Home {
    public Home(HBox root, VBox roomContainer, ArrayList<Object> previousPage, ArrayList<Object> nextPage) throws JsonProcessingException {
        roomContainer.getChildren().add(new Rooms(root, previousPage, nextPage));
    }
}
