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
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import controlP5.Button;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.RadioButton;
import controlP5.Toggle;
import de.hawilux.mapper.file.FileHandler;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.shapes.Face;
import de.hawilux.mapper.shapes.IEdge;
import de.hawilux.mapper.shapes.IFace;
import de.hawilux.mapper.shapes.IPoint;
import de.hawilux.mapper.shapes.Point;
import de.hawilux.mapper.ui.FileChooser;
import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.ui.MapperControlFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class FormContainer.
 */
public class FormContainer {

    /** The parent. */
    PApplet                  parent;

    /** The edges. */
    HashMap<Integer, IEdge>  edges;

    /** The points. */
    HashMap<Integer, IPoint> points;

    /** The faces. */
    HashMap<Integer, IFace>  faces;

    /** The file handler. */
    FileHandler              fileHandler;

    /** The face to build. */
    IFace                    faceToBuild;

    /** The group to store individual point shapes. */
    PShape                   pointShapeGroup;
    /** The group to store individual edge shapes. */
    PShape                   edgeShapeGroup;
    /** The group to store individual face shapes. */
    PShape                   faceShapeGroup;

    public PGraphics         renderLayer;
    /** The selected point. */
    int                      selectedPoint;

    /** The selected edge. */
    int                      selectedEdge;

    /** The selected face. */
    int                      selectedFace;

    /** The select mode. */
    int                      selectMode;

    /** The select points. */
    public final int         SELECT_POINTS = 0;

    /** The select edges. */
    public final int         SELECT_EDGES  = 1;

    /** The select faces. */
    public final int         SELECT_FACES  = 2;

    /** The max point id. */
    int                      maxPointId    = -1;

    /** The max edge id. */
    int                      maxEdgeId     = -1;

    /** The max face id. */
    int                      maxFaceId     = -1;

    /** The show helper. */
    boolean                  showHelper    = true;

    // gui elements
    /** The btn new config. */
    Button                   btnNewConfig;

    /** The btn load config. */
    Button                   btnLoadConfig;

    /** The btn save config. */
    Button                   btnSaveConfig;

    /** The tgl helper. */
    Toggle                   tglHelper;

    /** The rdb select mode. */
    RadioButton              rdbSelectMode;

    /** The btn delete selected. */
    Button                   btnDeleteSelected;

    /** The btn switch dir. */
    Button                   btnSwitchDir;

    /** The btn subdivide. */
    Button                   btnSubdivide;

    /** The file chooser. */
    FileChooser              fileChooser;

    /**
     * Instantiates a new form container.
     * 
     * @param parent_
     *            the parent_
     */
    public FormContainer(PApplet parent_, PGraphics renderLayer) {
        parent = parent_;
        edges = new HashMap<Integer, IEdge>();
        points = new HashMap<Integer, IPoint>();
        faces = new HashMap<Integer, IFace>();

        this.renderLayer = renderLayer;
        pointShapeGroup = renderLayer.createShape(PConstants.GROUP);
        edgeShapeGroup = renderLayer.createShape(PConstants.GROUP);
        faceShapeGroup = renderLayer.createShape(PConstants.GROUP);

        fileHandler = new FileHandler(this);

        faceToBuild = null;

        selectedPoint = -1;
        selectedEdge = -1;
        selectedFace = -1;

        selectMode = SELECT_POINTS;
    }

    /**
     * Adds the edge to new face. Considers Face complete when point count does
     * not increase with an added line. This needs to be improved!
     */
    public void addEdgeToFace() {
        if (selectMode == SELECT_EDGES) {
            if (faceToBuild == null) {
                maxFaceId++;
                faceToBuild = new Face(renderLayer, parent, faceShapeGroup,
                        maxFaceId, showHelper);
                faceToBuild.getShape().setFill(renderLayer.color(180,0,0));
                faceShapeGroup.addChild(faceToBuild.getShape());
            }
            select();
            if (selectedEdge != -1) {
                IEdge toAdd = edges.get(selectedEdge);
                int nBefore = faceToBuild.getPoints().size();
                faceShapeGroup.removeChild(faceShapeGroup
                        .getChildIndex(faceShapeGroup.findChild("Face_"
                                + faceToBuild.getId())));
                faceToBuild.addEdge(toAdd);
                faceShapeGroup.addChild(faceToBuild.getShape());

                int nAfter = faceToBuild.getPoints().size();
                // TODO: this needs a better check (line-loop?)
                if (nBefore == nAfter) {
                    int id = faceToBuild.getId();
                    // faceToBuild.update();
                    faces.put(id, faceToBuild);
                    faceShapeGroup.removeChild(faceShapeGroup
                            .getChildIndex(faceShapeGroup.findChild("Face_"
                                    + faceToBuild.getId())));
                    faceToBuild = null;
                    faces.get(id).getShape().setFill(renderLayer.color(100));
                    faceShapeGroup.addChild(faces.get(id).getShape());
                }
            }
        }
    }

    // ###### GUI ###########################################################
    /**
     * Adds the gui elements.
     * 
     * @param gui
     *            the gui
     */
    public void addGui(Gui gui) {
        btnNewConfig = gui.getCp5().addButton("newConfig").setPosition(10, 10)
                .setColor(gui.getC()).moveTo(gui.getFileGroup());
        btnNewConfig.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
                    clear();
                }
            }
        });
        btnLoadConfig = gui.getCp5().addButton("loadConfig")
                .setPosition(10, 35).setColor(gui.getC())
                .moveTo(gui.getFileGroup());
        // callback to open filechooser
        btnLoadConfig.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
                    PApplet.println("LOAD");
                    fileChooser.addFileChooser("load");
                    // add action callback
                    fileChooser.getActionButton().addCallback(
                            new CallbackListener() {
                                @Override
                                public void controlEvent(
                                        CallbackEvent theActionEvent) {
                                    if (theActionEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
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
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
                    PApplet.println("SAVE");
                    fileChooser.addFileChooser("save");
                    // add action callback
                    fileChooser.getActionButton().addCallback(
                            new CallbackListener() {
                                @Override
                                public void controlEvent(
                                        CallbackEvent theActionEvent) {
                                    if (theActionEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
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
     * Adds a point at mouse position.
     */
    public void addPoint() {
        IPoint a = null;
        IPoint p = null;
        // add points only in point select mode
        if (selectMode == SELECT_POINTS) {
            // when a point is selected take as first point
            if (selectedPoint != -1) {
                a = points.get(selectedPoint);
            }
            deselectPoints();

            // check if mouse is over existing point
            int sel = -1;
            for (IPoint exP : points.values()) {
                int ret = exP.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            // not over existing point, create new one
            if (sel == -1) {
                maxPointId++;
                p = new Point(renderLayer, parent, pointShapeGroup, maxPointId,
                        MapperControlFrame.projectorMouseX,
                        MapperControlFrame.projectorMouseY, showHelper);
                pointShapeGroup.addChild(p.getShape());
                selectedPoint = p.select();
                points.put(p.getId(), p);
            }
            // select as second point when hovering over it
            else {
                p = points.get(sel);
            }

            // if there was a point selected and the second point is not the
            // same as the first one (causes trouble on deletion), draw a line
            // to new point
            if (a != null && !p.equals(a)) {
                maxEdgeId++;
                Edge e = new Edge(renderLayer, parent, edgeShapeGroup,
                        maxEdgeId, a, p, showHelper);
                edges.put(maxEdgeId, e);
                edgeShapeGroup.addChild(e.getShape());
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
                .setColor(gui.getC()).setMode(ControlP5Constants.SWITCH).setValue(true)
                .moveTo(gui.getSetupGroup());
        tglHelper.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
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
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
                    deleteSelected();
                }
            }
        });
        btnSubdivide = gui.getCp5().addButton("subdivide").setPosition(10, 27)
                .setColor(gui.getC()).hide().moveTo(gui.getSetupEditGroup());
        btnSubdivide.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
                    subdivideEdge();
                }
            }
        });
        btnSwitchDir = gui.getCp5().addButton("switchdir").setPosition(10, 52)
                .setColor(gui.getC()).hide().moveTo(gui.getSetupEditGroup());
        btnSwitchDir.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
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
        pointShapeGroup = renderLayer.createShape(PConstants.GROUP);
        edgeShapeGroup = renderLayer.createShape(PConstants.GROUP);
        faceShapeGroup = renderLayer.createShape(PConstants.GROUP);
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
                IPoint toRemove = points.get(selectedPoint);
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
                        if (faces.get(j) != null) { // TODO: This is a
                                                    // workaround due to
                                                    // NullPointer bug issue#8
                            faces.get(j).prepareDelete();
                            faceShapeGroup.removeChild(faceShapeGroup
                                    .getChildIndex(faceShapeGroup
                                            .findChild("Face_" + j)));
                            faces.remove(j);
                        }
                    }
                    edges.get(i).prepareDelete();
                    edgeShapeGroup.removeChild(edgeShapeGroup
                            .getChildIndex(edges.get(i).getShape()));
                    edges.remove(i);
                }
                pointShapeGroup.removeChild(pointShapeGroup
                        .getChildIndex(points.get(selectedPoint).getShape()));
                points.remove(selectedPoint);
                selectedPoint = -1;
            }
        } else if (selectMode == SELECT_EDGES) {
            PApplet.println("Delete Edge " + selectedEdge);
            if (selectedEdge != -1) {
                IEdge toRemove = edges.get(selectedEdge);
                toRemove.prepareDelete();
                ArrayList<Integer> conFaces = new ArrayList<Integer>(
                        toRemove.getConnectedFaces());
                for (Integer i : conFaces) {
                    PApplet.println("CoDelete Face " + i);
                    // TODO: This is a workaround due to NullPointer bug issue#8
                    if (faces.get(i) != null) {
                        faces.get(i).prepareDelete();
                        faceShapeGroup.removeChild(faceShapeGroup
                                .getChildIndex(faceShapeGroup.findChild("Face_"
                                        + i)));
                        faces.remove(i);
                    } else {
                        PApplet.println("ERROR: issue#8 ocurred, workaround applied, bug is not fixed yet!");
                    }
                }
                edgeShapeGroup.removeChild(edgeShapeGroup.getChildIndex(edges
                        .get(selectedEdge).getShape()));
                edges.remove(selectedEdge);
                selectedEdge = -1;
            }
            PApplet.println("Edges:" + edges.size());
        } else if (selectMode == SELECT_FACES) {
            PApplet.println("Delete Face " + selectedFace);
            if (selectedFace != -1) {
                IFace toRemove = faces.get(selectedFace);
                toRemove.prepareDelete();
                faceShapeGroup.removeChild(faceShapeGroup
                        .getChildIndex(faceShapeGroup.findChild("Face_"
                                + toRemove.getId())));
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
    public void display(PGraphics layer, boolean config) {
        if (faceToBuild != null) {
            faceToBuild.display(config, false, false);
            faceToBuild.getShape().setFill(layer.color(180,0,0));
        }

        for (IPoint p : points.values()) {
            p.display(config, (p.getId() == selectedPoint),
                    selectMode == SELECT_POINTS);
        }

        for (IEdge e : edges.values()) {
            e.display(config, (e.getId() == selectedEdge),
                    selectMode == SELECT_EDGES);
        }

        for (IFace f : faces.values()) {
            f.display(config, (f.getId() == selectedFace),
                    selectMode == SELECT_FACES);
        }

        layer.shape(faceShapeGroup);

        layer.shape(edgeShapeGroup);

        if (config) {
            layer.shape(pointShapeGroup);
        }
    }

    /**
     * Gets the edges.
     * 
     * @return the edges
     */
    public HashMap<Integer, IEdge> getEdges() {
        return edges;
    }

    /**
     * Gets the faces.
     * 
     * @return the faces
     */
    public HashMap<Integer, IFace> getFaces() {
        return faces;
    }

    public PShape getPointShapeGroup() {
        return pointShapeGroup;
    }

    public PShape getEdgeShapeGroup() {
        return edgeShapeGroup;
    }

    public PShape getFaceShapeGroup() {
        return faceShapeGroup;
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
    public HashMap<Integer, IPoint> getPoints() {
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
            IPoint p = points.get(selectedPoint);
            if (p != null) {
                pointShapeGroup.removeChild(pointShapeGroup.getChildIndex(p
                        .getShape()));
                p.move();
                p.update();
                pointShapeGroup.addChild(p.getShape());
                updateShapesAfterPointMoved(p);
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
            IPoint p = points.get(selectedPoint);
            if (p != null) {
                pointShapeGroup.removeChild(pointShapeGroup.getChildIndex(p
                        .getShape()));
                p.move(dx, dy);
                p.update();
                pointShapeGroup.addChild(p.getShape());
                updateShapesAfterPointMoved(p);
            }
        }
    }

    /**
     * After a point moved, edges and faces have to be updated. Therefore this
     * method traverses through the connected edges and faces and updates them.
     * 
     * @param p
     *            the point that moved
     */
    private void updateShapesAfterPointMoved(IPoint p) {
        ArrayList<Integer> conEdges = p.getConnectedEdges();
        // HashSet<Integer> facesToUpdate = new HashSet<Integer>();

        for (Integer i : conEdges) {
            int eIndex = edgeShapeGroup.getChildIndex(edges.get(i).getShape());
            if (eIndex != -1) {
                edgeShapeGroup.removeChild(eIndex);
            }
            edges.get(i).update();
            edgeShapeGroup.addChild(edges.get(i).getShape());
            ArrayList<Integer> conFaces = edges.get(i).getConnectedFaces();
            for (Integer j : conFaces) {
                int fIndex = faceShapeGroup.getChildIndex(faceShapeGroup
                        .findChild("Face_" + j));
                if (fIndex != -1) {
                    faceShapeGroup.removeChild(fIndex);
                } else {
                    for (PShape s : faceShapeGroup.getChildren()) {
                        PApplet.println("No Shape #" + fIndex
                                + " in ShapeGroup: "
                                + faceShapeGroup.getChildIndex(s));
                    }
                }
                faces.get(j).update();
                faceShapeGroup.addChild(faces.get(j).getShape());
            }
        }
    }

    /**
     * Select.
     */
    public void select() {
        int sel = -1;
        if (selectMode == SELECT_POINTS) {
            for (IPoint p : points.values()) {
                int ret = p.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedPoint = sel;
            PApplet.println("Selected Point " + selectedPoint);
            debugPoint(selectedPoint);
        } else if (selectMode == SELECT_EDGES) {
            for (IEdge e : edges.values()) {
                int ret = e.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedEdge = sel;
            PApplet.println("Selected Edge " + selectedEdge);
            debugEdge(selectedEdge);
        } else if (selectMode == SELECT_FACES) {
            for (IFace f : faces.values()) {
                int ret = f.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedFace = sel;
            PApplet.println("Selected Face " + selectedFace);
        }
    }

    private void debugEdge(int id) {
        if (id != -1) {
            IEdge e = edges.get(id);
            if (e != null) {
                PApplet.println("Connected faces: " + e.getConnectedFaces());
            }
        }
    }

    private void debugPoint(int id) {
        if (id != -1) {
            IPoint p = points.get(id);
            if (p != null) {
                PApplet.println("Connected edges: " + p.getConnectedEdges());
            }
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
            IEdge toDivide = edges.get(selectedEdge);
            IPoint start = toDivide.getA();
            IPoint end = toDivide.getB();
            PVector newPointPos = toDivide.getCentroid();
            ArrayList<Integer> connectedFaces;

            // create new Point
            maxPointId++;
            Point newPoint = new Point(renderLayer, parent, pointShapeGroup,
                    maxPointId, (int) newPointPos.x, (int) newPointPos.y,
                    showHelper);
            pointShapeGroup.addChild(newPoint.getShape());
            selectedPoint = newPoint.select();
            points.put(newPoint.getId(), newPoint);

            // add new edges
            maxEdgeId++;
            Edge e1 = new Edge(renderLayer, parent, edgeShapeGroup, maxEdgeId,
                    start, newPoint, showHelper);

            maxEdgeId++;
            Edge e2 = new Edge(renderLayer, parent, edgeShapeGroup, maxEdgeId,
                    newPoint, end, showHelper);

            // remove edge but remember connected Faces
            edgeShapeGroup.removeChild(edgeShapeGroup.getChildIndex(toDivide
                    .getShape()));
            toDivide.prepareDelete();
            connectedFaces = toDivide.getConnectedFaces();
            edges.remove(toDivide.getId());

            // set connected Faces
            for (Integer i : connectedFaces) {
                // TODO: Another nullpointer workaround issue#11
                IFace face = faces.get(i);
                if (face != null) {
                    face.addEdge(e1);
                    face.addEdge(e2);
                    e1.addConnectedFace(i);
                    e2.addConnectedFace(i);
                } else {
                    PApplet.println("ERROR: connected Face " + i
                            + "is null, issue#11");
                }
            }

            edges.put(e1.getId(), e1);
            edges.put(e2.getId(), e2);
            edgeShapeGroup.addChild(e1.getShape());
            edgeShapeGroup.addChild(e2.getShape());
        }
    }

    /**
     * Switch edge direction.
     */
    public void switchEdgeDirection() {
        if (selectedEdge != -1) {
            IEdge toSwitch = edges.get(selectedEdge);
            IPoint start = toSwitch.getA();
            IPoint end = toSwitch.getB();

            ArrayList<Integer> conFaces;

            // remove edge
            edgeShapeGroup.removeChild(edgeShapeGroup.getChildIndex(toSwitch
                    .getShape()));
            toSwitch.prepareDelete();
            conFaces = toSwitch.getConnectedFaces();
            edges.remove(toSwitch.getId());

            // add new edges first edge reuses id of deleted
            maxEdgeId++;
            Edge e = new Edge(renderLayer, parent, edgeShapeGroup, maxEdgeId,
                    end, start, showHelper);
            for (Integer i : conFaces) {
                e.addConnectedFace(i);
            }
            edges.put(maxEdgeId, e);
            edgeShapeGroup.addChild(e.getShape());
        }
    }
}
