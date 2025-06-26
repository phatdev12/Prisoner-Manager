package prisoner.prisonermanager;

import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.pages.*;

import java.util.ArrayList;
import java.util.Map;

public class AppController {
    public Button chatAI;
    private ArrayList<Object> previousPage = new ArrayList<>();
    private ArrayList<Object> nextPage = new ArrayList<>();
//    private ChatService chatService;
//    private SocketService socketService;
//    private ObservableList<ChatRoom> chatRooms;
//    private ChatRoom currentChatRoom;

    @FXML
    private Button adminBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private VBox welcome;

    @FXML
    private VBox roomContainer;

    @FXML
    private VBox detail;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button notice;

    @FXML
    private HBox root;

    @FXML
    public void initialize() throws JsonProcessingException {
        adminBtn.setVisible(User.getIsAdmin());
        notice.setVisible(User.getIsAdmin());
        String rawStaffData = backend.Server.getStaff(User.getToken(), User.getId());
        Map<String, Object> staffData = new com.fasterxml.jackson.databind.ObjectMapper().readValue(rawStaffData, Map.class);
        ArrayList<Map<String, Object>> staffs = (ArrayList<Map<String, Object>>) staffData.get("staff");
        welcome.setAlignment(Pos.CENTER_LEFT);
        Label welcomeLabel = new Label("Xin chào ");
        welcomeLabel.getStyleClass().add("welcome-label");
        Label roleLabel = new Label("");
        roleLabel.getStyleClass().add("role-label");

        if(!staffs.isEmpty()) {
            Map<String, Object> staff = (Map<String, Object>) staffs.getFirst();
            welcomeLabel.setText("Xin chào " + staff.get("name"));
            if(User.getIsAdmin()) {
                roleLabel.setText("Quản trị viên");
            } else {
                roleLabel.setText(staff.get("role").toString());
            }
        } else {
            welcomeLabel.setText("Xin chào " + User.getUsername());
            if (User.getIsAdmin()) {
                roleLabel.setText("Quản trị viên");
            } else {
                roleLabel.setText("Nhân viên");
            }
        }

        welcome.getChildren().addAll(welcomeLabel, roleLabel);

        new Home(root, roomContainer, previousPage, nextPage);

        notice.setOnAction(e -> {
            detail.getChildren().clear();
            detail.getChildren().add(new Notice());
        });
        adminBtn.setOnAction(e -> {
            previousPage.add(roomContainer.getChildren().getFirst());
            roomContainer.getChildren().clear();
            new Manage(roomContainer, previousPage, nextPage);

        });

        chatAI.setOnAction(e -> {
           previousPage.add(roomContainer.getChildren().getFirst());
           roomContainer.getChildren().clear();
           new AIChatPage(roomContainer, previousPage, nextPage);
        });

        homeBtn.setOnAction(e -> {
            roomContainer.getChildren().clear();
            previousPage.clear();
            nextPage.clear();
            try {
                new Home(root, roomContainer, previousPage, nextPage);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });
        previousButton.setOnAction(e -> {
            if(previousPage.isEmpty()) return;
            roomContainer.getChildren().clear();
            nextPage.add(previousPage.getLast());
            roomContainer.getChildren().add((Node) previousPage.getLast());
            previousPage.removeLast();

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