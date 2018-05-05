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

import java.io.File;
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

	private enum ObjCount {

		N80("80 Objekte", 80),
		N70("70 Objeckte", 70),
		N50("50 Objekte", 50),
		N30("30 Objekte", 30),
		N20("20 Objekte", 20);

		private final int value;
		private final String text;

		ObjCount(String text, int value)
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
	private int numObjects = 50;

	private Random random = new Random();

	@FXML
	private ComboBox<ObjCount> cb_numObjects = new ComboBox<>();
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

	private int canvasHeight = 300;
	private int canvasWidth = 400;
	private int canvasArea = canvasHeight * canvasWidth;

	private int displayTime = 2000;

	@FXML
	private ComboBox<Answer> comboBox = new ComboBox<>();

	@FXML
	public void initialize() {

		label.setText("Folgend erscheinen mehrere Objekte.\nZum Starten der Tests Anzeigezeit & Objektanzahl wählen\nund den Button Test 1 clicken.");
		comboBox.setItems(FXCollections.observableArrayList(Answer.values()));
		comboBox.setDisable(true);
		cb_displayTime.setItems(FXCollections.observableArrayList(DpTime.S2, DpTime.S1, DpTime.MS500, DpTime.MS250, DpTime.MS150));
		cb_numObjects.setItems(FXCollections.observableArrayList(ObjCount.N80, ObjCount.N70, ObjCount.N50, ObjCount.N30, ObjCount.N20));
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
		label.setText("...");
		buttonCheck.setDisable(true);
		comboBox.setDisable(true);
		comboBox.setPromptText("Bitte Wählen!");


		showDiffObject = random.nextBoolean();

		//System.out.println("showDiffObject: "+showDiffObject);


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
		if(testFlag == 6) {
			System.exit(0);
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
	public void setNumObjects() {
		numObjects = cb_numObjects.getValue().getValue();
	}

	@FXML
	public void setDisplayTime() {

		displayTime = cb_displayTime.getValue().getValue();
	}

	@FXML
	public void chooseAnswer() {

		buttonCheck.setDisable(false);

		switch (comboBox.getValue()) {
		case JA:
			answer = true;
			//buttonCheck.setDisable(false);
			break;
		case NEIN:
			answer = false;
			//button.setDisable(false);
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
			question = "Haben Sie ein rotes Dreieck gesehn?";
		}
		if (testFlag == 7) {
			question = "Haben Sie einen rosa Elefant gesehn?";
		}

		label.setText(question);

	}

	public void testValidation(boolean answer) {

		System.out.println("answer was: "+answer+" result: "+(answer == showDiffObject));

		if (answer == showDiffObject) {
			gc.fillText("Die Antwort war korrekt.", canvasWidth / 3, canvasHeight / 2);
		} else {
			gc.fillText("Die Antwort war nicht korrekt.", canvasWidth / 4, canvasHeight / 2);
		}

	}

	// Schwarzer Kreis
	public void test1() {
		if (showDiffObject) {
			drawRandomCircles(20, numObjects-1, Color.BLUE);
			drawRandomCircles(20, 1, Color.BLACK);
		}
		else
			drawRandomCircles(20, numObjects, Color.BLUE);


	}

	// Grosser blauer Kreis
	public void test2() {
		if (showDiffObject)
		{
			drawRandomCircles(20, numObjects-1, Color.BLUE);
			drawRandomCircles(40, 1, Color.BLUE);
		}
		else
			drawRandomCircles(20, numObjects, Color.BLUE);

	}

	// Ein Quadrat und Kreise
	public void test3() {
		if (showDiffObject) {
			drawRandomCircles(20, numObjects-1, Color.BLUE);
			drawRandomSquares(20, 1, Color.BLUE);
		}
		else
			drawRandomCircles(20, numObjects, Color.BLUE);
	}

	// Ein verdrehtes Dreieck
	public void test4() {

		if (showDiffObject) {
			drawRandomTriangles(20, showDiffObject, numObjects-1);
			drawRandomTriangles(20, false, 1);
		}
		else
		{
			drawRandomTriangles(20, showDiffObject, numObjects);
		}


	}

	// Ein Dreieck, 2 unterschiedliche andere Formen
	public void test5() {

		if (showDiffObject) {
			drawRandomCircles(20, numObjects/2, Color.RED);
			drawRandomSquares(20, numObjects/2 - 1, Color.RED);
			drawRandomTriangles(20, false, 1);
		}
		else
		{
			drawRandomCircles(20, numObjects/2, Color.RED);
			drawRandomSquares(20, numObjects/2 , Color.RED);
		}
	}


	// Ein rosa Elefant
	public void test6() {

		if (showDiffObject) {
			drawElephant();
			drawRandomCircles(20, numObjects/2, Color.BLUE);
			drawRandomSquares(20, numObjects/2 - 1, Color.BLUE);
		}
		else
		{
			drawRandomCircles(20, numObjects/2, Color.BLUE);
			drawRandomSquares(20, numObjects/2 - 1, Color.BLUE);
			drawElephant();
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

	public void drawRandomStars(int sideLength, int number) {


		Image star = new Image(new File("resources/triangle_left.png").toURI().toString());
		int imgHeight = (int)star.getWidth() / 2;
		int imgWidth = (int)star.getWidth() / 2;

		for (int i = 0; i < number; i++) {
			xPos = random.nextInt(canvasWidth - imgWidth);
			yPos = random.nextInt(canvasHeight - imgHeight);

			gc.drawImage(star ,xPos, yPos, sideLength, sideLength);
		}
	}



	public void drawRandomTriangles(int sideLength, boolean upright, int number) {


		Image triangle_left = new Image(new File("resources/triangle_left.png").toURI().toString());
		Image triangle = new Image(new File("resources/triangle.png").toURI().toString());

		int imgHeight = (int)triangle.getHeight() / 2;
		int imgWidth = (int)triangle.getWidth() / 2;

		for (int i = 0; i < number; i++) {
			xPos = random.nextInt(canvasWidth - imgWidth);
			yPos = random.nextInt(canvasHeight - imgHeight);

			if(upright)
				gc.drawImage(triangle ,xPos, yPos, sideLength, sideLength);
			else
				gc.drawImage(triangle_left ,xPos, yPos, sideLength, sideLength);
		}
	}

	private void drawCircles(int radius, int xPos, int yPos, Color color) {

		Circle circle = new Circle(radius, color);
		circle.drawCircle(gc, xPos, yPos);

	}

	public void drawTriangle(Image image, int sideLength, boolean upright, int xPos, int yPos)
	{
		gc.drawImage(image,xPos, yPos, sideLength, sideLength);
	}

	public void drawElephant()
	{
		Image image = new Image(new File("resources/pink_elephant.png").toURI().toString());
		gc.drawImage(image, canvasWidth / 2, canvasHeight / 2, 60, 60);
	}

}
