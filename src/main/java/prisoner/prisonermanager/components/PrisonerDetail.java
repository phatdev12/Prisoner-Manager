package prisoner.prisonermanager.components;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;

public class PrisonerDetail extends VBox {
    public PrisonerDetail(Map<String, Object> prisoner) {
        Label title = new Label("Thông tin chi tiết");
        title.getStyleClass().add("detail-title-big");
        Label nameTitle = new Label("Họ và tên:");
        nameTitle.getStyleClass().add("detail-title");
        Label name = new Label((String) prisoner.get("name"));
        name.getStyleClass().add("detail-content");
        Label typeTitle = new Label("Phân loại:");
        typeTitle.getStyleClass().add("detail-title");
        Label type = new Label((String) prisoner.get("type"));
        type.getStyleClass().add("detail-content");
        Label ageTitle = new Label("Tuổi:");
        ageTitle.getStyleClass().add("detail-title");
        Label age = new Label(prisoner.get("age").toString());
        age.getStyleClass().add("detail-content");
        Label startDateTitle = new Label("Ngày lãnh án:");
        startDateTitle.getStyleClass().add("detail-title");
        Label startDate = new Label(prisoner.get("startDay").toString());
        startDate.getStyleClass().add("detail-content");
        Label endDateTitle = new Label("Ngày kết thúc:");
        endDateTitle.getStyleClass().add("detail-title");
        Label endDate = new Label(prisoner.get("endDay").toString());
        endDate.getStyleClass().add("detail-content");

        getChildren().addAll(title, nameTitle, name, typeTitle, type, ageTitle, age, startDateTitle, startDate, endDateTitle, endDate);
    }
}
