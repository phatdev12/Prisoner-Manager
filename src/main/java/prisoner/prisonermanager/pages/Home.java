package prisoner.prisonermanager.pages;

import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.Core;
import prisoner.prisonermanager.components.FeatureCard;

import java.util.ArrayList;
import java.util.Map;

public class Home extends GridPane {
    public Home(HBox root, VBox roomContainer, ArrayList<Object> previousPage, ArrayList<Object> nextPage) throws JsonProcessingException {
        setMinWidth(root.getPrefWidth());
        setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        setVgap(10);
        setHgap(10);

        FeatureCard prisonersCard = new FeatureCard(roomContainer, "Tù nhân", "Xem danh sách tù nhân", "assets/prisoner.png");
        prisonersCard.setOnMouseClicked(event -> {
            try {
                previousPage.add(this);
                roomContainer.getChildren().clear();
                new RoomsPage(root, roomContainer, previousPage, nextPage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        FeatureCard staffCard = new FeatureCard(roomContainer, "Nhân sự", "Quản lý nhân sự", "assets/staff.png");
        staffCard.setOnMouseClicked(event -> {
            previousPage.add(this);
            roomContainer.getChildren().clear();
            try {
                new StaffListPage(roomContainer);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        add(prisonersCard, 0, 0);
        add(staffCard, 1, 0);
        roomContainer.getChildren().add(this);

    }
}
