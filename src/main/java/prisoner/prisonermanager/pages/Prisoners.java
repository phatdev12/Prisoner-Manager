package prisoner.prisonermanager.pages;

import backend.Prisoner;
import backend.Server;
import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.Core;
import prisoner.prisonermanager.components.PrisonerCard;

import java.util.ArrayList;
import java.util.Map;


public class Prisoners extends ScrollPane {
    public Prisoners(HBox root, ArrayList<Object> previousPage, ArrayList<Object> nextPage, Map<String, Object> room) throws JsonProcessingException, JsonProcessingException {
        VBox roomContainer = (VBox) Core.root.lookup("#roomContainer");
        roomContainer.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));
        setMinWidth(root.getWidth());
        String rawPrisonerData = Server.getPrisoners(User.getToken(), room.get("id").toString());
        Map<String, Object> prisonerData = new ObjectMapper().readValue(rawPrisonerData, Map.class);
        ArrayList<Map<String, Object>> prisoners = (ArrayList<Map<String, Object>>) prisonerData.get("prisoner");

        VBox prisonerContainer = new VBox();
        prisonerContainer.setSpacing(10);
        prisonerContainer.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        for(int i = 0; i < prisoners.size(); i++) {
            Map<String, Object> prisoner = prisoners.get(i);
            prisonerContainer.getChildren().add(new PrisonerCard(root, prisoner, previousPage, nextPage, room));
        }
        setContent(prisonerContainer);
    }

}
