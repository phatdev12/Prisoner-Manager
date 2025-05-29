package prisoner.prisonermanager.pages;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Manage extends VBox {
    public Manage(VBox roomContainer, ArrayList<Object> previousPage, ArrayList<Object> nextPage) {
        HBox staffCard = new HBox();
        staffCard.getStyleClass().add("prisoner-card");
        Label staffTitle = new Label("Quản lý nhân sự");
        staffTitle.setMinWidth(roomContainer.getLayoutBounds().getWidth() - 160);
        staffTitle.getStyleClass().add("prisoner-name");
        Button staffButton = new Button("Truy cập");
        staffButton.getStyleClass().add("btn-primary");
        staffButton.setOnAction(e -> {
            previousPage.add(this);
            roomContainer.getChildren().clear();
            new StaffManage(roomContainer);
        });
        staffCard.getChildren().addAll(staffTitle, staffButton);

        HBox prisonerCard = new HBox();
        prisonerCard.getStyleClass().add("prisoner-card");
        Label prisonerTitle = new Label("Quản lý tù nhân");
        prisonerTitle.setMinWidth(roomContainer.getLayoutBounds().getWidth() - 160);
        prisonerTitle.getStyleClass().add("prisoner-name");
        Button prisonerButton = new Button("Truy cập");
        prisonerButton.getStyleClass().add("btn-primary");
        prisonerCard.getChildren().addAll(prisonerTitle, prisonerButton);
        prisonerButton.setOnAction(e -> {
            previousPage.add(this);
            roomContainer.getChildren().clear();
            new PrisonerManage(roomContainer);
        });

        setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        setSpacing(10);
        getChildren().addAll(staffCard, prisonerCard);
        roomContainer.getChildren().add(this);
    }

}
