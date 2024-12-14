package prisoner.prisonermanager;

import backend.Database;
import backend.Prisoner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;

public class AppController {
    Database db;
    @FXML
    private Label welcomeText;

    @FXML
    private VBox container;

    @FXML
    public void initialize() {
        container.setSpacing(10);
        container.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        db = new Database();
        try {
            db.getPrisoner();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Prisoner> prisoners = db.prisoners;
        for (Prisoner prisoner : prisoners) {
            Button button = new Button(prisoner.getName());
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            HBox.setMargin(hbox, new javafx.geometry.Insets(10, 10, 10, 10));
            hbox.getChildren().add(button);
            hbox.getChildren().add(new Label(prisoner.getType()));
            hbox.getChildren().add(new Label(prisoner.getAge() + ""));
            hbox.getChildren().add(new Label(prisoner.getRoomID() + ""));
            hbox.getChildren().add(new Label(prisoner.getStartDate().toString()));
            hbox.getChildren().add(new Label(prisoner.getEndDate().toString()));

            container.getChildren().add(hbox);
        }

        System.out.println("Hello");

    }


}