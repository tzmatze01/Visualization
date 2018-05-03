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
import javafx.scene.paint.Color;
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

	private boolean answer;
	private boolean lastTest = false;
	private int testFlag = 1;
	private int xPos;
	private int yPos;
	private int sleepTime;
	private Random random = new Random();

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

	@FXML
	private ComboBox<Answer> comboBox = new ComboBox<>();

	@FXML
	public void initialize() {

		label.setText("Zum Starten der Tests bitte auf den Button Test 1 clicken.");
		comboBox.setItems(FXCollections.observableArrayList(Answer.values()));
		comboBox.setDisable(true);
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
		
		if (button.getText() == "Ende") {
			System.exit(0);
		}
		
		if (testFlag == 1) {
			sleepTime = 2500;
			test1();
		}
		if (testFlag == 2) {
			sleepTime = 2500;
			test2();
		}
		if (testFlag == 3) {
			sleepTime = 2500;
			test3();
			lastTest = true;
		}

		testFlag++;
	}

	@FXML
	private void buttonWasClicked() {

		try {
			Thread.sleep(sleepTime);
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
			question = "Haben Sie einen gro�en blauen Kreis gesehen?";
		}
		if (testFlag == 4) {
			question = "Haben Sie einen blaues Viereck gesehen?";
		}

		label.setText(question);

	}

	public void testValidation(boolean answer) {

		String validation = null;
		if (answer == true) {
			gc.fillText("Die Antwort war korrekt.", canvasWidth / 3, canvasHeight / 2);
		} else {
			gc.fillText("Die Antwort war nicht korrekt.", canvasWidth / 3, canvasHeight / 2);
		}

	}

	public void test1() {
		drawRandomCircles(20, 50, Color.BLUE);
		drawRandomCircles(20, 50, Color.BLUE);

	}

	public void test2() {
		drawRandomCircles(20, 50, Color.BLUE);
		drawRandomCircles(40, 1, Color.BLUE);
	}

	public void test3() {
		drawRandomCircles(20, 50, Color.BLUE);
		drawRandomSquares(20, 1, Color.BLUE);
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

	private void drawCircles(int radius, int xPos, int yPos, Color color) {

		Circle circle = new Circle(radius, color);
		circle.drawCircle(gc, xPos, yPos);

	}

}
