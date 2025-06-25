package prisoner.prisonermanager.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Map;

public class StaffCard extends HBox {
    public StaffCard(Map<String, Object> staff) {
        setSpacing(10);
        setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        String name = staff.get("name").toString();
        String position = staff.get("roLe").toString();

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label positionLabel = new Label(position);

        getChildren().addAll(nameLabel, positionLabel);

        // Set on-click action if needed
        setOnMouseClicked(event -> {
            // Handle click event, e.g., open staff details page
        });
    }
}
