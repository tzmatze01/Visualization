package htw.controller;

import htw.model.Circle;
import htw.model.Square;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;






public class Controller {

	private enum Answer {
		JA("Ja"), NEIN("Nein");

		private final String name;

		Answer(String s) {
			name = s;
		}

		public String toString() {
			return this.name;
		}

	}

	private enum DpTime {

		S2("2 s", 2000),
		S1("1 s", 1000),
		MS500("500 ms", 500),
		MS250("250 ms", 250),
		MS150("150 ms", 150);

		private final int value;
		private final String text;

		DpTime(String text, int value)
		{
			this.text = text;
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public String toString() {
			return this.text;
		}

	}

	private boolean answer;
	private boolean lastTest = false;
	private int testFlag = 1;
	private int xPos;
	private int yPos;
	private boolean showDiffObject;

	private Random random = new Random();

	@FXML
	private ComboBox<DpTime> cb_displayTime = new ComboBox<>();

	@FXML
	private Button button;

	@FXML
	private Button buttonCheck;

	@FXML
	private Canvas canvas;

	@FXML
	private Label label = new Label();

	private GraphicsContext gc;

	private int canvasHeight = 600;
	private int canvasWidth = 800;
	private int canvasArea = canvasHeight * canvasWidth;

	private int displayTime = 2000;

	@FXML
	private ComboBox<Answer> comboBox = new ComboBox<>();

	@FXML
	public void initialize() {

		label.setText("Folgend erscheinen mehrere Objekte.\nZum Starten der Tests Anzeigezeit wählen und den Button Test 1 clicken.");
		comboBox.setItems(FXCollections.observableArrayList(Answer.values()));
		comboBox.setDisable(true);
		cb_displayTime.setItems(FXCollections.observableArrayList(DpTime.S2, DpTime.S1, DpTime.MS500, DpTime.MS250, DpTime.MS150));
		button.setText("Test " + testFlag);
		buttonCheck.setDisable(true);
		canvas.setHeight(canvasHeight);
		canvas.setWidth(canvasWidth);

		gc = canvas.getGraphicsContext2D();
		gc.setFont(new Font(20));
	}

	@FXML
	private void buttonCheckClick() {
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
		testValidation(answer);
	}

	@FXML
	private void buttonClick() {
		gc.clearRect(0, 0, canvasWidth, canvasHeight);


		showDiffObject = random.nextBoolean();

		System.out.println("showDiffObject: "+showDiffObject);

		if (button.getText() == "Ende") {
			System.exit(0);
		}
		
		if (testFlag == 1) {
			test1();
		}
		if (testFlag == 2) {
			test2();
		}
		if (testFlag == 3) {
			test3();
		}
		if(testFlag == 4) {
			test4();
		}
		if(testFlag == 5) {
			test5();
			lastTest = true;
		}

		testFlag++;
	}

	@FXML
	private void buttonWasClicked() {

		try {
			Thread.sleep(displayTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		updateGui();

	}

	private void updateGui() {

		gc.clearRect(0, 0, canvasWidth, canvasHeight);

		if (lastTest == false) {
			button.setText("Test " + testFlag);
			comboBox.setDisable(false);
		} else {
			button.setText("Ende");
			comboBox.setDisable(false);
		}
		
		testQuestion();

	}

	@FXML
	public void setDisplayTime() {

		displayTime = cb_displayTime.getValue().getValue();
	}

	@FXML
	public void chooseAnswer() {

		switch (comboBox.getValue()) {
		case JA:
			answer = true;
			buttonCheck.setDisable(false);
			break;
		case NEIN:
			answer = false;
			button.setDisable(false);
			break;
		default:
			break;
		}
	}

	/*
	 * testzeug vlt sp�ter in eigene klasse
	 */

	public void testQuestion() {
		String question = null;

		if (testFlag == 2) {
			question = "Haben Sie einen schwarzen Kreis gesehen?";
		}
		if (testFlag == 3) {
			question = "Haben Sie einen grossen blauen Kreis gesehen?";
		}
		if (testFlag == 4) {
			question = "Haben Sie einen blaues Viereck gesehen?";
		}
		if (testFlag == 5) {
			question = "Haben Sie das gedrehte Dreieck gesehen?";
		}
		if (testFlag == 6) {
			question = "Haben Sie einen rosa Elefant gesehn?";
		}

		label.setText(question);

	}

	public void testValidation(boolean answer) {

		System.out.println("answer was: "+answer+" result: "+(answer == showDiffObject));

		if (answer == showDiffObject) {
			gc.fillText("Die Antwort war korrekt.", canvasWidth / 3, canvasHeight / 2);
		} else {
			gc.fillText("Die Antwort war nicht korrekt.", canvasWidth / 3, canvasHeight / 2);
		}

	}

	// Schwarzer Kreis
	public void test1() {
		if (showDiffObject) {
			drawRandomCircles(20, 49, Color.BLUE);
			drawRandomCircles(20, 1, Color.BLACK);
		}
		else
			drawRandomCircles(20, 50, Color.BLUE);


	}

	// Grosser blauer Kreis
	public void test2() {
		if (showDiffObject)
		{
			drawRandomCircles(20, 49, Color.BLUE);
			drawRandomCircles(40, 1, Color.BLUE);
		}
		else
			drawRandomCircles(20, 50, Color.BLUE);

	}

	// Ein Quadrat und Kreise
	public void test3() {
		if (showDiffObject) {
			drawRandomCircles(20, 49, Color.BLUE);
			drawRandomSquares(20, 1, Color.BLUE);
		}
		else
			drawRandomCircles(20, 50, Color.BLUE);
	}

	// Ein verdrehtes Dreieck
	public void test4() {

		if (showDiffObject) {
			drawRandomTriangles(showDiffObject, 49, Color.RED);
			drawRandomTriangles(false, 49, Color.RED);
		}
		else
		{
			drawRandomTriangles(showDiffObject, 50, Color.RED);
		}


	}

	// Ein rosa Elefant
	public void test5() {

		if (showDiffObject) {
			drawRandomCircles(20, 50, Color.BLUE);
			drawRandomSquares(20, 1, Color.BLUE);
		}
		else
		{
		}
	}





	/*
	 * shapes
	 */

	public void drawRandomSquares(int sideLength, int number, Color color) {
		for (int i = 0; i < number; i++) {
			xPos = random.nextInt(canvasWidth - sideLength);
			yPos = random.nextInt(canvasHeight - sideLength);
			drawSquares(sideLength, xPos, yPos, color);
		}
	}

	private void drawSquares(int sideLength, int xPos, int yPos, Color color) {

		Square square = new Square(sideLength, color);
		square.draw(gc, xPos, yPos);


	}

	public void drawRandomCircles(int radius, int number, Color color) {

		for (int i = 0; i < number; i++) {
			xPos = random.nextInt(canvasWidth - radius);
			yPos = random.nextInt(canvasHeight - radius);

			drawCircles(radius, xPos, yPos, color);
		}
	}

	public void drawRandomTriangles(boolean upright, int number, Color color) {

		for (int i = 0; i < number; i++) {
			int xFaktor = random.nextInt(30);
			int yFaktor = random.nextInt(40);

			drawTriangle(upright, xFaktor, yFaktor, color);
		}
	}

	private void drawCircles(int radius, int xPos, int yPos, Color color) {

		Circle circle = new Circle(radius, color);
		circle.drawCircle(gc, xPos, yPos);

	}

	// myb draw rectangle

	public void drawTriangle(boolean upright, int xFaktor, int yFaktor, Color color)
	{
		gc.setStroke(color);
		gc.beginPath();
		gc.moveTo(5 * xFaktor, 5 * yFaktor);

		if(upright)
			gc.lineTo(5 * xFaktor, 20 * yFaktor);
		else
			gc.moveTo(20 * xFaktor, 5 * yFaktor);

		gc.lineTo(20 * xFaktor, 20 * yFaktor);
		gc.lineTo(5 * xFaktor, 5 * yFaktor);
		gc.stroke();
	}

	public void drawElephant()
	{
		Image image = new Image("/pink_elephant.png");
		gc.drawImage(image, canvasWidth / 2, canvasHeight / 2);
	}

}
