/*
 *   A 2D Video Mapping Tool created from experiences in the HAWilux project
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this program; if not, write to the Free Software Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Point class
 *
 * represents a point, the most basic "shape"
 *
 */
package de.hawilux.mapper.shapes;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import de.hawilux.mapper.ui.MapperControlFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class Point.
 */
public class Point extends Shape implements PConstants, IPoint {

    /** The radius. */
    private int                RADIUS  = 16;

    /** The RADIUS to the power of 2. */
    protected int              RADIUS2 = RADIUS * 2;

    /** The show helper. */
    boolean                    showHelper;

    protected boolean          updated;

    /** The connected edges. */
    private ArrayList<Integer> connectedEdges;

    /** The dot. */
    private PShape             circle;

    /**
     * Instantiates a new point.
     * 
     * @param parent_
     *            the parent_
     * @param id_
     *            the id_
     * @param x_
     *            the x_
     * @param y_
     *            the y_
     * @param helper_
     *            the helper_
     */
    public Point(PGraphics layer, PApplet parent_, PShape shapeGroup_, int id_,
            int x_, int y_, boolean helper_) {
        super(layer, parent_, shapeGroup_, id_);
        centroid = new PVector(x_, y_);
        // selected = false;
        showHelper = helper_;
        connectedEdges = new ArrayList<Integer>();
        update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#isUpdated()
     */
    @Override
    public boolean isUpdated() {
        return updated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#setUpdated(boolean)
     */
    @Override
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    /**
     * Gets the color.
     * 
     * @param config
     *            the config
     * @param selected
     *            the selected
     * @param mouseOverColor
     *            the mouse over color
     * @return the color
     */
    int getColor(boolean config, boolean selected, boolean mouseOverColor) {
        int c = 0;
        if (!config) {
            c = layer.color(255, 0, 0);
        } else if (mouseOver(centroid) && mouseOverColor) {
            if (selected) {
                c = layer.color(0, 255, 255);
            } else {
                c = layer.color(255, 255, 0);
            }
        } else {
            if (selected) {
                c = layer.color(0, 255, 0);
            } else {
                c = layer.color(255, 0, 0);
            }
        }
        return c;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#display(boolean, boolean, boolean)
     */
    @Override
    public void display(boolean config, boolean selected, boolean mouseOverColor) {
        int c = getColor(config, selected, mouseOverColor);

        shapeGroup.getChild("Point_" + id + "_circle").setStroke(c);
        shapeGroup.getChild("Point_" + id + "_dot").setStroke(c);

        // parent.pushMatrix();
        //
        // int c = getColor(config, selected, mouseOverColor);
        // circle.setStroke(c);
        // parent.shapeMode(CENTER);
        // circle.resetMatrix();
        // circle.translate(centroid.x, centroid.y);
        // parent.shape(circle);
        // shape.setStroke(c);
        // shape.resetMatrix();
        // shape.translate(centroid.x, centroid.y);
        // parent.shape(shape);
        // ;
        //
        // parent.popMatrix();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#displayHelper(boolean, boolean)
     */
    @Override
    public void displayHelper(boolean selected, boolean mouseOverColor) {
        int c = getColor(true, selected, mouseOverColor);
        layer.pushMatrix();
        layer.fill(c);
        layer.text(id, centroid.x - 20, centroid.y - 20);
        layer.popMatrix();
    }

    /**
     * Mouse over.
     * 
     * @param v
     *            the v
     * @return true, if successful
     */
    boolean mouseOver(PVector v) {
        PVector dist = new PVector(MapperControlFrame.projectorMouseX,
                MapperControlFrame.projectorMouseY);
        dist.sub(v);
        if (dist.magSq() > RADIUS2 * 1.5) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#select()
     */
    @Override
    public int select() {
        if (mouseOver(centroid)) {
            return id;
        } else {
            return -1;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#move()
     */
    @Override
    public void move() {
        centroid.x = MapperControlFrame.projectorMouseX;
        centroid.y = MapperControlFrame.projectorMouseY;
        updated = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#move(int, int)
     */
    @Override
    public void move(int dx, int dy) {
        centroid.x += dx;
        centroid.y += dy;
        updated = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#update()
     */
    @Override
    public void update() {
        shapeGroup = layer.createShape(GROUP);
        int c = getColor(true, true, mouseOver(centroid));
        // TODO: Shapes need a method that updates their color in each frame.
        // To do this without changing the shapeGroup the need to be accessed
        // by shapeGroup.getChild(<indexOfSubShape>)
        layer.pushMatrix();
        // parent.shapeMode(CENTER);
        circle = layer.createShape(ELLIPSE, 0, 0, RADIUS, RADIUS);
        circle.setFill(false);
        circle.setStrokeWeight(3);
        circle.setStroke(c);
        circle.resetMatrix();
        circle.translate(centroid.x - RADIUS / 2, centroid.y - RADIUS / 2);
        circle.setName("Point_" + id + "_circle");
        shapeGroup.addChild(circle);

        // parent.shapeMode(CORNER);
        shape = layer.createShape(ELLIPSE, 0, 0, 2, 2);
        shape.setFill(false);
        shape.setStrokeWeight(3);
        shape.setStroke(c);
        shape.resetMatrix();
        shape.translate(centroid.x - 1, centroid.y - 1);
        shape.setName("Point_" + id + "_dot");
        shapeGroup.addChild(shape);
        layer.popMatrix();

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#addConnectedEdge(int)
     */
    @Override
    public void addConnectedEdge(int id_) {
        connectedEdges.add(id_);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#removeConnectedEdge(int)
     */
    @Override
    public void removeConnectedEdge(int id_) {
        Integer toRemove = new Integer(id_);
        if (connectedEdges.contains(toRemove)) {
            connectedEdges.remove(toRemove);
        }
        // println("Connected Edges at point " + id + ":" + connectedEdges);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#getConnectedEdges()
     */
    @Override
    public ArrayList<Integer> getConnectedEdges() {
        return connectedEdges;
    }

    /**
     * Gets the location.
     * 
     * @return the location
     * @deprecated use getCentroid()
     */
    @Deprecated
    public PVector getLocation() {
        return centroid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#getX()
     */
    @Override
    public float getX() {
        return centroid.x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#getY()
     */
    @Override
    public float getY() {
        return centroid.y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hawilux.mapper.shapes.IPoint#equals(de.hawilux.mapper.shapes.Point)
     */
    public boolean equals(Point other) {
        return (this.parent.equals(other.parent)
                && this.showHelper == other.showHelper && this.id == other.id && this.centroid
                    .equals(other.centroid));
    }
}
