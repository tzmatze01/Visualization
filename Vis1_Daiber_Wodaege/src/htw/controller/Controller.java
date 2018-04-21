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
	int menuFlag = 0;

	private enum MenuEntries {
		CIRCILE("Circle"),
		SQUARE("Square"),
		PROGRAM("Program");

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

	private int randFixedArea;

	@FXML
	public void initialize() {

		infoLabel.setText("Waehlen Sie einen Test aus.");
		menuEntries.setItems(FXCollections.observableArrayList(MenuEntries.values()));

		canvas.setHeight(canvasHeight);
		canvas.setWidth(canvasWidth);

		gc = canvas.getGraphicsContext2D();

		slider.setMin(1);
		slider.setMax(12);

		button.setText("Ergebnis anzeigen");
		resultLabel.setText("Ergebnis");

		randFixedArea = ThreadLocalRandom.current().nextInt(canvasArea / 150, canvasArea / 60);

		slider.valueProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {

				programChange();
				/*
				if (menuFlag == true) {
					drawScaleableCircles(slider.getValue(), true);
				}
				if (squareFlag == true) {
					drawScaleableSquares(slider.getValue());
				}

				System.out.println(slider.getValue());
				*/
			}
		});
	}

	@FXML
	private void buttonClick() {
		double result = calculateResult(slider.getValue());

		String resultAsString = df2.format(result);

		resultLabel.setText(resultAsString);

	}

	private void programChange() {

		switch (menuEntries.getValue()) {
			case CIRCILE:
				infoLabel.setText("Bitte ziehen Sie den roten Kreis auf bis dieser im Verhaeltnis \n die dreifache Groesse des schwarzen Kreises hat.");
				drawScaleableCircles(slider.getValue(), true);
				break;
			case SQUARE:
				infoLabel.setText("Bitte ziehen Sie das rote Quadrat auf bis dieses im Verhaeltnis \n die dreifache Groesse des schwarzen Quadrates hat.");
				drawScaleableSquares(slider.getValue());
				break;
			case PROGRAM:
				break;

			default:
				break;
		}

	}



	@FXML
	public void chooseMenuEntry() {

		resetGui();
		programChange();

		/*
		switch (menuEntries.getValue()) {
		case CIRCILE:
			circleFlag = true;
			squareFlag = false;
			resetGui();
			infoLabel.setText("Bitte ziehen Sie den roten Kreis auf bis dieser im Verhaeltnis \n die dreifache Groesse des schwarzen Kreises hat.");
			drawScaleableCircles(slider.getValue(), true);
			break;
		case SQUARE:
			squareFlag = true;
			circleFlag = false;
			resetGui();
			infoLabel.setText("Bitte ziehen Sie das rote Quadrat auf bis dieses im Verhaeltnis \n die dreifache Groesse des schwarzen Quadrates hat.");
			drawScaleableSquares(slider.getValue());
			break;
		case PROGRAM:
			break;

		default:
			break;
		}
		*/
	}

	public void resetGui() {
		resultLabel.setText("Ergebnis");
		slider.setValue(1);
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
	}

	/*
	 * shape creation
	 */

	private void drawScaleableSquares(double sliderValue) {

		gc.clearRect(0, 0, canvasWidth, canvasHeight);

		double sideFixedSquare = Math.sqrt(randFixedArea);
		double sideScaleableSquare = Math.sqrt(randFixedArea * sliderValue);

		Square fixSquare = new Square(sideFixedSquare, Color.BLACK);
		Square scalableSquare = new Square(sideScaleableSquare, Color.RED);

		fixSquare.draw(gc, 0, canvasHeight / 4);
		scalableSquare.draw(gc, sideFixedSquare*2, canvasHeight / 4);

	}

	private void drawScaleableCircles(double sliderValue, boolean drawRadius) {

		gc.clearRect(0, 0, canvasWidth, canvasHeight);

		double radiusFixedCircle = Math.sqrt(randFixedArea);
		double radiusScaleableCircle = Math.sqrt(randFixedArea * sliderValue);

		Circle fixCircle = new Circle(radiusFixedCircle, Color.BLACK);
		Circle scaleableCircle = new Circle(radiusScaleableCircle, Color.RED);

		fixCircle.drawCircle(gc, 0, canvasHeight / 4);
		scaleableCircle.drawCircle(gc, radiusFixedCircle*2, canvasHeight / 4);

		if (drawRadius) {


			// radius small circle
			double x1 = radiusFixedCircle / 2;
			double y1 = canvasHeight / 4 + radiusFixedCircle / 2;
			double x2 = radiusFixedCircle;
			double y2 = canvasHeight / 4 + radiusFixedCircle / 2;

			gc.setStroke(Color.WHITE);
			gc.strokeLine(x1, y1, x2, y2);

			// radius big circle
			x1 = radiusFixedCircle * 2 + radiusScaleableCircle / 2;
			y1 = (canvasHeight / 4) + radiusScaleableCircle / 2;
			x2 = radiusFixedCircle * 2 + radiusScaleableCircle;
			y2 = (canvasHeight / 4) + radiusScaleableCircle / 2;


			gc.setStroke(Color.BLACK);
			gc.strokeLine(x1, y1, x2, y2);
		}
	}

	// generate two cirles with random numbers as radius and return the ratio
	private int drawCircles() {

		gc.clearRect(0, 0, canvasWidth, canvasHeight);

		int randAreaSmallCircle = ThreadLocalRandom.current().nextInt(canvasArea / 150, canvasArea / 60);
		int ratio = ThreadLocalRandom.current().nextInt(2, 12);

		// big cirle should be a 'even' multiple of small circle
		int randAreaBigCircle = randAreaSmallCircle * ratio;

		double radiusSmallCircle = Math.sqrt(randAreaSmallCircle);
		double radiusBigCircle = Math.sqrt(randAreaBigCircle);

		// TODO fläche schätzen mit radius eingezeichnet ?
		// TODO IDEE verschiedene Farben - ob sich das auf x auswirkt?

		Circle smallCircle = new Circle(radiusSmallCircle, Color.BLACK);
		Circle bigCircle = new Circle(radiusBigCircle, Color.RED);

		smallCircle.drawCircle(gc, 0, canvasHeight / 4);
		bigCircle.drawCircle(gc, radiusSmallCircle, canvasHeight / 4);

		return ratio;
	}

	// berechne x f�r (wahrgenommenes verh�ltnis) = (tats�chliches verh�ltnis)^x
	private double calculateResult(double astimatedRatio) {
		int realRation = 3;
		double result;
		return result = Math.log(astimatedRatio) / Math.log(realRation);

	}
}
