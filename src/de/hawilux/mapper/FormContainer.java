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
 * FormContainer class
 *
 * holds Forms (Points, Edges, Faces)
 * provides the the logic to create and modify forms
 * Effects can get Point, Edge and Face information from this class 
 *
 */
package de.hawilux.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PVector;
import controlP5.Button;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.RadioButton;
import controlP5.Toggle;
import de.hawilux.mapper.file.FileHandler;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.shapes.Face;
import de.hawilux.mapper.shapes.Point;
import de.hawilux.mapper.ui.FileChooser;
import de.hawilux.mapper.ui.Gui;

// TODO: Auto-generated Javadoc
/**
 * The Class FormContainer.
 */
public class FormContainer {

    /** The parent. */
    PApplet parent;

    /** The edges. */
    HashMap<Integer, Edge> edges;

    /** The points. */
    HashMap<Integer, Point> points;

    /** The faces. */
    HashMap<Integer, Face> faces;

    /** The file handler. */
    FileHandler fileHandler;

    /** The face to build. */
    Face faceToBuild;

    /** The selected point. */
    int selectedPoint;

    /** The selected edge. */
    int selectedEdge;

    /** The selected face. */
    int selectedFace;

    /** The select mode. */
    int selectMode;

    /** The select points. */
    public final int SELECT_POINTS = 0;

    /** The select edges. */
    public final int SELECT_EDGES = 1;

    /** The select faces. */
    public final int SELECT_FACES = 2;

    /** The max point id. */
    int maxPointId = -1;

    /** The max edge id. */
    int maxEdgeId = -1;

    /** The max face id. */
    int maxFaceId = -1;

    /** The show helper. */
    boolean showHelper = true;

    // gui elements
    /** The btn new config. */
    Button btnNewConfig;

    /** The btn load config. */
    Button btnLoadConfig;

    /** The btn save config. */
    Button btnSaveConfig;

    /** The tgl helper. */
    Toggle tglHelper;

    /** The rdb select mode. */
    RadioButton rdbSelectMode;

    /** The btn delete selected. */
    Button btnDeleteSelected;

    /** The btn switch dir. */
    Button btnSwitchDir;

    /** The btn subdivide. */
    Button btnSubdivide;

    /** The file chooser. */
    FileChooser fileChooser;

    /**
     * Instantiates a new form container.
     * 
     * @param parent_
     *            the parent_
     */
    public FormContainer(PApplet parent_) {
        parent = parent_;
        edges = new HashMap<Integer, Edge>();
        points = new HashMap<Integer, Point>();
        faces = new HashMap<Integer, Face>();

        fileHandler = new FileHandler(this);

        faceToBuild = null;

        selectedPoint = -1;
        selectedEdge = -1;
        selectedFace = -1;

        selectMode = SELECT_POINTS;
    }

    /**
     * Adds the edge to face.
     */
    public void addEdgeToFace() {
        if (selectMode == SELECT_EDGES) {
            if (faceToBuild == null) {
                maxFaceId++;
                faceToBuild = new Face(parent, maxFaceId, showHelper);
            }
            select();
            if (selectedEdge != -1) {
                Edge toAdd = edges.get(selectedEdge);
                int nBefore = faceToBuild.getPoints().size();
                faceToBuild.addEdge(toAdd);
                int nAfter = faceToBuild.getPoints().size();
                // this needs a better check (line-loop?)
                if (nBefore == nAfter) {
                    faces.put(faceToBuild.getId(), faceToBuild);
                    faceToBuild = null;
                }
            }
        }
    }

    // ###### GUI ###########################################################
    /**
     * Adds the file gui.
     * 
     * @param gui
     *            the gui
     */
    public void addFileGui(Gui gui) {
        btnNewConfig = gui.getCp5().addButton("newConfig").setPosition(10, 10)
                .setColor(gui.getC()).moveTo(gui.getFileGroup());
        btnNewConfig.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    clear();
                }
            }
        });
        btnLoadConfig = gui.getCp5().addButton("loadConfig")
                .setPosition(10, 35).setColor(gui.getC())
                .moveTo(gui.getFileGroup());
        // callback to open filechooser
        btnLoadConfig.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    PApplet.println("LOAD");
                    fileChooser.addFileChooser("load");
                    // add action callback
                    fileChooser.getActionButton().addCallback(
                            new CallbackListener() {
                                public void controlEvent(
                                        CallbackEvent theActionEvent) {
                                    if (theActionEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                                        fileHandler.loadXML(fileChooser
                                                .getFilename());
                                    }
                                }
                            });
                }
            }
        });
        btnSaveConfig = gui.getCp5().addButton("saveConfig")
                .setPosition(10, 60).setColor(gui.getC())
                .moveTo(gui.getFileGroup());
        btnSaveConfig.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    PApplet.println("SAVE");
                    fileChooser.addFileChooser("save");
                    // add action callback
                    fileChooser.getActionButton().addCallback(
                            new CallbackListener() {
                                public void controlEvent(
                                        CallbackEvent theActionEvent) {
                                    if (theActionEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                                        fileHandler.saveXML(fileChooser
                                                .getFilename());
                                    }
                                }
                            });
                }
            }
        });
        addSetupGui(gui);
    }

    /**
     * Adds the point.
     */
    public void addPoint() {
        Point a = null;
        Point p = null;
        // add points only in point select mode
        if (selectMode == SELECT_POINTS) {
            // when a point is selected take as first point
            if (selectedPoint != -1) {
                a = points.get(selectedPoint);
            }
            deselectPoints();

            // check if mouse is over existing point
            int sel = -1;
            for (Point exP : points.values()) {
                int ret = exP.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            // not over existing point, create new one
            if (sel == -1) {
                maxPointId++;
                p = new Point(parent, maxPointId, parent.mouseX, parent.mouseY,
                        showHelper);
                selectedPoint = p.select();
                points.put(p.getId(), p);
            }
            // select as second point when hovering over it
            else {
                p = points.get(sel);
            }

            // if there was a point selected, draw a line to new point
            if (a != null) {
                maxEdgeId++;
                Edge e = new Edge(parent, maxEdgeId, a, p, showHelper);
                edges.put(maxEdgeId, e);
                // face.addEdge(e);
            }
        }
    }

    /**
     * Adds the setup gui.
     * 
     * @param gui
     *            the gui
     */
    public void addSetupGui(Gui gui) {
        tglHelper = gui.getCp5().addToggle("showHelper").setPosition(10, 10)
                .setColor(gui.getC()).setMode(ControlP5.SWITCH).setValue(true)
                .moveTo(gui.getSetupGroup());
        tglHelper.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    if (theEvent.getController().getValue() == 1.0) {
                        setHelper(true);
                    } else {
                        setHelper(false);
                    }
                }
            }
        });
        rdbSelectMode = gui.getCp5().addRadioButton("selectmode")
                .setPosition(10, 2).setColor(gui.getC())
                .setNoneSelectedAllowed(false).addItem("points", 1)
                .addItem("edges", 2).addItem("faces", 3)
                .moveTo(gui.getSetupSelectModeGroup()).activate("points");
        btnDeleteSelected = gui.getCp5().addButton("deleteSelected")
                .setPosition(10, 2).setColor(gui.getC())
                .moveTo(gui.getSetupEditGroup());
        btnDeleteSelected.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    deleteSelected();
                }
            }
        });
        btnSubdivide = gui.getCp5().addButton("subdivide").setPosition(10, 27)
                .setColor(gui.getC()).hide().moveTo(gui.getSetupEditGroup());
        btnSubdivide.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    subdivideEdge();
                }
            }
        });
        btnSwitchDir = gui.getCp5().addButton("switchdir").setPosition(10, 52)
                .setColor(gui.getC()).hide().moveTo(gui.getSetupEditGroup());
        btnSwitchDir.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    switchEdgeDirection();
                }
            }
        });
    }

    /**
     * Clear.
     */
    public void clear() {
        points.clear();
        edges.clear();
        faces.clear();
        maxPointId = -1;
        maxEdgeId = -1;
        maxFaceId = -1;
    }

    /**
     * Delete selected.
     */
    public void deleteSelected() {
        if (selectMode == SELECT_POINTS) {
            PApplet.println("Delete Point " + selectedPoint);
            if (selectedPoint != -1) {
                Point toRemove = points.get(selectedPoint);
                ArrayList<Integer> conEdges = new ArrayList<Integer>(
                        toRemove.getConnectedEdges());
                PApplet.println("Connected Edges at point " + selectedPoint
                        + " : " + conEdges);
                for (Integer i : conEdges) {
                    PApplet.println("CoDelete Edge " + i);
                    ArrayList<Integer> conFaces = new ArrayList<Integer>(edges
                            .get(i).getConnectedFaces());
                    PApplet.println("Connected Faces at edge " + i + " : "
                            + conFaces);
                    for (Integer j : conFaces) {
                        PApplet.println("CoCoDelete Face " + j);
                        faces.get(j).prepareDelete();
                        faces.remove(j);
                    }
                    edges.get(i).prepareDelete();
                    edges.remove(i);
                }
                points.remove(selectedPoint);
                selectedPoint = -1;
            }
        } else if (selectMode == SELECT_EDGES) {
            PApplet.println("Delete Edge " + selectedEdge);
            if (selectedEdge != -1) {
                Edge toRemove = edges.get(selectedEdge);
                toRemove.prepareDelete();
                ArrayList<Integer> conFaces = new ArrayList<Integer>(
                        toRemove.getConnectedFaces());
                for (Integer i : conFaces) {
                    PApplet.println("CoDelete Face " + i);
                    faces.get(i).prepareDelete();
                    faces.remove(i);
                }
                edges.remove(selectedEdge);
                selectedEdge = -1;
            }
            PApplet.println("Edges:" + edges.size());
        } else if (selectMode == SELECT_FACES) {
            PApplet.println("Delete Face " + selectedFace);
            if (selectedFace != -1) {
                Face toRemove = faces.get(selectedFace);
                toRemove.prepareDelete();
                faces.remove(selectedFace);
                selectedEdge = -1;
            }
            PApplet.println("Face:" + faces.size());
        }
    }

    /**
     * Deselect points.
     */
    public void deselectPoints() {
        selectedPoint = -1;
    }

    /**
     * Display.
     * 
     * @param config
     *            the config
     */
    public void display(boolean config) {
        boolean selected;
        if (faceToBuild != null) {
            faceToBuild.display(config, true, false);
        }
        for (Face f : faces.values()) {
            if (selectedFace == f.getId()) {
                selected = true;
            } else {
                selected = false;
            }
            f.display(config, selected, (selectMode == SELECT_FACES));
        }
        for (Edge e : edges.values()) {
            if (selectedEdge == e.getId()) {
                selected = true;
            } else {
                selected = false;
            }
            e.display(config, selected, (selectMode == SELECT_EDGES));
            if (config && showHelper) {
                e.displayHelper(selected, (selectMode == SELECT_EDGES));
            }
        }
        if (config) {
            for (Point p : points.values()) {
                if (selectedPoint == p.getId()) {
                    selected = true;
                } else {
                    selected = false;
                }
                p.display(config, selected, (selectMode == SELECT_POINTS));
                if (showHelper) {
                    p.displayHelper(selected, (selectMode == SELECT_POINTS));
                }
            }
        }
    }

    /**
     * Gets the edges.
     * 
     * @return the edges
     */
    public HashMap<Integer, Edge> getEdges() {
        return edges;
    }

    /**
     * Gets the faces.
     * 
     * @return the faces
     */
    public HashMap<Integer, Face> getFaces() {
        return faces;
    }

    /**
     * Gets the select mode.
     * 
     * @return the select mode
     */
    public int getSelectMode() {
        return selectMode;
    }

    /**
     * Gets the max edge id.
     * 
     * @return the max edge id
     */
    public int getMaxEdgeId() {
        return maxEdgeId;
    }

    /**
     * Gets the max face id.
     * 
     * @return the max face id
     */
    public int getMaxFaceId() {
        return maxFaceId;
    }

    /**
     * Gets the max point id.
     * 
     * @return the max point id
     */
    public int getMaxPointId() {
        return maxPointId;
    }

    /**
     * Gets the parent.
     * 
     * @return the parent
     */
    public PApplet getParent() {
        return parent;
    }

    /**
     * Gets the points.
     * 
     * @return the points
     */
    public HashMap<Integer, Point> getPoints() {
        return points;
    }

    /**
     * Gets the rdb select mode.
     * 
     * @return the rdb select mode
     */
    public RadioButton getRdbSelectMode() {
        return rdbSelectMode;
    }

    /**
     * Checks if is show helper.
     * 
     * @return true, if is show helper
     */
    public boolean isShowHelper() {
        return showHelper;
    }

    /**
     * Move point.
     */
    public void movePoint() {
        if (selectedPoint != -1) {
            Point p = points.get(selectedPoint);
            if (p != null) {
                p.move();
                ArrayList<Integer> conEdges = p.getConnectedEdges();
                for (Integer i : conEdges) {
                    edges.get(i).update();
                    ArrayList<Integer> conFaces = edges.get(i)
                            .getConnectedFaces();
                    for (Integer j : conFaces) {
                        faces.get(j).update();
                    }
                }
            }
        }
    }

    /**
     * Move point.
     * 
     * @param dx
     *            the dx
     * @param dy
     *            the dy
     */
    public void movePoint(int dx, int dy) {
        if (selectedPoint != -1) {
            Point p = points.get(selectedPoint);
            if (p != null) {
                p.move(dx, dy);
                ArrayList<Integer> conEdges = p.getConnectedEdges();
                for (Integer i : conEdges) {
                    edges.get(i).update();
                    ArrayList<Integer> conFaces = edges.get(i)
                            .getConnectedFaces();
                    for (Integer j : conFaces) {
                        faces.get(j).update();
                    }
                }
            }
        }
    }

    /**
     * Select.
     */
    public void select() {
        int sel = -1;
        if (selectMode == SELECT_POINTS) {
            for (Point p : points.values()) {
                int ret = p.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedPoint = sel;
            PApplet.println("Selected Point " + selectedPoint);
        } else if (selectMode == SELECT_EDGES) {
            for (Edge e : edges.values()) {
                int ret = e.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedEdge = sel;
            PApplet.println("Selected Edge " + selectedEdge);
        } else if (selectMode == SELECT_FACES) {
            for (Face f : faces.values()) {
                int ret = f.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedFace = sel;
            PApplet.println("Selected Face " + selectedFace);
        }
    }

    /**
     * Sets the file chooser.
     * 
     * @param fileChooser_
     *            the new file chooser
     */
    public void setFileChooser(FileChooser fileChooser_) {
        fileChooser = fileChooser_;
    }

    /**
     * Sets the helper.
     * 
     * @param theValue
     *            the new helper
     */
    public void setHelper(boolean theValue) {
        showHelper = theValue;
    }

    /**
     * Sets the line editor enabled.
     * 
     * @param state
     *            the new line editor enabled
     */
    public void setLineEditorEnabled(boolean state) {
        if (state == true) {
            btnSubdivide.show();
            btnSwitchDir.show();
        } else {
            btnSubdivide.hide();
            btnSwitchDir.hide();
        }
    }

    /**
     * Sets the max edge id.
     * 
     * @param maxEdgeId
     *            the new max edge id
     */
    public void setMaxEdgeId(int maxEdgeId) {
        this.maxEdgeId = maxEdgeId;
    }

    /**
     * Sets the max face id.
     * 
     * @param maxFaceId
     *            the new max face id
     */
    public void setMaxFaceId(int maxFaceId) {
        this.maxFaceId = maxFaceId;
    }

    /**
     * Sets the max point id.
     * 
     * @param maxPointId
     *            the new max point id
     */
    public void setMaxPointId(int maxPointId) {
        this.maxPointId = maxPointId;
    }

    /**
     * Sets the select mode.
     * 
     * @param mode_
     *            the new select mode
     */
    public void setSelectMode(int mode_) {
        if (mode_ == SELECT_POINTS || mode_ == SELECT_EDGES
                || mode_ == SELECT_FACES) {
            selectMode = mode_;
            selectedPoint = -1;
            selectedEdge = -1;
            selectedFace = -1;
            switch (mode_) {
            case SELECT_POINTS:
                PApplet.println("Mode: SELECT_POINTS");
                break;
            case SELECT_EDGES:
                PApplet.println("Mode: SELECT_EDGES");
                break;
            case SELECT_FACES:
                PApplet.println("Mode: SELECT_FACES");
                break;
            }
        }
    }

    /**
     * Sets the show helper.
     * 
     * @param showHelper
     *            the new show helper
     */
    public void setShowHelper(boolean showHelper) {
        this.showHelper = showHelper;
    }

    /**
     * Subdivide edge.
     */
    public void subdivideEdge() {
        if (selectedEdge != -1) {
            Edge toDivide = edges.get(selectedEdge);
            Point start = toDivide.getA();
            Point end = toDivide.getB();
            PVector newPointPos = toDivide.getCentroid();
            ArrayList<Integer> connectedFaces = toDivide.getConnectedFaces();

            // create new Point
            maxPointId++;
            Point newPoint = new Point(parent, maxPointId, (int) newPointPos.x,
                    (int) newPointPos.y, showHelper);
            selectedPoint = newPoint.select();
            points.put(newPoint.getId(), newPoint);

            // add new edges
            maxEdgeId++;
            Edge e1 = new Edge(parent, maxEdgeId, start, newPoint, showHelper);

            maxEdgeId++;
            Edge e2 = new Edge(parent, maxEdgeId, newPoint, end, showHelper);

            for (Integer i : connectedFaces) {
                faces.get(i).addEdge(e1);
                faces.get(i).addEdge(e2);
                e1.addConnectedFace(i);
                e2.addConnectedFace(i);
            }

            // remove edge
            toDivide.prepareDelete();
            edges.remove(toDivide.getId());

            edges.put(e1.getId(), e1);
            edges.put(e2.getId(), e2);
        }
    }

    /**
     * Switch edge direction.
     */
    public void switchEdgeDirection() {
        if (selectedEdge != -1) {
            Edge toSwitch = edges.get(selectedEdge);
            Point start = toSwitch.getA();
            Point end = toSwitch.getB();

            // remove edge
            toSwitch.prepareDelete();
            edges.remove(toSwitch.getId());

            // add new edges first edge reuses id of deleted
            maxEdgeId++;
            Edge e = new Edge(parent, maxEdgeId, end, start, showHelper);
            for (Integer i : toSwitch.getConnectedFaces()) {
                e.addConnectedFace(i);
            }
            edges.put(maxEdgeId, e);
        }
    }
}
