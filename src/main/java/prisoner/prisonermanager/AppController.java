package prisoner.prisonermanager;

import backend.Database;
import backend.Prisoner;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;

public class AppController {
    Database db;
    private ArrayList<Object> previousPage = new ArrayList<>();
    private ArrayList<Object> nextPage = new ArrayList<>();
    @FXML
    private Label welcomeText;

    @FXML
    private VBox roomContainer;

    @FXML
    private VBox detail;

    @FXML
    private Button previousButton;

    @FXML
    public void initialize() {
        db = new Database();
        try {
            db.getRoom();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<backend.Room> rooms = db.rooms;
        HBox btnContainer = new HBox();
        for (backend.Room room : rooms) {
            Button button = new Button(room.getName());
            button.setOnAction(e -> {
                try {
                    db.getPrisoner(room.getId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                ArrayList<Prisoner> prisoners = db.prisoners;
                ScrollPane scrollPane = new ScrollPane();
                VBox container = new VBox();
                container.setSpacing(10);
                container.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
                for (Prisoner prisoner: prisoners) {
                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    HBox.setMargin(hbox, new javafx.geometry.Insets(10, 10, 10, 10));
                    hbox.getChildren().add(new Label(prisoner.getName()));
                    SplitPane splitPane = new SplitPane();
                    splitPane.getItems().add(new Label(prisoner.getType()));
                    Button xemthem = new Button("Xem thêm");

                    xemthem.setOnAction(event -> {
                        detail.getChildren().clear();
                        detail.getChildren().add(new Label("Name: " + prisoner.getName()));
                        detail.getChildren().add(new Label("Type: " + prisoner.getType()));
                        detail.getChildren().add(new Label("Age: " + prisoner.getAge()));
                        detail.getChildren().add(new Label("Room ID: " + prisoner.getRoomID()));
                        detail.getChildren().add(new Label("Start Date: " + prisoner.getStartDate().toString()));
                        detail.getChildren().add(new Label("End Date: " + prisoner.getEndDate().toString()));
                    });

                    splitPane.getItems().add(xemthem);
                    hbox.getChildren().add(splitPane);
                    container.getChildren().add(hbox);
                }
                scrollPane.setContent(container);
                roomContainer.getChildren().clear();
                roomContainer.getChildren().add(scrollPane);
                previousPage.add(container);
            });
            btnContainer.getChildren().add(button);
            previousPage.add(btnContainer);
        }
        roomContainer.getChildren().add(btnContainer);


        previousButton.setOnAction(e -> {
            roomContainer.getChildren().clear();
            previousPage.removeLast();
            roomContainer.getChildren().add((Node) previousPage.get(previousPage.size() - 1));
        });

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