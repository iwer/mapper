package de.hawilux.mapper.tools;

import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.ui.GuiElement;
import processing.core.PApplet;
import processing.core.PImage;
import gab.opencv.Histogram;
import gab.opencv.OpenCV;

public class MovementQuantifier implements GuiElement {
    private OpenCV    opencv;
    private PImage    before, after, grayDiff;

    private Histogram grayHist;
    private int       histogramWidth;
    private float     movement;

    public MovementQuantifier(PApplet parent) {
        before = new PImage(640, 480);
        after = new PImage(640, 480);
        grayDiff = new PImage(640, 480);

        opencv = new OpenCV(parent, before);
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
        histogramWidth = 4;
        opencv.blur(4);
        opencv.diff(after);
        grayDiff = opencv.getSnapshot();

        // calc histogram
        opencv.loadImage(grayDiff);
        grayHist = opencv.findHistogram(opencv.getGray(), histogramWidth);
        
        
        // calculate movement from histogram
        float[] histValues = new float[histogramWidth];
        grayHist.getMat().get(0, 0, histValues);
        movement = (histValues[2] + histValues[3]);
        
        after = before.get();
    }

    public void draw() {

    }

    public float getMovement() {
        return movement;
    }
}
