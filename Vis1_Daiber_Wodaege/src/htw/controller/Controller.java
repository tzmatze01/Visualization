package htw.controller;

import htw.model.Circle;
import htw.model.Square;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {

	private static DecimalFormat df2 = new DecimalFormat(".#");
	boolean circleFlag = false;
	boolean squareFlag = false;

	private enum MenuEntries {
		CIRCILE("Circle"), SQUARE("Square");

		private final String name;

		MenuEntries(String s) {
			name = s;
		}

		public String toString() {
			return this.name;
		}
	}

	@FXML
	private ComboBox<MenuEntries> menuEntries;

	@FXML
	private Slider slider;

	@FXML
	private Button button;

	@FXML
	private Canvas canvas;

	@FXML
	private Label resultLabel;

	@FXML
	private Label infoLabel;

	private GraphicsContext gc;

	private int canvasHeight = 600;
	private int canvasWidth = 800;
	private int canvasArea = canvasHeight * canvasWidth;

	@FXML
	public void initialize() {

		infoLabel.setText("Wählen Sie einen Test aus.");
		menuEntries.setItems(FXCollections.observableArrayList(MenuEntries.values()));

		canvas.setHeight(canvasHeight);
		canvas.setWidth(canvasWidth);

		gc = canvas.getGraphicsContext2D();

		slider.setMin(1);
		slider.setMax(12);

		button.setText("view result");
		resultLabel.setText("result");
	}

	@FXML
	private void buttonClick() {
		double result = calculateResult(slider.getValue());

		String resultAsString = df2.format(result);

		resultLabel.setText(resultAsString);

	}

	@FXML
	public void chooseMenuEntry() {

		switch (menuEntries.getValue()) {
		case CIRCILE:
			circleFlag = true;
			squareFlag = false;
			resetGui();
			circleTest();
			break;
		case SQUARE:
			squareFlag = true;
			circleFlag = false;
			resetGui();
			squareTest();
			break;
		default:
			break;
		}
	}

	public void resetGui() {
		resultLabel.setText("result");
		slider.setValue(1);
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
	}

	public void circleTest() {
		infoLabel.setText(
				"Bitte ziehen Sie den roten Kreis auf bis dieser im Verhältnis \n die dreifache Größe des schwarzen Kreises hat.");

		// drawCircles();

		int randAreaCircle = ThreadLocalRandom.current().nextInt(canvasArea / 150, canvasArea / 60); // TODO sollte nach
																										// canvas
																										// angepasst
																										// werden
		drawScaleableCircles(randAreaCircle, slider.getValue(), true);

		slider.valueProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if (circleFlag == true) {
					drawScaleableCircles(randAreaCircle, slider.getValue(), true);
				}
				if (squareFlag == true) {
					// drawScaleableSquares);
				}

				System.out.println(slider.getValue());

			}
		});
	}

	public void squareTest() {
		infoLabel.setText(
				"Bitte ziehen Sie das rote Quadrat auf bis dieses im Verhältnis \n die dreifache Größe des schwarzen Quadrates hat.");
		drawScaleableSquares();
	}

	/*
	 * shape creation
	 */

	private void drawScaleableSquares() {
		Square fixSquare = new Square(5);

	}

	private void drawScaleableCircles(double areaFixedCircle, double sliderValue, boolean drawRadius) {
		gc.clearRect(0, 0, canvasWidth, canvasHeight);

		double radiusFixedCircle = Math.sqrt(areaFixedCircle);
		double radiusScaleableCircle = Math.sqrt(areaFixedCircle * sliderValue);

		Circle fixCircle = new Circle(radiusFixedCircle, Color.RED);
		Circle scaleableCircle = new Circle(radiusScaleableCircle, Color.BLACK);

		fixCircle.drawCircle(gc, 0, canvasHeight / 4);
		scaleableCircle.drawCircle(gc, radiusFixedCircle, canvasHeight / 4);

		if (drawRadius) {
			double x1 = radiusFixedCircle + radiusScaleableCircle / 2;
			double y1 = (canvasHeight / 4) + radiusScaleableCircle / 2;
			double x2 = radiusFixedCircle + radiusScaleableCircle;
			double y2 = (canvasHeight / 4) + radiusScaleableCircle / 2;

			gc.strokeLine(x1, y1, x2, y2);
		}
	}

	// generate two cirles with random numbers as radius and return the ratio
	private int drawCircles() {

		int randAreaSmallCircle = ThreadLocalRandom.current().nextInt(canvasArea / 150, canvasArea / 60); // TODO sollte
																											// nach
																											// canvas
																											// angepasst
																											// werden
		int ratio = ThreadLocalRandom.current().nextInt(2, 12);

		// big cirle should be a 'even' multiple of small circle
		int randAreaBigCircle = randAreaSmallCircle * ratio;

		double radiusSmallCircle = Math.sqrt(randAreaSmallCircle);
		double radiusBigCircle = Math.sqrt(randAreaBigCircle);

		// TODO flÃ¤che schÃ¤tzen mit radius eingezeichnet ?
		// TODO IDEE verschiedene Farben - ob sich das auf x auswirkt?

		Circle smallCircle = new Circle(radiusSmallCircle, Color.RED);
		Circle bigCircle = new Circle(radiusBigCircle, Color.RED);

		smallCircle.drawCircle(gc, 0, canvasHeight / 4);
		bigCircle.drawCircle(gc, radiusSmallCircle, canvasHeight / 4);

		return ratio;
	}

	// berechne x für (wahrgenommenes verhältnis) = (tatsächliches verhältnis)^x
	private double calculateResult(double astimatedRatio) {
		int realRation = 3;
		double result;
		return result = Math.log(astimatedRatio) / Math.log(realRation);

	}
}
