package htw.model;

import java.awt.*;

public class Square {

    private float sideLength;
    private Color color;

    public Square() {
        this.sideLength = 0;
        this.color = Color.RED;

    }

    public Square(float radius) {
        this.sideLength = radius;
    }

    public float getSideLength() {
        return sideLength;
    }

    public void setSideLength(float sideLength) {
        this.sideLength = sideLength;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
