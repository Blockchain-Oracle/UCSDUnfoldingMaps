package module3;

import processing.core.PApplet;

public class MyDisplay extends PApplet {

    public void setup() {

        size(400, 400);
        background(200, 200, 200);

    }

    public void draw() {
        fill(255, 255, 0);
        ellipse(200, 200, 300, 300);
        fill(GRAY);
        ellipse(120, 170, 50, 100);
        ellipse(280, 170, 50, 100);
        fill(188, 201, 110);
        noFill();
        arc(200, 280, 75, 75, 0, PI);

    }

    public static void main(String[] args) {
        PApplet.main("module3.MyDisplay");
    }
}
