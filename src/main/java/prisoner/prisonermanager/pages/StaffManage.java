package prisoner.prisonermanager.pages;

import backend.Server;
import backend.User;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StaffManage extends ScrollPane {
    public StaffManage(VBox roomContainer) {
        VBox container = new VBox();
        container.setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        container.setSpacing(10);

        Label title = new Label("Thêm nhân viên");
        title.getStyleClass().add("detail-title-big");

        Label nameTitle = new Label("Họ và tên:");
        nameTitle.getStyleClass().add("detail-title");

        TextField staffName = new TextField();
        staffName.setPromptText("Tên nhân viên");
        staffName.getStyleClass().add("input");

        Label positionTitle = new Label("Chức vụ:");
        positionTitle.getStyleClass().add("detail-title");

        TextField staffPosition = new TextField();
        staffPosition.setPromptText("Chức vụ");
        staffPosition.getStyleClass().add("input");

        Label ageTitle = new Label("Tuổi:");
        ageTitle.getStyleClass().add("detail-title");

        TextField staffAge = new TextField();
        staffAge.setPromptText("Tuổi");
        staffAge.getStyleClass().add("input");

        Label salaryTitle = new Label("Lương:");
        salaryTitle.getStyleClass().add("detail-title");

        TextField staffSalary = new TextField();
        staffSalary.setPromptText("Lương");
        staffSalary.getStyleClass().add("input");

        Label startDateTitle = new Label("Ngày bắt đầu:");
        startDateTitle.getStyleClass().add("detail-title");

        DatePicker staffStartDay = new DatePicker();
        staffStartDay.setPromptText("Ngày bắt đầu");
        staffStartDay.getStyleClass().add("input");

        Label endDateTitle = new Label("Ngày kết thúc:");
        endDateTitle.getStyleClass().add("detail-title");

        DatePicker staffEndDay = new DatePicker();
        staffEndDay.setPromptText("Ngày kết thúc");
        staffEndDay.getStyleClass().add("input");

        Button addStaff = new Button("Thêm nhân viên");
        addStaff.getStyleClass().add("btn-primary");
        addStaff.setOnAction(event -> {
            if(staffName.getText().isEmpty() || staffPosition.getText().isEmpty() || staffAge.getText().isEmpty() || staffStartDay.getValue() == null || staffEndDay.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Vui lòng điền đầy đủ thông tin");
                alert.showAndWait();
            } else {
                try {
                    Server.addStaff(User.getToken(), staffName.getText(), staffAge.getText(), staffSalary.getText(), staffPosition.getText(), staffStartDay.getValue().toString(), staffEndDay.getValue().toString());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setHeaderText("Thêm tù nhân thành công");
                    alert.onHiddenProperty().set(e2 -> {
                        staffName.clear();
                        staffPosition.clear();
                        staffAge.clear();
                        staffStartDay.getEditor().clear();
                        staffEndDay.getEditor().clear();
                    });
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Không thể thêm nhân viên");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        container.getChildren().addAll(title, nameTitle, staffName, positionTitle, staffPosition, salaryTitle, staffSalary, ageTitle, staffAge, startDateTitle, staffStartDay, endDateTitle, staffEndDay, addStaff);

        setContent(container);
        roomContainer.getChildren().add(this);
    }
}
