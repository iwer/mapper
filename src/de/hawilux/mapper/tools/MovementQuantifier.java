package de.hawilux.mapper.tools;

import gab.opencv.Histogram;
import gab.opencv.OpenCV;
import processing.core.PApplet;
import processing.core.PImage;
import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.ui.GuiElement;

public class MovementQuantifier implements GuiElement {
    private static final int imageHeight = 480;
    private static final int imageWidth  = 640;
    private OpenCV           opencv;
    private PImage           before, after, grayDiff;

    private Histogram        grayHist;
    private int              histogramWidth;
    private float            movement;
    public int               multiplier;

    public MovementQuantifier(PApplet parent) {
        before = new PImage(imageWidth, imageHeight);
        after = new PImage(imageWidth, imageHeight);
        grayDiff = new PImage(imageWidth, imageHeight);

        opencv = new OpenCV(parent, before);
        multiplier = 4;
        histogramWidth = 4;
    }

    @Override
    public void addControllersToGui(Gui gui_) {
        // TODO Auto-generated method stub

    }

    public void update(PImage newImage) {
        // save last image and get new image;
        before = newImage;

        // image diff
        opencv.loadImage(before);
        opencv.blur(histogramWidth);
        opencv.diff(after);
        grayDiff = opencv.getSnapshot();

        // calc histogram
        opencv.loadImage(grayDiff);
        grayHist = opencv.findHistogram(opencv.getGray(), histogramWidth);

        // calculate movement from histogram
        float[] histValues = new float[histogramWidth];
        grayHist.getMat().get(0, 0, histValues);
        movement = PApplet.min((histValues[2] + histValues[3]) * multiplier, 1);

        after = before.get();
    }

    public void draw() {

    }

    public float getMovement() {
        return movement;
    }
}
