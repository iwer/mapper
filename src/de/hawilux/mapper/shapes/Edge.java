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

// TODO: Auto-generated Javadoc
/**
 * The Class Edge.
 */
public class Edge extends Shape implements PConstants, IEdge {

    /** The arrow. */
    private PShape             arrow;

    /** The grabber. */
    private PShape             grabber;

    /** The b. */
    IPoint                     a, b;

    /** The connected faces. */
    private ArrayList<Integer> connectedFaces;

    /** The labelpos. */
    private PVector            labelpos;

    /** The show helper. */
    private boolean            showHelper;

    /**
     * Instantiates a new edge.
     * 
     * @param parent_
     *            the parent_
     * @param id_
     *            the id_
     * @param a_
     *            the a_
     * @param b_
     *            the b_
     * @param helper_
     *            the helper_
     */
    public Edge(PApplet parent_, PShape shapeGroup_, int id_, IPoint a_,
            IPoint b_, boolean helper_) {
        super(parent_, shapeGroup_, id_);
        a = a_;
        b = b_;
        connectedFaces = new ArrayList<Integer>();

        labelpos = new PVector();
        centroid = new PVector();
        showHelper = helper_;

        a.addConnectedEdge(id);
        b.addConnectedEdge(id);

        update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#getA()
     */
    @Override
    public IPoint getA() {
        return a;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#getB()
     */
    @Override
    public IPoint getB() {
        return b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#getConnectedFaces()
     */
    @Override
    public ArrayList<Integer> getConnectedFaces() {
        return connectedFaces;
    }

    /**
     * Gets the grabber pos.
     * 
     * @return the grabber pos
     * @deprecated use getCentroid()
     */
    @Deprecated
    public PVector getGrabberPos() {
        return centroid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#prepareDelete()
     */
    @Override
    public void prepareDelete() {
        a.removeConnectedEdge(id);
        b.removeConnectedEdge(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#addConnectedFace(int)
     */
    @Override
    public void addConnectedFace(int id_) {
        connectedFaces.add(id_);
        // println("Connected Faces at edge " + id + ":" + connectedFaces);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#removeConnectedFace(int)
     */
    @Override
    public void removeConnectedFace(int id_) {
        Integer toRemove = new Integer(id_);
        if (connectedFaces.contains(toRemove)) {
            connectedFaces.remove(toRemove);
        }
        // println("Connected Faces at edge " + id + ":" + connectedFaces);
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

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#display(boolean, boolean, boolean)
     */
    @Override
    public void display(boolean config, boolean selected, boolean mouseOverColor) {
        if (a.isUpdated() || b.isUpdated()) {
            update();
        }
        // parent.pushMatrix();
        // int c = getColor(config, selected, mouseOverColor);
        //
        // shape.setStrokeWeight(3);
        // shape.setStroke(c);
        // shape.setFill(false);
        // parent.shapeMode(CORNERS);
        // parent.shape(shape);
        //
        // if (config && showHelper) {
        // grabber.setFill(true);
        // grabber.setFill(c);
        // grabber.setStroke(c);
        // parent.shapeMode(CENTER);
        // parent.shape(grabber);
        // }
        // parent.popMatrix();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#displayHelper(boolean, boolean)
     */
    @Override
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

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#update()
     */
    @Override
    public void update() {
        shapeGroup = parent.createShape(GROUP);
        PVector normal = new PVector(a.getCentroid().x, a.getCentroid().y);

        normal.sub(b.getCentroid());
        normal.normalize();
        normal.rotate(PApplet.radians(90));
        normal.mult(10);

        centroid.x = PApplet.lerp(a.getX(), b.getX(), .5f);
        centroid.y = PApplet.lerp(a.getY(), b.getY(), .5f);
        labelpos.x = centroid.x + normal.x;
        labelpos.y = centroid.y + normal.y;

        parent.pushMatrix();
        int c = getColor(true, false, mouseOver());

//        parent.shapeMode(CENTER);
        grabber = parent.createShape(RECT, centroid.x, centroid.y, 5, 5);
        grabber.setStrokeWeight(1);
        // shapeGroup.addChild(grabber);

        PVector arrowstart = new PVector(), arrowend1 = new PVector(), arrowend2 = new PVector();
        arrowstart.x = PApplet.lerp(a.getX(), b.getX(), .6f);
        arrowstart.y = PApplet.lerp(a.getY(), b.getY(), .6f);
        arrowend1.x = PApplet.lerp(a.getX(), b.getX(), .4f) + normal.x;
        arrowend1.y = PApplet.lerp(a.getY(), b.getY(), .4f) + normal.y;
        arrowend2.x = PApplet.lerp(a.getX(), b.getX(), .4f) - normal.x;
        arrowend2.y = PApplet.lerp(a.getY(), b.getY(), .4f) - normal.y;

//        parent.shapeMode(CORNERS);
        shape = parent.createShape();
        shape.beginShape(LINES);
        shape.stroke(255);
        shape.strokeCap(ROUND);
        shape.vertex(a.getCentroid().x, a.getCentroid().y);
        shape.vertex(b.getCentroid().x, b.getCentroid().y);
        shape.endShape();
        shape.setStrokeWeight(3);
        shape.setStroke(c);
        shape.setFill(false);
        shapeGroup.addChild(shape);

//        parent.shapeMode(CORNERS);
        arrow = parent.createShape();
        arrow.beginShape(LINES);
        arrow.stroke(255);
        arrow.strokeCap(ROUND);
        arrow.vertex(arrowstart.x, arrowstart.y);
        arrow.vertex(arrowend1.x, arrowend1.y);
        arrow.vertex(arrowstart.x, arrowstart.y);
        arrow.vertex(arrowend2.x, arrowend2.y);
        arrow.endShape();
        shapeGroup.addChild(arrow);

        parent.popMatrix();
    }

    /**
     * Mouse over.
     * 
     * @return true, if successful
     */
    protected boolean mouseOver() {
        PVector dist = new PVector();

        dist.x = centroid.x - parent.mouseX;
        dist.y = centroid.y - parent.mouseY;

        float len2 = dist.magSq();
        if (len2 < 100) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IEdge#select()
     */
    @Override
    public int select() {
        if (mouseOver()) {
            return id;
        } else {
            return -1;
        }
    }
}