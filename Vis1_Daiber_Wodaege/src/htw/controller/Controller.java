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
        canvas.setHeight(600);
        canvas.setWidth(800);

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
        // substract 'margin' from area that generated circles stay in canvas
        double canvasArea = canvas.getWidth() * canvas.getHeight();

        int randAreaSmallCircle = ThreadLocalRandom.current().nextInt((int)canvasArea / 150, (int)canvasArea / 60);    // TODO sollte nach canvas angepasst werden

        // big cirle should be a 'even' multiple of small circle
        int randAreaBigCircle = randAreaSmallCircle * ThreadLocalRandom.current().nextInt(2, 20);

        System.out.println("area small circle: "+randAreaSmallCircle);
        System.out.println("area big circle: "+randAreaBigCircle);

        double radiusSmallCircle = Math.sqrt(randAreaSmallCircle);
        double radiusBigCircle = Math.sqrt(randAreaBigCircle);


        // TODO fläche schätzen mit radius eingezeichnet ?

        System.out.println("rad small circle: "+radiusSmallCircle);
        System.out.println("rad big circle: "+radiusBigCircle);

        int ratio = (int) Math.ceil((double)randAreaBigCircle / randAreaSmallCircle);
        //double rat = checkAreaRatio(randAreaBigCircle, randAreaSmallCircle);

        System.out.println("\n\nratio is: "+(double)randAreaBigCircle / randAreaSmallCircle);

        Circle smallCircle = new Circle(radiusSmallCircle, Color.RED); // TODO IDEE verschiedene Farben - ob sich das auf x auswirkt?
        Circle bigCircle = new Circle(radiusBigCircle, Color.RED);
//        Circle smallCircle = new Circle(100, Color.RED); // TODO IDEE verschiedene Farben - ob sich das auf x auswirkt?
//        Circle bigCircle = new Circle(300, Color.RED);

        gc.moveTo(canvas.getHeight() / 2, canvas.getWidth() / 4);
        smallCircle.drawCircle(gc, 0, canvas.getHeight() / 4);
        gc.moveTo(canvas.getHeight() / 2, canvas.getWidth() - canvas.getWidth() / 4);
        bigCircle.drawCircle(gc, radiusSmallCircle, canvas.getHeight() / 4);


    }

}
