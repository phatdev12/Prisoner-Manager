package prisoner.prisonermanager.pages;

import backend.WebSocket;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Notice extends VBox {
    public Notice() {
        setPadding(new javafx.geometry.Insets(15, 15, 15, 15));
        setSpacing(10);
        Label title = new Label("Thông báo");
        title.getStyleClass().add("detail-title-big");
        Label content = new Label("Nội dung");
        content.getStyleClass().add("detail-title");
        TextField messageField = new TextField();
        messageField.setPromptText("Nội dung thông báo");
        messageField.getStyleClass().add("input");
        Button send = new Button("Gửi thông báo");
        send.getStyleClass().add("btn-primary");
        send.setOnAction(e -> {
            WebSocket.sendNotification(messageField.getText());
        });
        getChildren().addAll(title, content, messageField, send);
    }
}
