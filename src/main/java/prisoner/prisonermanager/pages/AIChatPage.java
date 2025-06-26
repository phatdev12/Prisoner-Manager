package prisoner.prisonermanager.pages;

import backend.AIChat;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.ArrayList;

public class AIChatPage extends VBox {
    public AIChatPage(VBox roomContainer, ArrayList<Object> previousPage, ArrayList<Object> nextPage) {
        roomContainer.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setContent(new VBox());
        roomContainer.getChildren().add(scrollPane);
        roomContainer.getStyleClass().add("ai-chat-page");
        roomContainer.setSpacing(10);
        TextField aiField = new TextField();
        aiField.setPromptText("Nhập câu hỏi của bạn ở đây...");
        aiField.getStyleClass().add("input");
        aiField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                String question = aiField.getText();
                if (!question.isEmpty()) {
                    try {
                        handleSendButtonAction(new ActionEvent(), aiField, scrollPane);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        roomContainer.getChildren().add(aiField);
    }

    public void onSuccessMessage(Task<String> aiResponseTask, VBox vbox, ScrollPane scrollPane) {
        String response = aiResponseTask.getValue();
        createAIResponse(response, vbox, scrollPane);
    }

    public void onFailureMessage(Task<String> aiResponseTask, VBox vbox, ScrollPane scrollPane) {
        Throwable exception = aiResponseTask.getException();
        createAIResponse("AI: Xin lỗi, đã xảy ra lỗi khi xử lý yêu cầu của bạn.", vbox, scrollPane);
    }

    private String wrapHtml(String htmlContent) {
        return """
        <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        font-size: 14px;
                        padding: 10px;
                        background-color: #d0f0c0;
                        margin: 0;
                        word-wrap: break-word;
                        max-width: 100%;
                        height: fit-content;
                    }
                </style>
            </head>
            <body>
                """ + htmlContent + """
            </body>
        </html>
        """;
    }

    private String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private void createUserMessage(String message, VBox chatContainer) {
        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add("user-message");
        textFlow.getChildren().add(new javafx.scene.text.Text(message));
        chatContainer.getChildren().add(textFlow);

    }

    public void createAIResponse(String response, VBox vbox, ScrollPane scrollPane) {
        String htmlContent = markdownToHtml(response);

        WebView webView = new WebView();
        webView.setContextMenuEnabled(false);
        webView.setMaxWidth(Double.MAX_VALUE);
        webView.setPrefHeight(50);
        VBox.setVgrow(webView, Priority.ALWAYS);
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(wrapHtml(htmlContent));
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newState) -> {
            if (newState == Worker.State.SUCCEEDED)  {
                Platform.runLater(() -> {
                    Object result = webEngine.executeScript("document.body.scrollHeight");
                    if (result instanceof Integer) {
                        int height = (Integer) result;
                        webView.setPrefHeight(height + 20);
                    } else if (result instanceof Double) {
                        double height = (Double) result;
                        webView.setPrefHeight(height + 20);
                    }
                });
            }
        });

        vbox.getChildren().add(webView);
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    public void handleSendButtonAction(ActionEvent actionEvent, TextField inputField, ScrollPane scrollPane) throws Exception {
        String userInput = inputField.getText();
        if (!userInput.isEmpty()) {
            createUserMessage(userInput, (VBox) scrollPane.getContent());
            inputField.clear();

            Task<String> aiResponseTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    return AIChat.callAIChat(userInput);
                }
            };

            aiResponseTask.setOnRunning(event -> {
                TextFlow loadingMessage = new TextFlow(new Text("..."));
                loadingMessage.setStyle("-fx-background-color: #d0f0c0; -fx-padding: 10px;");
                VBox vbox = (VBox) scrollPane.getContent();
                vbox.getChildren().add(loadingMessage);
                Platform.runLater(() -> scrollPane.setVvalue(1.0));

                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                    if (!vbox.getChildren().isEmpty() && vbox.getChildren().getLast() instanceof TextFlow lastMessage) {
                        if (lastMessage.getChildren().size() == 1 && lastMessage.getChildren().getFirst() instanceof Text textNode) {
                            String currentText = textNode.getText();
                            if (currentText.length() >= 6) {
                                textNode.setText(".");
                            } else {
                                textNode.setText(currentText + ".");
                            }
                        }
                    }
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();

                aiResponseTask.setOnSucceeded(ev -> {
                    timeline.stop();
                    vbox.getChildren().remove(loadingMessage);
                    onSuccessMessage(aiResponseTask, vbox, scrollPane);

                });
                aiResponseTask.setOnFailed(ev -> {
                    timeline.stop();
                    vbox.getChildren().remove(loadingMessage);
                    onFailureMessage(aiResponseTask, vbox, scrollPane);
                });
            });


            new Thread(aiResponseTask).start();
        }
    }

}
