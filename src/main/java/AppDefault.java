import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class AppDefault extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SyncSafe");

        GridPane gridPane = createOpenFilePane();
        addUIControls(gridPane, primaryStage);
        Scene scene = new Scene(gridPane, 800, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private GridPane createOpenFilePane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnConstraints.setHalignment(HPos.RIGHT);

        ColumnConstraints columnTwoConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstraints.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnConstraints, columnTwoConstraints);

        return gridPane;
    }

    public abstract void addUIControls(GridPane gridPane, Stage primaryStage);

    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
