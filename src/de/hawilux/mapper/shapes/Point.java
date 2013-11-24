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
import processing.core.PShape;
import processing.core.PVector;

// TODO: Auto-generated Javadoc
/**
 * The Class Point.
 */
public class Point implements PConstants {

    /** The radius. */
    private int RADIUS = 15;

    /** The RADIUS to the power of 2. */
    private int RADIUS2 = RADIUS * 2;

    /** The parent. */
    private PApplet parent;

    /** The show helper. */
    private boolean showHelper;

    /** The id. */
    public int id;

    /** The location. */
    private PVector location;

    /** The connected edges. */
    private ArrayList<Integer> connectedEdges;

    /** The dot. */
    private PShape circle, dot;

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
    public Point(PApplet parent_, int id_, int x_, int y_, boolean helper_) {
        parent = parent_;
        id = id_;
        location = new PVector(x_, y_);
        // selected = false;
        showHelper = helper_;
        connectedEdges = new ArrayList<Integer>();
        update();
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
            c = parent.color(255, 0, 0);
        } else if (mouseOver(location) && mouseOverColor) {
            if (selected) {
                c = parent.color(0, 255, 255);
            } else {
                c = parent.color(255, 255, 0);
            }
        } else {
            if (selected) {
                c = parent.color(0, 255, 0);
            } else {
                c = parent.color(255, 0, 0);
            }
        }
        return c;
    }

    /**
     * Display.
     * 
     * @param config
     *            the config
     * @param selected
     *            the selected
     * @param mouseOverColor
     *            the mouse over color
     */
    public void display(boolean config, boolean selected, boolean mouseOverColor) {
        parent.pushMatrix();

        int c = getColor(config, selected, mouseOverColor);
        circle.setStroke(c);
        parent.shapeMode(CENTER);
        parent.shape(circle);
        dot.setStroke(c);
        parent.shape(dot);

        parent.popMatrix();
    }

    /**
     * Display helper.
     * 
     * @param selected
     *            the selected
     * @param mouseOverColor
     *            the mouse over color
     */
    public void displayHelper(boolean selected, boolean mouseOverColor) {
        int c = getColor(true, selected, mouseOverColor);
        parent.pushMatrix();
        parent.fill(c);
        parent.text(id, location.x - 20, location.y - 20);
        parent.popMatrix();
    }

    /**
     * Mouse over.
     * 
     * @param v
     *            the v
     * @return true, if successful
     */
    boolean mouseOver(PVector v) {
        PVector dist = new PVector(parent.mouseX, parent.mouseY);
        dist.sub(v);
        if (dist.magSq() > RADIUS2) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Select.
     * 
     * @return the int
     */
    public int select() {
        if (mouseOver(location)) {
            return id;
        } else {
            return -1;
        }
    }

    /**
     * Move.
     */
    public void move() {
        // if (selected) {
        location.x = parent.mouseX;
        location.y = parent.mouseY;
        update();
        // }
    }

    /**
     * Move.
     * 
     * @param dx
     *            the dx
     * @param dy
     *            the dy
     */
    public void move(int dx, int dy) {
        // if (selected) {
        location.x += dx;
        location.y += dy;
        update();
        // }
    }

    /**
     * Update.
     */
    public void update() {
        circle = parent.createShape(ELLIPSE, location.x, location.y, RADIUS,
                RADIUS);
        circle.setFill(false);
        circle.setStrokeWeight(3);
        dot = parent.createShape(ELLIPSE, location.x, location.y, 2, 2);
        dot.setFill(false);
        dot.setStrokeWeight(3);
    }

    /**
     * Adds the connected edge.
     * 
     * @param id_
     *            the id_
     */
    public void addConnectedEdge(int id_) {
        connectedEdges.add(id_);
    }

    /**
     * Removes the connected edge.
     * 
     * @param id_
     *            the id_
     */
    public void removeConnectedEdge(int id_) {
        Integer toRemove = new Integer(id_);
        if (connectedEdges.contains(toRemove)) {
            connectedEdges.remove(toRemove);
        }
        // println("Connected Edges at point " + id + ":" + connectedEdges);
    }

    /**
     * Gets the connected edges.
     * 
     * @return the connected edges
     */
    public ArrayList<Integer> getConnectedEdges() {
        return connectedEdges;
    }

    /**
     * Gets the location.
     * 
     * @return the location
     */
    public PVector getLocation() {
        return location;
    }

    /**
     * Gets the x.
     * 
     * @return the x
     */
    public float getX() {
        return location.x;
    }

    /**
     * Gets the y.
     * 
     * @return the y
     */
    public float getY() {
        return location.y;
    }

    /**
     * Equals.
     * 
     * @param other
     *            the other
     * @return true, if successful
     */
    public boolean equals(Point other) {
        return (this.parent.equals(other.parent)
                && this.showHelper == other.showHelper && this.id == other.id && this.location
                    .equals(other.location));
    }
}
