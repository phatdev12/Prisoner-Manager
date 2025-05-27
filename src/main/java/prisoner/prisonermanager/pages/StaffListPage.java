package prisoner.prisonermanager.pages;

import backend.Server;
import backend.Staff;
import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.components.StaffCard;

import java.util.ArrayList;
import java.util.Map;

public class StaffListPage extends ScrollPane {
    public StaffListPage(VBox roomContainer) throws JsonProcessingException {
        VBox container = new VBox();
        container.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        String rawStaffData = Server.getStaffList(User.getToken());
        Map<String, Object> staffData = new ObjectMapper().readValue(rawStaffData, Map.class);
        ArrayList<Map<String, Object>> staffList = (ArrayList<Map<String, Object>>) staffData.get("users");
        if(staffList.isEmpty()) {
            Label noStaff = new Label("No staff found");
            container.getChildren().add(noStaff);
            setContent(container);
            roomContainer.getChildren().add(noStaff);
            return;
        }
        ArrayList<Map<String, Object>> staffs = (ArrayList<Map<String, Object>>) staffData.get("users");
        for (Map<String, Object> staff : staffs) {
            StaffCard staffCard = new StaffCard(staff);
            container.getChildren().add(staffCard);
        }

        setContent(container);
        setFitToWidth(true);
        setFitToHeight(true);
        setMinWidth(roomContainer.getWidth());
        setMinHeight(roomContainer.getHeight());
        roomContainer.getChildren().add(this);
    }
}
