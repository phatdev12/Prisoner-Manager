package backend;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import prisoner.prisonermanager.App;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletionStage;

public class WebSocketEvent implements WebSocket.Listener {

    @Override
    public void onOpen(WebSocket webSocket) {
        System.out.println("WebSocket opened");
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("Message received: " + data);

        Platform.runLater(() -> showNotification(data.toString()));

        webSocket.request(1);
        return null;
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        System.out.println("Binary data received");
        webSocket.request(1);
        return null;
    }

    @Override
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        System.out.println("Ping received");
        return WebSocket.Listener.super.onPing(webSocket, message);
    }

    @Override
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        System.out.println("Pong received");
        return WebSocket.Listener.super.onPong(webSocket, message);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed: " + reason);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.err.println("WebSocket error: " + error.getMessage());
    }

    private void showNotification(String message) {
        ProcessBuilder pb = new ProcessBuilder("src/main/resources/google-service/google-tts", message);
        Stage notificationStage = new Stage();
        playAudioFromResources("notice.wav", notificationStage);

        Scene notificationScene = getScene(message, pb);
        notificationStage.setScene(notificationScene);
        notificationStage.setFullScreen(true);

        notificationStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(30000);
                Platform.runLater(notificationStage::close);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static Scene getScene(String message, ProcessBuilder pb) {
        Timer timer = new Timer();
        final int[] count = {0};
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                count[0]++;
                try {
                    pb.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(count[0] >= 5) {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 5000);
        Text notificationText = new Text(message);
        notificationText.setStyle("-fx-font-size: 48; -fx-fill: white;");

        StackPane notificationPane = new StackPane(notificationText);
        notificationPane.setStyle("-fx-background-color: red;");

        Scene notificationScene = new Scene(notificationPane, 600, 400);
        return notificationScene;
    }

    private void playAudioFromResources(String resourceFileName, Stage stage) {
        try {
            URL resource = Paths.get("src/main/resources/" + resourceFileName).toUri().toURL();
            if (resource == null) {
                System.err.println("Audio file not found!");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(resource);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = volumeControl.getValue();
            volumeControl.setValue(volume - 4.0f);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();

            stage.setOnCloseRequest(event -> clip.stop());
        } catch (LineUnavailableException |
                 UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
}
