package prisoner.prisonermanager;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.components.Login;

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
