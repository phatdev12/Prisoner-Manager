package prisoner.prisonermanager.components;

import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.Core;
import prisoner.prisonermanager.pages.Prisoners;

import java.util.ArrayList;
import java.util.Map;

public class PrisonerCard extends HBox {
    public PrisonerCard(HBox root, Map<String, Object> prisoner, ArrayList<Object> previousPage, ArrayList<Object> nextPage, Map<String, Object> room) {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);
        getStyleClass().add("prisoner-card");
        setMinWidth(Core.root.lookup("#roomContainer").getLayoutBounds().getWidth() - 80);
        Label prisonerName = new Label((String) prisoner.get("name"));
        prisonerName.getStyleClass().add("prisoner-name");
        Button prisonerButton = new Button("Xem chi tiết");
        prisonerButton.getStyleClass().add("btn-primary");
        prisonerButton.setOnAction(e -> {
            System.out.println(prisoner);
            VBox detail = (VBox) Core.root.lookup("#detail");
            detail.getChildren().clear();
            detail.getChildren().add(new PrisonerDetail(prisoner));
        });

        if(User.getIsAdmin()) {
            Button deleteButton = new Button("Xóa");
            deleteButton.getStyleClass().add("btn-danger");
            deleteButton.setOnAction(e -> {
                String rawResponse = backend.Server.deletePrisoner(User.getToken(), prisoner.get("id").toString());
                Map<String, Object> response = null;
                try {
                    response = new ObjectMapper().readValue(rawResponse, Map.class);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }

                if((boolean) response.get("success")) {
                    VBox roomContainer = (VBox) Core.root.lookup("#roomContainer");
                    roomContainer.getChildren().clear();
                    try {
                        roomContainer.getChildren().add(new Prisoners(root, previousPage, nextPage, room));
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            });

            Button editButton = new Button("Sửa");
            editButton.getStyleClass().add("btn-primary");
            editButton.setOnAction(e -> {
                VBox detail = (VBox) Core.root.lookup("#detail");
                detail.getChildren().clear();
                detail.getChildren().add(new PrisonerForm(root, prisoner, room, previousPage, nextPage));
            });

            prisonerName.setPrefWidth(Core.root.lookup("#roomContainer").getLayoutBounds().getWidth() - 340);
            getChildren().addAll(prisonerName, prisonerButton, editButton, deleteButton);
        } else {
            prisonerName.setPrefWidth(Core.root.lookup("#roomContainer").getLayoutBounds().getWidth() - 200);
            getChildren().addAll(prisonerName, prisonerButton);
        }
    }
}
