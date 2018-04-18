package htw.controller;

import htw.model.Circle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;


public class Controller {


    @FXML
    private Button button;

    @FXML
    private Slider slider;

    @FXML
    private Canvas canvas;


    private GraphicsContext gc;

    private int canvasHeight = 600;
    private int canvasWidth = 800;
    private int canvasArea = canvasHeight * canvasWidth;

    @FXML
    public void initialize() {

        button.setText("Click me");
        canvas.setHeight(canvasHeight);
        canvas.setWidth(canvasWidth);

        gc = canvas.getGraphicsContext2D();

        slider.setMin(1);
        slider.setMax(12);

        //drawCircles();

        int randAreaCircle = ThreadLocalRandom.current().nextInt(canvasArea / 150, canvasArea / 60);    // TODO sollte nach canvas angepasst werden
        drawScaleableCircles(randAreaCircle, slider.getValue(), true);


        slider.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                drawScaleableCircles(randAreaCircle, slider.getValue(), true);
            }
        });
    }


    @FXML
    private void buttonClick()
    {

        System.out.print("button");
    }

    private void drawScaleableCircles(double areaFixedCircle, double sliderValue, boolean drawRadius)
    {
        gc.clearRect(0,0, canvasWidth, canvasHeight);

        double radiusFixedCircle = Math.sqrt(areaFixedCircle);
        double radiusScaleableCircle = Math.sqrt(areaFixedCircle * sliderValue);

        Circle fixCircle = new Circle(radiusFixedCircle, Color.RED);
        Circle scaleableCircle = new Circle(radiusScaleableCircle, Color.BLACK);

        fixCircle.drawCircle(gc, 0, canvasHeight / 4);
        scaleableCircle.drawCircle(gc, radiusFixedCircle, canvasHeight / 4);

        if(drawRadius)
        {
            double x1 = radiusFixedCircle + radiusScaleableCircle / 2;
            double y1 = (canvasHeight / 4) + radiusScaleableCircle / 2;
            double x2 = radiusFixedCircle + radiusScaleableCircle;
            double y2 = (canvasHeight / 4) + radiusScaleableCircle / 2;

            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    // generate two cirles with random numbers as radius and return the ratio
    private int drawCircles() {

        int randAreaSmallCircle = ThreadLocalRandom.current().nextInt(canvasArea / 150, canvasArea / 60);    // TODO sollte nach canvas angepasst werden
        int ratio = ThreadLocalRandom.current().nextInt(2, 12);

        // big cirle should be a 'even' multiple of small circle
        int randAreaBigCircle = randAreaSmallCircle * ratio;

        double radiusSmallCircle = Math.sqrt(randAreaSmallCircle);
        double radiusBigCircle = Math.sqrt(randAreaBigCircle);

        // TODO fläche schätzen mit radius eingezeichnet ?
        // TODO IDEE verschiedene Farben - ob sich das auf x auswirkt?

        Circle smallCircle = new Circle(radiusSmallCircle, Color.RED);
        Circle bigCircle = new Circle(radiusBigCircle, Color.RED);

        smallCircle.drawCircle(gc, 0, canvasHeight / 4);
        bigCircle.drawCircle(gc, radiusSmallCircle, canvasHeight / 4);

        return ratio;
    }

}
