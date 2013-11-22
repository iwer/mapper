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
 * FormHandler class
 *
 * Handles ex- and import of points, edges and faces to xml
 *
 */
package de.hawilux.mapper.file;

import processing.core.PApplet;
import processing.data.XML;
import de.hawilux.mapper.FormContainer;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.shapes.Face;
import de.hawilux.mapper.shapes.Point;

public class FileHandler {
    FormContainer data;

    public FileHandler(FormContainer data_) {
        data = data_;
    }

    public void loadXML(String filename) {
        XML xml;

        data.clear();

        xml = data.getParent().loadXML(filename);

        XML[] pointsElement = xml.getChildren("point");
        PApplet.println("nPoints: " + pointsElement.length);
        for (int i = 0; i < pointsElement.length; i++) {
            XML idElement = pointsElement[i].getChild("id");
            int id = idElement.getInt("id");

            XML positionElement = pointsElement[i].getChild("position");
            int x = positionElement.getInt("x");
            int y = positionElement.getInt("y");

            PApplet.println("Point: " + id + " # " + x + "," + y);
            Point p = new Point(data.getParent(), id, x, y, data.isShowHelper());
            data.getPoints().put(id, p);
            data.getParent();
            data.setMaxPointId(PApplet.max(id, data.getMaxPointId()));
        }

        XML[] edgesElement = xml.getChildren("edge");
        PApplet.println("nEdges: " + edgesElement.length);
        for (int i = 0; i < edgesElement.length; i++) {
            XML idElement = edgesElement[i].getChild("id");
            int id = idElement.getInt("id");

            XML pointElement = edgesElement[i].getChild("points");
            // Note how with attributes we can get an integer or float via
            // getInt() and getFloat()
            int a = pointElement.getInt("a");
            int b = pointElement.getInt("b");

            PApplet.println("Edge: " + id + " # " + a + "," + b);
            Edge e = new Edge(data.getParent(), id, data.getPoints().get(a),
                    data.getPoints().get(b), data.isShowHelper());
            data.getEdges().put(id, e);
            data.setMaxEdgeId(PApplet.max(id, data.getMaxEdgeId()));
        }

        XML[] facesElement = xml.getChildren("face");
        for (int i = 0; i < facesElement.length; i++) {
            XML idElement = facesElement[i].getChild("id");
            int id = idElement.getInt("id");

            Face f = new Face(data.getParent(), id, data.isShowHelper());
            XML[] edgeElements = facesElement[i].getChildren("edge");
            for (int j = 0; j < edgeElements.length; j++) {
                int edgeId = edgeElements[j].getInt("id");
                // println("Face "+ id + " addsedge " + edgeId);
                f.addEdge(data.getEdges().get(edgeId));
            }
            data.getFaces().put(id, f);
            data.setMaxFaceId(PApplet.max(id, data.getMaxFaceId()));
        }
    }

    public void saveXML(String filename) {
        XML xml = new XML("data");

        for (Point p : data.getPoints().values()) {
            XML pointElement = xml.addChild("point");
            XML idElement = pointElement.addChild("id");
            idElement.setInt("id", p.id);
            XML positionElement = pointElement.addChild("position");
            positionElement.setInt("x", (int) p.getLocation().x);
            positionElement.setInt("y", (int) p.getLocation().y);
        }
        for (Edge e : data.getEdges().values()) {
            XML edgeElement = xml.addChild("edge");
            XML idElement = edgeElement.addChild("id");
            idElement.setInt("id", e.getId());
            XML pointsElement = edgeElement.addChild("points");
            pointsElement.setInt("a", e.getA().id);
            pointsElement.setInt("b", e.getB().id);
        }
        for (Face f : data.getFaces().values()) {
            XML faceElement = xml.addChild("face");
            XML idElement = faceElement.addChild("id");
            idElement.setInt("id", f.getId());
            // write connected Edge Ids
            for (Edge e : f.getConnectedEdges()) {
                XML edgeElement = faceElement.addChild("edge");
                edgeElement.setInt("id", e.getId());
            }
        }
        data.getParent().saveXML(xml, filename);
    }
}