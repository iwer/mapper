package de.hawilux.mapper.shapes;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public abstract class Shape {
    /** The parent. */
    protected PApplet   parent;

    /** The shape. */
    protected PShape    shapeGroup;

    /** The shape. */
    protected PShape    shape;

    protected PGraphics layer;

    /** The id. */
    protected int       id;

    /** The centroid. */
    protected PVector   centroid;

    public Shape(PGraphics layer_, PApplet parent_, PShape shapeGroup_, int id_) {
        parent = parent_;
        layer = layer_;
        shapeGroup = layer.createShape(PConstants.GROUP);
        id = id_;
        centroid = new PVector();
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the centroid.
     * 
     * @return the centroid
     */
    public PVector getCentroid() {
        return centroid;
    }

    /**
     * Gets the shape.
     * 
     * @return the shape
     */
    public PShape getShape() {
        return shapeGroup;
    }

    public abstract void update();

    public abstract void display(boolean config, boolean selected,
            boolean mouseOverColor);

    public abstract int select();
}
