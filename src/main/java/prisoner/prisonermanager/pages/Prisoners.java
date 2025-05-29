package prisoner.prisonermanager.pages;

import backend.Prisoner;
import backend.Server;
import backend.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prisoner.prisonermanager.Core;
import prisoner.prisonermanager.components.PrisonerCard;

import java.util.ArrayList;
import java.util.Map;


public class Prisoners extends ScrollPane {
    public Prisoners(HBox root, ArrayList<Object> previousPage, ArrayList<Object> nextPage, Map<String, Object> room) throws JsonProcessingException, JsonProcessingException {
        VBox roomContainer = (VBox) Core.root.lookup("#roomContainer");
        roomContainer.setPadding(new Insets(0, 0, 0, 0));
        setMinWidth(root.getWidth());
        String rawPrisonerData = Server.getPrisoners(User.getToken(), room.get("id").toString());
        Map<String, Object> prisonerData = new ObjectMapper().readValue(rawPrisonerData, Map.class);
        ArrayList<Map<String, Object>> prisoners = (ArrayList<Map<String, Object>>) prisonerData.get("prisoner");

        Button viewChartButton = new Button("Xem biểu đồ");
        viewChartButton.getStyleClass().add("btn-primary");
        viewChartButton.setOnAction(event -> {
            Stage chartStage = new Stage();
            chartStage.setTitle("Biểu đồ tù nhân");
            Map<String, Integer> typeCount = new java.util.HashMap<>(Map.of());
            for(Map<String, Object> prisoner: prisoners) {
                String type = (String) prisoner.get("type");
                if(typeCount.containsKey(type)) {
                    typeCount.put(type, typeCount.get(type) + 1);
                } else {
                    typeCount.put(type, 1);
                }
            }
            // tạo dữ liệu cho biểu đồ bằng cách đếm loại tội phạm (type) của từng tù nhân
            PieChart.Data[] data = typeCount.entrySet().stream()
                    .map(entry -> new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()))
                    .toArray(PieChart.Data[]::new);


            PieChart pieChart = new PieChart();
            pieChart.setData(FXCollections.observableArrayList(data));
            pieChart.setTitle("Biểu đồ số lượng tù nhân theo trạng thái");
            pieChart.setLegendVisible(true);
            pieChart.setLabelsVisible(true);
            pieChart.setLabelLineLength(10);
            pieChart.setStartAngle(90);
            pieChart.setClockwise(false);
            pieChart.setPrefSize(800, 600);

            Group chartGroup = new Group(pieChart);
            Scene chartScene = new Scene(chartGroup, 800, 600);
            chartStage.setScene(chartScene);
            chartStage.setResizable(false);
            chartStage.show();
        });

        VBox prisonerContainer = new VBox();
        prisonerContainer.setSpacing(10);
        prisonerContainer.setPadding(new Insets(15, 15, 15, 15));
        prisonerContainer.getChildren().add(viewChartButton);
        for(int i = 0; i < prisoners.size(); i++) {
            Map<String, Object> prisoner = prisoners.get(i);
            prisonerContainer.getChildren().add(new PrisonerCard(root, prisoner, previousPage, nextPage, room));
        }

        setContent(prisonerContainer);
    }

}
