/*
 *   A 2D Video Mapping Tool created from experiences in the HAWilux project
 *   Copyright (C) 2013  Iwer Petersen
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
 * Face class
 *
 * represents a polygon consisting of a loop of lines 
 * written by Iwer Petersen
 *
 */
package de.hawilux.mapper.shapes;

import java.util.ArrayList;
import java.util.HashSet;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;
import sun.font.CreatedFontTracker;

// TODO: Auto-generated Javadoc
/**
 * The Class Face.
 */
public class Face extends Shape implements PConstants, IFace {

    // this should be edges rather than id's
    /** The connected edges. */
    ArrayList<IEdge>   connectedEdges;

    // this also could be an ArrayList with contains() check in addEdge,
    // then we can get rid of the cast in sortVertices
    /** The points. */
    HashSet<IPoint>    points;

    /** The vertices. */
    ArrayList<PVector> vertices;

    /** The grabber. */
    PShape             grabber;

    /** The helper. */
    boolean            helper;

    /**
     * Instantiates a new face.
     * 
     * @param parent_
     *            the parent_
     * @param id_
     *            the id_
     * @param helper_
     *            the helper_
     */
    public Face(PApplet parent_, PShape shapeGroup_, int id_, boolean helper_) {
        super(parent_, shapeGroup_, id_);
        helper = helper_;
        connectedEdges = new ArrayList<IEdge>();
        points = new HashSet<IPoint>();
        vertices = new ArrayList<PVector>();

        update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IFace#getConnectedEdges()
     */
    @Override
    public ArrayList<IEdge> getConnectedEdges() {
        return connectedEdges;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IFace#getShape()
     */
    @Override
    public PShape getShape() {
        return shape;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IFace#getPoints()
     */
    @Override
    public HashSet<IPoint> getPoints() {
        return points;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IFace#getVertices()
     */
    @Override
    public ArrayList<PVector> getVertices() {
        return vertices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hawilux.mapper.shapes.IFace#addEdge(de.hawilux.mapper.shapes.Edge)
     */
    @Override
    public void addEdge(IEdge e_) {
        e_.addConnectedFace(id);
        connectedEdges.add(e_);
        points.add(e_.getA());
        points.add(e_.getB());

        update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IFace#prepareDelete()
     */
    @Override
    public void prepareDelete() {
        // remove this face from all connected Edges
        for (IEdge e : connectedEdges) {
            e.removeConnectedFace(id);
        }
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
        int c;
        if (!config) {
            c = parent.color(100, 100, 100);
        } else if (mouseOver() && mouseOverColor) {
            if (selected) {
                c = parent.color(200, 200, 200);
            } else {
                c = parent.color(150, 150, 150);
            }
        } else {
            if (selected) {
                c = parent.color(180, 180, 180);
            } else {
                c = parent.color(100, 100, 100);
            }
        }
        return c;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IFace#display(boolean, boolean, boolean)
     */
    @Override
    public void display(boolean config, boolean selected, boolean mouseOverColor) {
        boolean update = false;
        for (IPoint p : points) {
            if (p.isUpdated()) {
                update = true;
            }
        }
        if (update) {
            update();
        }
        // parent.pushMatrix();
        // int c = getColor(config, selected, mouseOverColor);
        //
        // shape.setFill(true);
        // shape.setFill(c);
        // shape.setStroke(parent.color(255, 255, 255));
        // // parent.shapeMode(CORNERS);
        // parent.shape(shape);
        //
        // if (config) {
        // // grabber.setFill(true);
        // grabber.setFill(parent.color(0, 255, 255));
        // grabber.setStroke(c);
        // // parent.shapeMode(CENTER);
        // parent.shape(grabber);
        // parent.fill(parent.color(0, 255, 255));
        // parent.text(id, centroid.x + 10, centroid.y - 10);
        // }
        // parent.popMatrix();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IFace#update()
     */
    @Override
    public void update() {
        updateCentroid();
        sortVertices();

        int c = getColor(true, false, false);
        shapeGroup = parent.createShape(GROUP);

        parent.pushMatrix();

        // parent.shapeMode(CORNERS);
        shape = parent.createShape();
        shape.beginShape();
        for (PVector v : vertices) {
            shape.vertex(v.x, v.y);
        }
        shape.endShape(CLOSE);
        shape.setStrokeWeight((float) .5);
        shape.setFill(true);
        shape.setFill(parent.color(100));
        shape.setStroke(parent.color(255, 255, 255));
        shapeGroup.addChild(shape);

        // parent.shapeMode(CENTER);
        grabber = parent.createShape(RECT, centroid.x, centroid.y, 5, 5);
        grabber.setStrokeWeight(1);
        grabber.setFill(true);
        grabber.setFill(parent.color(0, 255, 255));
        grabber.setStroke(c);
        shapeGroup.addChild(grabber);
        //shapeGroup.setFill(c);
        parent.popMatrix();

    }

    /**
     * Update centroid.
     */
    void updateCentroid() {
        centroid = new PVector();
        for (IPoint p : points) {
            centroid.add(p.getCentroid());
        }
        centroid.div(points.size());
    }

    // Sorting the ArrayList
    // taken from
    // http://shiffman.net/2011/12/23/night-4-sorting-the-vertices-of-a-polygon/
    // there may be a faster sorting method (merge-sort?)
    /**
     * Sort vertices.
     */
    void sortVertices() {
        // make a local ArrayList from HashSet
        ArrayList<IPoint> tmpPoints = new ArrayList<IPoint>(points);

        // This is something like a selection sort
        // Here, instead of sorting within the ArrayList
        // We'll just build a new one sorted
        ArrayList<PVector> newVertices = new ArrayList<PVector>();

        // As long as it's not empty
        while (!tmpPoints.isEmpty()) {
            // Let's find the one with the highest angle
            float biggestAngle = 0;
            IPoint biggestVertex = null;
            // Look through all of them
            for (IPoint p : tmpPoints) {
                // Make a vector that points from center
                PVector dir = PVector.sub(p.getCentroid(), centroid);
                // What is it's heading
                // The heading function will give us values between -PI and PI
                // easier to sort if we have from 0 to TWO_PI
                float a = dir.heading() + PI;
                // Did we find it
                if (a > biggestAngle) {
                    biggestAngle = a;
                    biggestVertex = p;
                }
            }
            // Put the one we found in the new arraylist
            newVertices.add(biggestVertex.getCentroid());
            // Delete it so that the next biggest one
            // will be found the next time
            tmpPoints.remove(biggestVertex);
        }
        // We've got a new ArrayList
        vertices = newVertices;
    }

    /**
     * Mouse over.
     * 
     * @return true, if successful
     */
    boolean mouseOver() {
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
     * @see de.hawilux.mapper.shapes.IFace#select()
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
