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

public class Face {
    PApplet parent;
    int id;

    // this should be edges rather than id's
    ArrayList<Edge> connectedEdges;

    // this also could be an ArrayList with contains() check in addEdge,
    // then we can get rid of the cast in sortVertices
    HashSet<Point> points;
    ArrayList<PVector> vertices;

    PShape s;
    PShape grabber;
    PVector centroid;

    boolean helper;

    public Face(PApplet parent_, int id_, boolean helper_) {
        parent = parent_;
        id = id_;
        helper = helper_;
        connectedEdges = new ArrayList<Edge>();
        points = new HashSet<Point>();
        vertices = new ArrayList<PVector>();

        centroid = new PVector();

        update();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Edge> getConnectedEdges() {
        return connectedEdges;
    }

    public PVector getCentroid() {
        return centroid;
    }

    public HashSet<Point> getPoints() {
        return points;
    }

    public void addEdge(Edge e_) {
        e_.addConnectedFace(id);
        connectedEdges.add(e_);
        points.add(e_.a);
        points.add(e_.b);

        update();
    }

    public void prepareDelete() {
        // remove this face from all connected Edges
        for (Edge e : connectedEdges) {
            e.removeConnectedFace(id);
        }
    }

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

    public void display(boolean config, boolean selected, boolean mouseOverColor) {
        parent.pushMatrix();
        int c = getColor(config, selected, mouseOverColor);

        s.setFill(true);
        s.setFill(c);
        s.setStroke(parent.color(255, 255, 255));
        parent.shapeMode(PConstants.CORNERS);
        parent.shape(s);

        if (config) {
            grabber.setFill(true);
            grabber.setFill(parent.color(0, 255, 255));
            grabber.setStroke(c);
            parent.shapeMode(PConstants.CENTER);
            parent.shape(grabber);
            parent.fill(parent.color(0, 255, 255));
            parent.text(id, centroid.x + 10, centroid.y - 10);
        }
        parent.popMatrix();
    }

    public void update() {
        updateCentroid();
        sortVertices();
        s = parent.createShape();
        s.beginShape();
        s.strokeWeight((float) .5);
        s.fill(255);
        for (PVector v : vertices) {
            s.vertex(v.x, v.y);
        }
        s.endShape(PConstants.CLOSE);
        grabber = parent.createShape(PConstants.RECT, centroid.x, centroid.y,
                5, 5);
        grabber.setStrokeWeight(1);
    }

    void updateCentroid() {
        centroid = new PVector();
        for (Point p : points) {
            centroid.add(p.getLocation());
        }
        centroid.div(points.size());
    }

    // Sorting the ArrayList
    // taken from
    // http://shiffman.net/2011/12/23/night-4-sorting-the-vertices-of-a-polygon/
    // there may be a faster sorting method (merge-sort?)
    void sortVertices() {
        // make a local ArrayList from HashSet
        ArrayList<Point> tmpPoints = new ArrayList<Point>(points);

        // This is something like a selection sort
        // Here, instead of sorting within the ArrayList
        // We'll just build a new one sorted
        ArrayList<PVector> newVertices = new ArrayList<PVector>();

        // As long as it's not empty
        while (!tmpPoints.isEmpty()) {
            // Let's find the one with the highest angle
            float biggestAngle = 0;
            Point biggestVertex = null;
            // Look through all of them
            for (Point p : tmpPoints) {
                // Make a vector that points from center
                PVector dir = PVector.sub(p.getLocation(), centroid);
                // What is it's heading
                // The heading function will give us values between -PI and PI
                // easier to sort if we have from 0 to TWO_PI
                float a = dir.heading() + PConstants.PI;
                // Did we find it
                if (a > biggestAngle) {
                    biggestAngle = a;
                    biggestVertex = p;
                }
            }
            // Put the one we found in the new arraylist
            newVertices.add(biggestVertex.getLocation());
            // Delete it so that the next biggest one
            // will be found the next time
            tmpPoints.remove(biggestVertex);
        }
        // We've got a new ArrayList
        vertices = newVertices;
    }

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

    public int select() {
        if (mouseOver()) {
            return id;
        } else {
            return -1;
        }
    }
}
