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

    @FXML
    public void initialize() {

        button.setText("Click me");
        canvas.setHeight(300);
        canvas.setWidth(400);

        gc = canvas.getGraphicsContext2D();

        drawCircles(gc);

        // TODO set programmatically with borders of circles / squares
        slider.setMin(0);
        slider.setMax(10);

        slider.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                drawStuff(slider.getValue());
            }
        });


    }


    @FXML
    private void buttonClick()
    {

        System.out.print("button");
    }

    private void drawStuff(double sliderValue)
    {

        System.out.print("slider: "+sliderValue);
    }


    private double checkAreaRatio(int areaA, int areaB) {

        if(areaA > areaB)
            return Math.log(areaA) / Math.log(areaB);
        else
            return Math.log(areaB) / Math.log(areaA);

    }

    // TODO returns area of created circles?
    // generate two cirles with random numbers as radius
    private void drawCircles(GraphicsContext gc) {

        // circles should have 'even' areas, therefore integer
        int randNumSmallCircle = ThreadLocalRandom.current().nextInt((int)canvas.getWidth() / 10, (int)canvas.getWidth() / 2);    // TODO sollte nach GUI angepasst werden

        // big cirle should be a 'even' multiple of small circle
        int randNumBigCircle = randNumSmallCircle * ThreadLocalRandom.current().nextInt(0, 20);

        System.out.println("area small circle: "+randNumSmallCircle);
        System.out.println("area big circle: "+randNumBigCircle);

        double radiusSmallCircle = Math.sqrt(randNumSmallCircle / Math.PI);
        double radiusBigCircle = Math.sqrt(randNumBigCircle / Math.PI);

        Circle smallCircle = new Circle(radiusSmallCircle, Color.RED); // TODO IDEE verschiedene Farben - ob sich das auf x auswirkt?
        Circle bigCircle = new Circle(radiusBigCircle, Color.RED);

        smallCircle.drawCircle(gc, 0, radiusBigCircle);
        bigCircle.drawCircle(gc, radiusBigCircle, 0);


    }

}
