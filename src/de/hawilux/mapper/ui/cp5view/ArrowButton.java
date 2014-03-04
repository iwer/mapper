package de.hawilux.mapper.ui.cp5view;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import controlP5.Button;
import controlP5.CColor;
import controlP5.ControllerView;

public class ArrowButton implements ControllerView<Button>, PConstants {
    PApplet parent;
    PShape arrow;
    CColor colorScheme;

    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    public ArrowButton(PApplet parent, int orientation) {
        this.parent = parent;
        arrow = createArrow();
        colorScheme = new CColor();
        if (orientation == ArrowButton.LEFT) {
            arrow.rotate(HALF_PI * 3);
            arrow.translate(-100, 0);
        } else if (orientation == ArrowButton.DOWN) {
            arrow.rotate(PI);
            arrow.translate(-100, -100);
        } else if (orientation == ArrowButton.RIGHT) {
            arrow.rotate(HALF_PI);
            arrow.translate(0, -100);
        }
    }

    PShape createArrow() {
        arrow = parent.createShape();
        arrow.beginShape();
        arrow.fill(0, 0, 255);
        arrow.noStroke();
        arrow.vertex(50, 0);
        arrow.vertex(100, 66);
        arrow.vertex(75, 66);
        arrow.vertex(75, 100);
        arrow.vertex(25, 100);
        arrow.vertex(25, 66);
        arrow.vertex(0, 66);
        arrow.endShape(CLOSE);

        return arrow;
    }

    public void display(PApplet theApplet, Button theButton) {
        theApplet.pushMatrix();
        if (theButton.isInside()) {
            if (theButton.isPressed()) { // button is pressed
                arrow.setFill(colorScheme.getActive());
            } else { // mouse hovers the button
                arrow.setFill(colorScheme.getForeground());
            }
        } else { // the mouse is located outside the button area
            arrow.setFill(colorScheme.getBackground());
        }

        theApplet.shape(arrow, 0, 0);

        // center the caption label
        int x = theButton.getWidth() / 2
                - theButton.getCaptionLabel().getWidth() / 2;
        int y = theButton.getHeight() / 2
                - theButton.getCaptionLabel().getHeight() / 2;

        theApplet.translate(x, y);
        theButton.getCaptionLabel().draw(theApplet);

        theApplet.popMatrix();
    }
}
