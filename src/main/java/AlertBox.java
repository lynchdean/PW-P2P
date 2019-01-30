import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(Stage ownerWindow, String title, String msg) {
        Stage alertWindow = new Stage();
        alertWindow.initOwner(ownerWindow);
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);

        Text message = new Text(msg);
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> alertWindow.close());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(25, 50, 25, 50));
        vbox.getChildren().addAll(message, closeBtn);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
}
