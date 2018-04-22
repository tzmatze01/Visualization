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
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {

	private static DecimalFormat df2 = new DecimalFormat(".#");
	int menuFlag = 0;

	private enum MenuEntries {
		CIRCLE("Kreis"),
		RAD_CIRCLE("Kreis mit Radius"),
		SQUARE("Viereck"),
		GUESS_CIRCLE("Verhältnis Kreisflächen");
		//PROGRAM("Program");

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
	private Button btnCalc;

	@FXML
	private Button btnNext;

	@FXML
	private Canvas canvas;

	@FXML
	private Label resultLabel;

	@FXML
	private Label infoLabel;

	@FXML
	private Label lblEstimate;

	@FXML
	private TextField tfEstimate;

	private GraphicsContext gc;

	private int canvasHeight = 300;
	private int canvasWidth = 400;
	private int canvasArea = canvasHeight * canvasWidth;

	private int randFixedArea;
	private int shapeSize = 0;
	private int circleRatio = 0;

	private List<Double> overallRatio;

	@FXML
	public void initialize()
	{
		overallRatio = new LinkedList<>();

		infoLabel.setText("Waehlen Sie einen Test aus.");
		menuEntries.setItems(FXCollections.observableArrayList(MenuEntries.values()));

		canvas.setHeight(canvasHeight);
		canvas.setWidth(canvasWidth);

		gc = canvas.getGraphicsContext2D();

		slider.setMin(1);
		slider.setMax(10);

		btnCalc.setText("Ergebnis anzeigen");
		btnNext.setText("Nächter Versuch");
		resultLabel.setText("Ergebnis");

		btnNext.setDisable(true);
		lblEstimate.setVisible(false);
		tfEstimate.setVisible(false);


		randFixedArea = ThreadLocalRandom.current().nextInt(canvasArea / 80, canvasArea / 20);
		shapeSize = ThreadLocalRandom.current().nextInt(2, 11);

		slider.valueProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {

				shapeChange();
			}
		});
	}

	@FXML
	private void calcRatio()
	{
		double meanRatio = 0;
		double currentRatio = 0;
		for(int i = 0; i < overallRatio.size(); ++i)
		{
			meanRatio += overallRatio.get(i);
		}

		meanRatio /= overallRatio.size();

		switch (menuEntries.getValue()) {
			case CIRCLE:
			case SQUARE:
			case RAD_CIRCLE:
				currentRatio = calculateResult(randFixedArea * shapeSize, randFixedArea * slider.getValue());
				break;
			case GUESS_CIRCLE:
				if(tfEstimate.getText().isEmpty()) {
					infoLabel.setText("Bitte etwas als Schätzung eingeben!");
					break;
				}
				else {
					// TODO ungültige eingaben escapen
					double guessedRatio = Double.valueOf(tfEstimate.getText());

					currentRatio = calculateResult(circleRatio, guessedRatio);
					break;
				}

		}

		// TODO value
		double currentResult = calculateResult(currentRatio, shapeSize);
		double meanResult = calculateResult(meanRatio, shapeSize);

		String currentResultAsString = df2.format(currentRatio);
		String meanResultAsString = df2.format(meanRatio);

		resultLabel.setText("Die aktuelle Ratio ist: "+ currentResultAsString + " die bisherigen Ratios im Schnitt sind: "+meanRatio);

	}

	private void shapeChange() {

		switch (menuEntries.getValue()) {
			case CIRCLE:
				infoLabel.setText("Bitte ziehen Sie den roten Kreis auf bis dieser im Verhaeltnis \n die "+shapeSize+"-fache Groesse des schwarzen Kreises hat.");
				drawScaleableCircles(slider.getValue(), false);
				break;
			case SQUARE:
				infoLabel.setText("Bitte ziehen Sie das rote Quadrat auf bis dieses im Verhaeltnis \n die "+shapeSize+"-fache Groesse des schwarzen Quadrates hat.");
				drawScaleableSquares(slider.getValue());
				break;
			case RAD_CIRCLE:
				infoLabel.setText("Bitte ziehen Sie den roten Kreis auf bis dieser im Verhaeltnis \n die "+shapeSize+"-fache Groesse des schwarzen Kreises hat.");
				drawScaleableCircles(slider.getValue(), true);
				break;
			case GUESS_CIRCLE:
				infoLabel.setText("Schätzen Sie um wieviel groesser der schwarze Kreis im Vergleich \n zum roten Kreis ist.");
				slider.setDisable(true);
				lblEstimate.setVisible(true);
				tfEstimate.setVisible(true);
				drawCircles();
				break;
			/*
			case PROGRAM:
				btnNext.setDisable(false);
				drawProgram();
				break;
			*/
			default:
				break;
		}

	}

	@FXML
	public void chooseMenuEntry() {

		lblEstimate.setVisible(false);
		tfEstimate.setVisible(false);
		slider.setDisable(false);

		resultLabel.setText("Ergebnis");
		overallRatio = new LinkedList<>();
		slider.setValue(1);
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
		btnNext.setDisable(false);
		nextShape();
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

		if (drawRadius)
		{
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
	private void drawCircles() {

		gc.clearRect(0, 0, canvasWidth, canvasHeight);

		int randAreaSmallCircle = ThreadLocalRandom.current().nextInt(canvasArea / 150, canvasArea / 60);
		circleRatio = ThreadLocalRandom.current().nextInt(2, 12);

		// big cirle should be a 'even' multiple of small circle
		int randAreaBigCircle = randAreaSmallCircle * circleRatio;

		double radiusSmallCircle = Math.sqrt(randAreaSmallCircle);
		double radiusBigCircle = Math.sqrt(randAreaBigCircle);

		// TODO fläche schätzen mit radius eingezeichnet ?
		// TODO IDEE verschiedene Farben - ob sich das auf x auswirkt?

		Circle smallCircle = new Circle(radiusSmallCircle, Color.BLACK);
		Circle bigCircle = new Circle(radiusBigCircle, Color.RED);

		smallCircle.drawCircle(gc, 0, canvasHeight / 4);
		bigCircle.drawCircle(gc, radiusSmallCircle, canvasHeight / 4);
	}

	/*
	@FXML
	public void drawProgram()
	{
		switch (programCounter){
			case 0:
				infoLabel.setText("Dieses Program führt Sie durch einige Aufgaben, bei denen  \n " +
									"der Groessenunterschied von Zwei Formen bestimmt werden soll. \n\n " +
									"Click 'Nächste' für nächsten Schritt"  );
				break;
			case 1:
				infoLabel.setText("Bitte ziehen Sie das rote Quadrat auf bis dieses im Verhaeltnis \n die dreifache Groesse des schwarzen Quadrates hat.");
				drawScaleableSquares(slider.getValue());
				programRatios.add(calculateResult(3, ratio));
				System.out.println("1 ratio: "+ratio);
				// TODO error label for leeres feld
				break;
			case 3:
				infoLabel.setText("Schätzen Sie um wieviel der schwarze Kreis größer ist als der rote. \n Click 'Nächste' für nächsten Schritt");
				break;
			case 4:
				break;
			case 5:
				break;

			default:
				infoLabel.setText("program nix gut");
				break;
		}

		programCounter++;
	}
	*/

	@FXML
	private void nextShape()
	{
		switch (menuEntries.getValue()) {
			case CIRCLE:
			case SQUARE:
			case RAD_CIRCLE:
				overallRatio.add(calculateResult(randFixedArea * shapeSize, randFixedArea * slider.getValue()));
				newShapes();
				break;
			case GUESS_CIRCLE:
				if(tfEstimate.getText().isEmpty() && tfEstimate.isVisible()) {
					infoLabel.setText("Bitte etwas als Schätzung eingeben!");
					break;
				}
				else {
					// TODO ungültige eingaben escapen

					if(tfEstimate.isVisible()) // for initialisation
					{
						double guessedRatio = Double.valueOf(tfEstimate.getText());
						overallRatio.add(calculateResult(circleRatio, guessedRatio));
					}

					tfEstimate.clear();
					newShapes();
					break;
				}
		}
	}

	private void newShapes()
	{
		// generate a random number for the value which the user should guess
		shapeSize = ThreadLocalRandom.current().nextInt(2, 11);
		shapeChange();
	}

	// berechne x fuer (wahrgenommenes verh�ltnis) = (tats�chliches verh�ltnis)^x
	private double calculateResult(double realRation , double estimatedRatio) {

		return Math.log(estimatedRatio) / Math.log(realRation);

	}
}
