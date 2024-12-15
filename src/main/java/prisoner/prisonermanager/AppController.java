package prisoner.prisonermanager;

import backend.Database;
import backend.Prisoner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;

public class AppController {
    Database db;
    private ArrayList<VBox> previousPage = new ArrayList<VBox>();
    private ArrayList<VBox> nextPage = new ArrayList<VBox>();
    @FXML
    private Label welcomeText;

    @FXML
    private VBox roomContainer;

    @FXML
    public void initialize() {
        db = new Database();
        try {
            db.getRoom();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<backend.Room> rooms = db.rooms;
        for (backend.Room room : rooms) {
            Button button = new Button(room.getName());
            button.setOnAction(e -> {
                try {
                    db.getPrisoner(room.getId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                ArrayList<Prisoner> prisoners = db.prisoners;
                VBox container = new VBox();
                container.setSpacing(10);
                container.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
                for (Prisoner prisoner : prisoners) {
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    HBox.setMargin(hbox, new javafx.geometry.Insets(10, 10, 10, 10));
                    hbox.getChildren().add(new Label(prisoner.getName()));
                    hbox.getChildren().add(new Label(prisoner.getType()));
                    hbox.getChildren().add(new Label(prisoner.getAge() + ""));
                    hbox.getChildren().add(new Label(prisoner.getRoomID() + ""));
                    hbox.getChildren().add(new Label(prisoner.getStartDate().toString()));
                    hbox.getChildren().add(new Label(prisoner.getEndDate().toString()));
                    container.getChildren().add(hbox);
                }
                roomContainer.getChildren().clear();
                roomContainer.getChildren().add(container);
            });
            roomContainer.getChildren().add(button);
        }


//        container.setSpacing(10);
//        container.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
//        db = new Database();
//        try {
//            db.getPrisoner();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        ArrayList<Prisoner> prisoners = db.prisoners;
//
//        for (Prisoner prisoner : prisoners) {
//            Button button = new Button(prisoner.getName());
//            HBox hbox = new HBox();
//            hbox.setSpacing(10);
//            HBox.setMargin(hbox, new javafx.geometry.Insets(10, 10, 10, 10));
//            hbox.getChildren().add(button);
//            hbox.getChildren().add(new Label(prisoner.getType()));
//            hbox.getChildren().add(new Label(prisoner.getAge() + ""));
//            hbox.getChildren().add(new Label(prisoner.getRoomID() + ""));
//            hbox.getChildren().add(new Label(prisoner.getStartDate().toString()));
//            hbox.getChildren().add(new Label(prisoner.getEndDate().toString()));
//
//            container.getChildren().add(hbox);
//        }
//
//        System.out.println("Hello");
    }
}