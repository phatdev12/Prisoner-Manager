package prisoner.prisonermanager.components;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.App;

import java.net.URL;
import java.util.Objects;

public class FeatureCard extends VBox {
    public FeatureCard(VBox roomContainer, String title, String description, String iconPath) {
        getStyleClass().add("feature-card");
        setSpacing(10);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("feature-title");

        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("feature-description");

        URL iconUrl = App.class.getResource(iconPath);
        if (iconUrl == null) {
            throw new IllegalArgumentException("Icon path not found: " + iconPath);
        }
        String path = iconUrl.toExternalForm();
        ImageView icon = new ImageView(path);
        icon.setPreserveRatio(true);
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        icon.getStyleClass().add("feature-icon");

        getChildren().addAll(icon, titleLabel, descriptionLabel);
        setOnMouseClicked(event -> {
            roomContainer.getChildren().clear();
        });
    }
}
