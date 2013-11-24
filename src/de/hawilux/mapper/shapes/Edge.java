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
 * Edge class
 * 
 * represents a line between two Points
 *
 */
package de.hawilux.mapper.shapes;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Edge implements PConstants{
    private PApplet parent;
    private PShape shape;
    private PShape arrow;
    private PShape grabber;
    private int id;

    Point a, b;

    private ArrayList<Integer> connectedFaces;

    private PVector labelpos;
    private PVector grabberPos;

    private boolean showHelper;

    public Edge(PApplet parent_, int id_, Point a_, Point b_, boolean helper_) {
        parent = parent_;
        id = id_;
        a = a_;
        b = b_;
        connectedFaces = new ArrayList<Integer>();

        labelpos = new PVector();
        grabberPos = new PVector();
        showHelper = helper_;

        a.addConnectedEdge(id);
        b.addConnectedEdge(id);

        update();
    }

    public int getId() {
        return id;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public ArrayList<Integer> getConnectedFaces() {
        return connectedFaces;
    }

    public PVector getGrabberPos() {
        return grabberPos;
    }

    public PShape getShape() {
        return shape;
    }

    public void prepareDelete() {
        a.removeConnectedEdge(id);
        b.removeConnectedEdge(id);
    }

    public void addConnectedFace(int id_) {
        connectedFaces.add(id_);
        // println("Connected Faces at edge " + id + ":" + connectedFaces);
    }

    public void removeConnectedFace(int id_) {
        Integer toRemove = new Integer(id_);
        if (connectedFaces.contains(toRemove)) {
            connectedFaces.remove(toRemove);
        }
        // println("Connected Faces at edge " + id + ":" + connectedFaces);
    }

    private int getColor(boolean config, boolean selected,
            boolean mouseOverColor) {
        int c;
        if (!config) {
            c = parent.color(255, 255, 255);
        } else if (mouseOver() && mouseOverColor) {
            if (selected) {
                c = parent.color(0, 255, 255);
            } else {
                c = parent.color(255, 255, 0);
            }
        } else {
            if (selected) {
                c = parent.color(0, 255, 0);
            } else {
                c = parent.color(255, 255, 255);
            }
        }
        return c;
    }

    public void display(boolean config, boolean selected, boolean mouseOverColor) {
        parent.pushMatrix();
        int c = getColor(config, selected, mouseOverColor);

        shape.setStrokeWeight(3);
        shape.setStroke(c);
        shape.setFill(false);
        parent.shapeMode(CORNERS);
        parent.shape(shape);

        if (config && showHelper) {
            grabber.setFill(true);
            grabber.setFill(c);
            grabber.setStroke(c);
            parent.shapeMode(CENTER);
            parent.shape(grabber);
        }
        parent.popMatrix();
    }

    public void displayHelper(boolean selected, boolean mouseOverColor) {
        int c = getColor(true, selected, mouseOverColor);
        parent.pushMatrix();
        parent.fill(c);
        parent.text(id, labelpos.x, labelpos.y);
        arrow.setStroke(c);
        parent.shapeMode(CORNERS);
        parent.shape(arrow);
        parent.popMatrix();
    }

    public void update() {
        PVector normal = new PVector(a.getLocation().x, a.getLocation().y);
        normal.sub(b.getLocation());
        normal.normalize();
        normal.rotate(PApplet.radians(90));
        normal.mult(10);

        grabberPos.x = PApplet.lerp(a.getX(), b.getX(), (float) .5);
        grabberPos.y = PApplet.lerp(a.getY(), b.getY(), (float) .5);
        labelpos.x = grabberPos.x + normal.x;
        labelpos.y = grabberPos.y + normal.y;

        grabber = parent.createShape(RECT, grabberPos.x,
                grabberPos.y, 5, 5);
        grabber.setStrokeWeight(1);

        PVector arrowstart = new PVector(), arrowend1 = new PVector(), arrowend2 = new PVector();
        arrowstart.x = PApplet.lerp(a.getX(), b.getX(), (float) .6);
        arrowstart.y = PApplet.lerp(a.getY(), b.getY(), (float) .6);
        arrowend1.x = PApplet.lerp(a.getX(), b.getX(), (float) .4) + normal.x;
        arrowend1.y = PApplet.lerp(a.getY(), b.getY(), (float) .4) + normal.y;
        arrowend2.x = PApplet.lerp(a.getX(), b.getX(), (float) .4) - normal.x;
        arrowend2.y = PApplet.lerp(a.getY(), b.getY(), (float) .4) - normal.y;

        shape = parent.createShape();
        shape.beginShape(LINES);
        shape.stroke(255);
        shape.strokeCap(ROUND);
        shape.vertex(a.getLocation().x, a.getLocation().y);
        shape.vertex(b.getLocation().x, b.getLocation().y);
        shape.endShape();

        arrow = parent.createShape();
        arrow.beginShape(LINES);
        arrow.stroke(255);
        arrow.strokeCap(ROUND);
        arrow.vertex(arrowstart.x, arrowstart.y);
        arrow.vertex(arrowend1.x, arrowend1.y);
        arrow.vertex(arrowstart.x, arrowstart.y);
        arrow.vertex(arrowend2.x, arrowend2.y);
        arrow.endShape();
    }

    private boolean mouseOver() {
        PVector dist = new PVector();

        dist.x = grabberPos.x - parent.mouseX;
        dist.y = grabberPos.y - parent.mouseY;

        float len2 = dist.magSq();
        if (len2 < 100) {
            return true;
        } else {
            return false;
        }
    }

    public int select() {
        if (mouseOver()) {
            return id;
        } else {
            return -1;
        }
    }
}