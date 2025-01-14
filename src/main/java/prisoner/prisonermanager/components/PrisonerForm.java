package prisoner.prisonermanager.components;

import backend.Server;
import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prisoner.prisonermanager.Core;
import prisoner.prisonermanager.pages.Prisoners;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class PrisonerForm extends VBox {
    public PrisonerForm(HBox root, Map<String, Object> prisoner, Map<String, Object> room, ArrayList<Object> previousPage, ArrayList<Object> nextPage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Label title = new Label("Thông tin tù nhân");
        title.getStyleClass().add("detail-title-big");

        Label nameTitle = new Label("Họ và tên:");
        nameTitle.getStyleClass().add("detail-title");
        TextField name = new TextField((String) prisoner.get("name"));
        name.getStyleClass().add("input");

        Label typeTitle = new Label("Phân loại:");
        typeTitle.getStyleClass().add("detail-title");
        TextField type = new TextField((String) prisoner.get("type"));
        type.getStyleClass().add("input");

        Label ageTitle = new Label("Tuổi:");
        ageTitle.getStyleClass().add("detail-title");
        TextField age = new TextField(prisoner.get("age").toString());
        age.getStyleClass().add("input");

        Label startDateTitle = new Label("Ngày lãnh án:");
        startDateTitle.getStyleClass().add("detail-title");
        DatePicker startDate = new DatePicker();
        startDate.setValue(LocalDate.parse((CharSequence) prisoner.get("startDay").toString().substring(0, 10), formatter));
        startDate.getStyleClass().add("input");

        Label endDateTitle = new Label("Ngày kết thúc:");
        endDateTitle.getStyleClass().add("detail-title");
        DatePicker endDate = new DatePicker();
        endDate.setValue(LocalDate.parse((CharSequence) prisoner.get("endDay").toString().substring(0, 10), formatter));
        endDate.getStyleClass().add("input");

        Label cellTitle = new Label("Phòng:");
        cellTitle.getStyleClass().add("detail-title");
        TextField cell = new TextField(prisoner.get("roomID").toString());
        cell.getStyleClass().add("input");

        Button editPrisoner = new Button("Câp nhật");
        editPrisoner.getStyleClass().add("btn-primary");
        editPrisoner.setOnAction(e -> {
            if(name.getText().isEmpty() || type.getText().isEmpty() || age.getText().isEmpty() || startDate.getValue() == null || endDate.getValue() == null || cell.getText().isEmpty()) {
                return;
            }

            String rawData = Server.updatePrisoner(User.getToken(), prisoner.get("id").toString(), name.getText(), age.getText(), cell.getText(), type.getText(), startDate.getValue().format(formatter), endDate.getValue().format(formatter));
            Map<String, Object> response = null;
            try {
                response = new ObjectMapper().readValue(rawData, Map.class);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }

            if((boolean) response.get("success")) {
                prisoner.put("name", name.getText());
                prisoner.put("type", type.getText());
                prisoner.put("age", Integer.parseInt(age.getText()));
                prisoner.put("startDay", startDate.getValue().format(formatter));
                prisoner.put("endDay", endDate.getValue().format(formatter));
                Node detail = Core.root.lookup("#detail");
                ((VBox) detail).getChildren().clear();

                if(!prisoner.get("roomID").toString().equals(cell.getText())) {
                    prisoner.put("roomID", cell.getText());
                    VBox roomContainer = (VBox) Core.root.lookup("#roomContainer");
                    roomContainer.getChildren().clear();
                    try {
                        roomContainer.getChildren().add(new Prisoners(root, previousPage, nextPage, room));
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    ((VBox) detail).getChildren().add(new PrisonerDetail(prisoner));
                }

            }
        });

        getChildren().addAll(title, nameTitle, name, typeTitle, type, ageTitle, age, startDateTitle, startDate, endDateTitle, endDate, cellTitle, cell, editPrisoner);
    }
}
