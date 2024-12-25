package prisoner.prisonermanager;

import backend.Database;
import backend.Prisoner;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.components.Login;

import java.sql.SQLException;
import java.util.ArrayList;

public class AuthController {
    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    VBox root;

    @FXML
    public void initialize() {
        System.out.println(app);
        root.getChildren().add(new Login(root, app));
    }

}
