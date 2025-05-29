package prisoner.prisonermanager.pages;

import backend.Server;
import backend.User;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class PrisonerManage extends ScrollPane {
    public PrisonerManage(VBox roomContainer) {
        VBox container = new VBox();
        container.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        container.setSpacing(10);

        Label title = new Label("Thêm tù nhân");
        title.getStyleClass().add("detail-title-big");

        Label nameTitle = new Label("Họ và tên:");
        nameTitle.getStyleClass().add("detail-title");
        TextField prisonerName = new TextField();
        prisonerName.setPromptText("Tên tù nhân");
        prisonerName.getStyleClass().add("input");

        Label typeTitle = new Label("Phân loại:");
        typeTitle.getStyleClass().add("detail-title");
        TextField prisonerType = new TextField();
        prisonerType.setPromptText("Phân loại");
        prisonerType.getStyleClass().add("input");

        Label ageTitle = new Label("Tuổi:");
        ageTitle.getStyleClass().add("detail-title");
        TextField prisonerAge = new TextField();
        prisonerAge.setPromptText("Tuổi");
        prisonerAge.getStyleClass().add("input");

        Label startDateTitle = new Label("Ngày lãnh án:");
        startDateTitle.getStyleClass().add("detail-title");
        DatePicker prisonerStartDay = new DatePicker();
        prisonerStartDay.setPromptText("Ngày lãnh án");
        prisonerStartDay.getStyleClass().add("input");

        Label endDateTitle = new Label("Ngày kết thúc:");
        endDateTitle.getStyleClass().add("detail-title");
        DatePicker prisonerEndDay = new DatePicker();
        prisonerEndDay.setPromptText("Ngày kết thúc");
        prisonerEndDay.getStyleClass().add("input");

        Label cellTitle = new Label("Phòng:");
        cellTitle.getStyleClass().add("detail-title");
        TextField prisonerCell = new TextField();
        prisonerCell.setPromptText("Phòng");
        prisonerCell.getStyleClass().add("input");

        Button addPrisoner = new Button("Thêm tù nhân");
        addPrisoner.getStyleClass().add("btn-primary");
        addPrisoner.setOnAction(e -> {
            if(prisonerName.getText().isEmpty() || prisonerType.getText().isEmpty() || prisonerAge.getText().isEmpty() || prisonerStartDay.getValue() == null || prisonerEndDay.getValue() == null || prisonerCell.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Vui lòng điền đầy đủ thông tin");
                alert.showAndWait();
                return;
            }

            Server.addPrisoner(User.getToken(), prisonerName.getText(), prisonerAge.getText(), prisonerCell.getText(), prisonerType.getText(), prisonerStartDay.getValue().toString(), prisonerEndDay.getValue().toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText("Thêm tù nhân thành công");
            alert.onHiddenProperty().set(e2 -> {
                prisonerName.clear();
                prisonerType.clear();
                prisonerAge.clear();
                prisonerStartDay.getEditor().clear();
                prisonerEndDay.getEditor().clear();
                prisonerCell.clear();
            });
            alert.showAndWait();

        });

        container.getChildren().addAll(title, nameTitle, prisonerName, typeTitle, prisonerType, ageTitle, prisonerAge, startDateTitle, prisonerStartDay, endDateTitle, prisonerEndDay, cellTitle, prisonerCell, addPrisoner);
        setContent(container);
        roomContainer.getChildren().add(this);
    }
}
