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
import javafx.scene.layout.Pane;
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
    private Button nextButton;

    @FXML
    private HBox root;

    @FXML
    public void initialize() {
        db = new Database();
        try {
            db.getRoom();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<backend.Room> rooms = db.rooms;
        GridPane btnContainer = new GridPane();
        btnContainer.setMinWidth(root.getWidth());
        btnContainer.setVgap(10);
        btnContainer.setHgap(10);
        btnContainer.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));

        int counter = 0;
        for (backend.Room room : rooms) {
            Button button = new Button(room.getName());
            button.getStyleClass().add("btn-primary");
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
                    Label name = new Label(prisoner.getName());
                    name.setMinWidth(100);
                    Label type = new Label(prisoner.getType());
                    type.setPrefWidth(100);

                    hbox.setSpacing(10);
                    HBox.setMargin(hbox, new javafx.geometry.Insets(10, 10, 10, 10));
                    hbox.getChildren().add(name);
                    HBox splitPane = new HBox();
                    Pane space = new Pane();

                    space.setMinWidth(root.getWidth() - name.getMinWidth() - type.getPrefWidth());

                    splitPane.getChildren().add(type);
                    splitPane.getChildren().add(space);

                    Button xemthem = new Button("Xem thêm");
                    xemthem.getStyleClass().add("btn-primary");

                    xemthem.setOnAction(event -> {
                        detail.getChildren().clear();
                        detail.getChildren().add(new Label("Name: " + prisoner.getName()));
                        detail.getChildren().add(new Label("Type: " + prisoner.getType()));
                        detail.getChildren().add(new Label("Age: " + prisoner.getAge()));
                        detail.getChildren().add(new Label("Room ID: " + prisoner.getRoomID()));
                        detail.getChildren().add(new Label("Start Date: " + prisoner.getStartDate().toString()));
                        detail.getChildren().add(new Label("End Date: " + prisoner.getEndDate().toString()));
                    });

                    splitPane.getChildren().add(xemthem);
                    hbox.getChildren().add(splitPane);
                    container.getChildren().add(hbox);
                }
                scrollPane.setContent(container);
                roomContainer.getChildren().clear();
                roomContainer.getChildren().add(scrollPane);
                previousPage.add(scrollPane);
            });
            btnContainer.add(button, counter % 4, counter / 4);
            counter++;
            previousPage.add(btnContainer);
        }

        roomContainer.getChildren().add(btnContainer);
        previousButton.setOnAction(e -> {
            if(previousPage.isEmpty()) return;
            roomContainer.getChildren().clear();
            nextPage.add(previousPage.getLast());
            previousPage.removeLast();
            roomContainer.getChildren().add((Node) previousPage.getLast());
        });

        nextButton.setOnAction(e -> {
            if(nextPage.isEmpty()) return;
            roomContainer.getChildren().clear();
            previousPage.add(nextPage.getLast());
            roomContainer.getChildren().add((Node) nextPage.getLast());
            nextPage.removeLast();
        });
    }
}