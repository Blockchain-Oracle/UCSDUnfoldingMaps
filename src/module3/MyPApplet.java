package module3;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * MyPApplet
 */
public class MyPApplet extends PApplet {
    PImage bImage;

    public void setup() {

        size(600, 800);
        background(200, 200, 200);
        bImage = loadImage("University of California San Diego/UCSDUnfoldingMaps/data/palmTrees.jpg", "jpg");
        bImage.resize(0, height);
        image(bImage, 0, 0);
    }

    public void draw() {
        int c010r[] = getC010r(second());
        fill(c010r[0], c010r[1], c010r[2]);
        ellipse(width / 4, height / 5, width / 4, height / 5);
    }

    private int[] getC010r(float secnds) {
        int[] rbg = new int[3];
        float abs = Math.abs(30 - secnds);
        float rati0 = abs / 30;
        rbg[0] = (int) (rati0 * 255);
        rbg[1] = (int) (rati0 * 255);
        rbg[2] = 0;
        return rbg;
    }

    public static void main(String[] args) {
        PApplet.main("module3.MyPApplet");
    }
}