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

public class Point implements PConstants {
    private int RADIUS = 15;
    private int RADIUS2 = RADIUS * 2;
    private PApplet parent;

    // boolean selected;
    private boolean showHelper;

    public int id;

    private PVector location;
    private ArrayList<Integer> connectedEdges;

    private PShape circle, dot;

    public Point(PApplet parent_, int id_, int x_, int y_, boolean helper_) {
        parent = parent_;
        id = id_;
        location = new PVector(x_, y_);
        // selected = false;
        showHelper = helper_;
        connectedEdges = new ArrayList<Integer>();
        update();
    }

    public int getId() {
        return id;
    }

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

    public void displayHelper(boolean selected, boolean mouseOverColor) {
        int c = getColor(true, selected, mouseOverColor);
        parent.pushMatrix();
        parent.fill(c);
        parent.text(id, location.x - 20, location.y - 20);
        parent.popMatrix();
    }
   
    boolean mouseOver(PVector v) {
        PVector dist = new PVector(parent.mouseX, parent.mouseY);
        dist.sub(v);
        if (dist.magSq() > RADIUS2) {
            return false;
        } else {
            return true;
        }
    }

    public int select() {
        if (mouseOver(location)) {
            return id;
        } else {
            return -1;
        }
    }

    public void move() {
        // if (selected) {
        location.x = parent.mouseX;
        location.y = parent.mouseY;
        update();
        // }
    }

    public void move(int dx, int dy) {
        // if (selected) {
        location.x += dx;
        location.y += dy;
        update();
        // }
    }

    public void update() {
        circle = parent.createShape(ELLIPSE, location.x, location.y,
                RADIUS, RADIUS);
        circle.setFill(false);
        circle.setStrokeWeight(3);
        dot = parent.createShape(ELLIPSE, location.x, location.y, 2,
                2);
        dot.setFill(false);
        dot.setStrokeWeight(3);
    }

    public void addConnectedEdge(int id_) {
        connectedEdges.add(id_);
    }

    public void removeConnectedEdge(int id_) {
        Integer toRemove = new Integer(id_);
        if (connectedEdges.contains(toRemove)) {
            connectedEdges.remove(toRemove);
        }
        // println("Connected Edges at point " + id + ":" + connectedEdges);
    }

    public ArrayList<Integer> getConnectedEdges() {
        return connectedEdges;
    }

    public PVector getLocation() {
        return location;
    }

    public float getX() {
        return location.x;
    }

    public float getY() {
        return location.y;
    }

    public boolean equals(Point other) {
        return (this.parent.equals(other.parent)
                && this.showHelper == other.showHelper && this.id == other.id && this.location
                    .equals(other.location));
    }
}
