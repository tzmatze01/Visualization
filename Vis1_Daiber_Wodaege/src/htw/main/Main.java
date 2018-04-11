package htw.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Main extends Application {


    //private BorderPane borderPane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = FXMLLoader.load(getClass().getResource("/htw/controller/sample.fxml"));
        primaryStage.setTitle("Vis1 - Daiber");

        /*
        borderPane = new BorderPane(root);

        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();


        // TODO generate randoom circle radiuses in method
        Circle circle = new Circle(5, Color.GREEN);
        circle.draw8Circle(gc);

        //borderPane.getChildren().add(canvas);
        borderPane.setCenter(canvas);
        */
        //primaryStage.setScene(borderPane);
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
