package prisoner.prisonermanager.pages;

import backend.Server;
import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.Core;

import java.util.ArrayList;
import java.util.Map;

public class Rooms extends GridPane {
    public Rooms(HBox root, ArrayList<Object> previousPage, ArrayList<Object> nextPage) throws JsonProcessingException {
        setMinWidth(root.getWidth());
        setVgap(10);
        setHgap(10);
        String rawRoomData = Server.getRooms(User.getToken());
        Map<String, Object> roomData = new ObjectMapper().readValue(rawRoomData, Map.class);
        ArrayList<Map<String, Object>> rooms = (ArrayList<Map<String, Object>>) roomData.get("rooms");

        for(int i = 0; i < rooms.size(); i++) {
            Map<String, Object> room = rooms.get(i);
            Button roomButton = new Button((String) room.get("name"));
            roomButton.getStyleClass().add("btn-primary");
            add(roomButton, 0, i);

            roomButton.setOnAction(e -> {
                try {
                    previousPage.add(this);
                    nextPage.add(new Prisoners(root, previousPage, nextPage, room));
                    VBox roomContainer = (VBox) Core.root.lookup("#roomContainer");
                    if(roomContainer != null) {
                        roomContainer.getChildren().clear();
                        roomContainer.getChildren().add((Node) nextPage.getLast());
                    }


                } catch (JsonProcessingException jsonProcessingException) {
                    jsonProcessingException.printStackTrace();
                }
            });
        }
        System.out.println(rawRoomData);

    }
}
