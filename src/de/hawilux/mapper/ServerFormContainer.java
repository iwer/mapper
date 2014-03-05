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

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscStatus;
import processing.core.PApplet;
import processing.core.PVector;
import controlP5.Button;
import controlP5.RadioButton;
import controlP5.Toggle;
import de.hawilux.mapper.file.ServerFileHandler;
import de.hawilux.mapper.net.OscMessagePaths;
import de.hawilux.mapper.net.OscStack;
import de.hawilux.mapper.schedule.TaskQueue;
import de.hawilux.mapper.shapes.IEdge;
import de.hawilux.mapper.shapes.IFace;
import de.hawilux.mapper.shapes.IPoint;
import de.hawilux.mapper.shapes.ServerEdge;
import de.hawilux.mapper.shapes.ServerFace;
import de.hawilux.mapper.shapes.ServerPoint;
import de.hawilux.mapper.ui.FileChooser;

// TODO: Auto-generated Javadoc
/**
 * The Class FormContainer.
 */
public class ServerFormContainer {

    /** The parent. */
    PApplet                  parent;
    public MapperServer      server;

    /** The edges. */
    HashMap<Integer, IEdge>  edges;

    /** The points. */
    HashMap<Integer, IPoint> points;

    /** The faces. */
    HashMap<Integer, IFace>  faces;

    /** The file handler. */
    ServerFileHandler        fileHandler;

    /** The face to build. */
    ServerFace               faceToBuild;

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

    TaskQueue                preDrawTasks;

    OscEventListener         listener;

    /**
     * Instantiates a new form container.
     * 
     * @param parent_
     *            the parent_
     */
    public ServerFormContainer(PApplet parent_, MapperServer server_) {
        parent = parent_;
        server = server_;
        edges = new HashMap<Integer, IEdge>();
        points = new HashMap<Integer, IPoint>();
        faces = new HashMap<Integer, IFace>();

        fileHandler = new ServerFileHandler(this);

        preDrawTasks = new TaskQueue();

        faceToBuild = null;

        selectedPoint = -1;
        selectedEdge = -1;
        selectedFace = -1;

        selectMode = SELECT_POINTS;

        listener = new OscFormContainerListener();

        parent.registerMethod("pre", this);
        try {
            OscStack osc = OscStack.getExistingInstance();
            osc.registerOscMessageCallback(listener, OscMessagePaths.HELPER);
            osc.registerOscMessageCallback(listener, OscMessagePaths.UP);
            osc.registerOscMessageCallback(listener, OscMessagePaths.DOWN);
            osc.registerOscMessageCallback(listener, OscMessagePaths.LEFT);
            osc.registerOscMessageCallback(listener, OscMessagePaths.RIGHT);
            osc.registerOscMessageCallback(listener, OscMessagePaths.SELECTMODE);
            osc.registerOscMessageCallback(listener, OscMessagePaths.NEWCONFIG);
            osc.registerOscMessageCallback(listener, OscMessagePaths.REQFILES);
            osc.registerOscMessageCallback(listener, OscMessagePaths.LOADCONFIG);
            osc.registerOscMessageCallback(listener, OscMessagePaths.SAVECONFIG);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the edge to new face. Considers Face complete when point count does
     * not increase with aa added line. This needs to be improved!
     */
    public void addEdgeToFace() {
        if (selectMode == SELECT_EDGES) {
            if (faceToBuild == null) {
                maxFaceId++;
                faceToBuild = new ServerFace(parent, server, maxFaceId, showHelper);
            }
            select();
            if (selectedEdge != -1) {
                IEdge toAdd = edges.get(selectedEdge);
                int nBefore = faceToBuild.getPoints().size();
                faceToBuild.addEdge(toAdd);
                int nAfter = faceToBuild.getPoints().size();
                // TODO: this needs a better check (line-loop?)
                if (nBefore == nAfter) {
                    faces.put(faceToBuild.getId(), faceToBuild);
                    faceToBuild = null;
                }
            }
        }
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
                p = new ServerPoint(parent, server, maxPointId, server.mouseX, server.mouseY,
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
                IEdge e = new ServerEdge(parent, server, maxEdgeId, a, p, showHelper);
                edges.put(maxEdgeId, e);
                // face.addEdge(e);
            }
        }
    }

    public void load(String filename) {
        fileHandler.loadXML(filename);
    }

    public void save(String filename) {
        fileHandler.saveXML(filename);
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
                IPoint toRemove = points.get(selectedPoint);
                ArrayList<Integer> conEdges = new ArrayList<Integer>(toRemove.getConnectedEdges());
                PApplet.println("Connected Edges at point " + selectedPoint + " : " + conEdges);
                for (Integer i : conEdges) {
                    PApplet.println("CoDelete Edge " + i);
                    ArrayList<Integer> conFaces = new ArrayList<Integer>(edges.get(i)
                            .getConnectedFaces());
                    PApplet.println("Connected Faces at edge " + i + " : " + conFaces);
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
        }
        else if (selectMode == SELECT_EDGES) {
            PApplet.println("Delete Edge " + selectedEdge);
            if (selectedEdge != -1) {
                IEdge toRemove = edges.get(selectedEdge);
                toRemove.prepareDelete();
                ArrayList<Integer> conFaces = new ArrayList<Integer>(toRemove.getConnectedFaces());
                for (Integer i : conFaces) {
                    PApplet.println("CoDelete Face " + i);
                    faces.get(i).prepareDelete();
                    faces.remove(i);
                }
                edges.remove(selectedEdge);
                selectedEdge = -1;
            }
            PApplet.println("Edges:" + edges.size());
        }
        else if (selectMode == SELECT_FACES) {
            PApplet.println("Delete Face " + selectedFace);
            if (selectedFace != -1) {
                IFace toRemove = faces.get(selectedFace);
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
        for (IFace f : faces.values()) {
            if (selectedFace == f.getId()) {
                selected = true;
            }
            else {
                selected = false;
            }
            f.display(config, selected, (selectMode == SELECT_FACES));
        }
        for (IEdge e : edges.values()) {
            if (selectedEdge == e.getId()) {
                selected = true;
            }
            else {
                selected = false;
            }
            e.display(config, selected, (selectMode == SELECT_EDGES));
            if (config && showHelper) {
                e.displayHelper(selected, (selectMode == SELECT_EDGES));
            }
        }
        if (config) {
            for (IPoint p : points.values()) {
                if (selectedPoint == p.getId()) {
                    selected = true;
                }
                else {
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
                p.move();
                ArrayList<Integer> conEdges = p.getConnectedEdges();
                for (Integer i : conEdges) {
                    edges.get(i).update();
                    ArrayList<Integer> conFaces = edges.get(i).getConnectedFaces();
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
            IPoint p = points.get(selectedPoint);
            if (p != null) {
                p.move(dx, dy);
                ArrayList<Integer> conEdges = p.getConnectedEdges();
                for (Integer i : conEdges) {
                    edges.get(i).update();
                    ArrayList<Integer> conFaces = edges.get(i).getConnectedFaces();
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
            for (IPoint p : points.values()) {
                int ret = p.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedPoint = sel;
            PApplet.println("Selected Point " + selectedPoint);
        }
        else if (selectMode == SELECT_EDGES) {
            for (IEdge e : edges.values()) {
                int ret = e.select();
                if (ret != -1) {
                    sel = ret;
                }
            }
            selectedEdge = sel;
            PApplet.println("Selected Edge " + selectedEdge);
        }
        else if (selectMode == SELECT_FACES) {
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

    void selectNextPoint() {
        int currentId = selectedPoint + 1;
        while (!points.containsKey(currentId)) {
            currentId++;
            if (currentId > maxPointId) {
                currentId = 0;
            }
            if (currentId == selectedPoint) {
                return;
            }
        }
        selectedPoint = currentId;
    }

    void selectPreviousPoint() {
        int currentId = selectedPoint - 1;
        while (!points.containsKey(currentId)) {
            currentId--;
            if (currentId < 0) {
                currentId = maxPointId;
            }
            if (currentId == selectedPoint) {
                return;
            }
        }
        selectedPoint = currentId;
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
        }
        else {
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
        if (mode_ == SELECT_POINTS || mode_ == SELECT_EDGES || mode_ == SELECT_FACES) {
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
            ArrayList<Integer> connectedFaces = toDivide.getConnectedFaces();

            // create new Point
            maxPointId++;
            IPoint newPoint = new ServerPoint(parent, server, maxPointId, (int) newPointPos.x,
                    (int) newPointPos.y, showHelper);
            selectedPoint = newPoint.select();
            points.put(newPoint.getId(), newPoint);

            // add new edges
            maxEdgeId++;
            IEdge e1 = new ServerEdge(parent, server, maxEdgeId, start, newPoint, showHelper);

            maxEdgeId++;
            IEdge e2 = new ServerEdge(parent, server, maxEdgeId, newPoint, end, showHelper);

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
            IEdge toSwitch = edges.get(selectedEdge);
            IPoint start = toSwitch.getA();
            IPoint end = toSwitch.getB();

            // remove edge
            toSwitch.prepareDelete();
            edges.remove(toSwitch.getId());

            // add new edges first edge reuses id of deleted
            maxEdgeId++;
            IEdge e = new ServerEdge(parent, server, maxEdgeId, end, start, showHelper);
            for (Integer i : toSwitch.getConnectedFaces()) {
                e.addConnectedFace(i);
            }
            edges.put(maxEdgeId, e);
        }
    }

    public void pre() {
        preDrawTasks.execute();
    }

    class OscFormContainerListener implements OscEventListener {
        @Override
        public void oscEvent(OscMessage theMessage) {
            if (theMessage.checkAddrPattern(OscMessagePaths.HELPER) == true) {
                if (theMessage.get(0).intValue() == 1) {
                    setHelper(true);
                }
                else {
                    setHelper(false);
                }
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.NEXT) == true) {
                if (theMessage.get(0).intValue() == 1) {
                    OscMessage myOscMessage = new OscMessage(OscMessagePaths.SELECTEDPOINT);
                    selectNextPoint();
                    myOscMessage.add(selectedPoint);
                    try {
                        OscStack.getExistingInstance().sendOscMessage(myOscMessage);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.PREVIOUS) == true) {
                if (theMessage.get(0).intValue() == 1) {
                    OscMessage myOscMessage = new OscMessage(OscMessagePaths.SELECTEDPOINT);
                    selectPreviousPoint();
                    myOscMessage.add(selectedPoint);
                    try {
                        OscStack.getExistingInstance().sendOscMessage(myOscMessage);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.UP) == true) {
                preDrawTasks.queueTask(new Runnable() {
                    public void run() {
                        points.get(selectedPoint).move(0, -1);
                    }
                });
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.DOWN) == true) {
                preDrawTasks.queueTask(new Runnable() {
                    public void run() {
                        points.get(selectedPoint).move(0, 1);
                    }
                });
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.LEFT) == true) {
                preDrawTasks.queueTask(new Runnable() {
                    public void run() {
                        points.get(selectedPoint).move(-1, 0);
                    }
                });
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.RIGHT) == true) {
                preDrawTasks.queueTask(new Runnable() {
                    public void run() {
                        points.get(selectedPoint).move(1, 0);
                    }
                });
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.SELECTMODE) == true) {
                setSelectMode(theMessage.get(0).intValue());
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.NEWCONFIG) == true) {
                clear();
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.REQFILES) == true) {
                OscMessage filelist = new OscMessage(OscMessagePaths.FILELIST);
                String[] files = fileHandler.filelist();
                filelist.add(files);
                try {
                    OscStack.getExistingInstance().sendOscMessage(filelist);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.LOADCONFIG) == true) {
                final String filename = theMessage.get(0).stringValue();
                PApplet.println("Loading " + filename);
                preDrawTasks.queueTask(new Runnable() {
                    public void run() {
                        fileHandler.loadXML(filename);
                    }
                });
            }
            else if (theMessage.checkAddrPattern(OscMessagePaths.SAVECONFIG) == true) {
                final String filename = parent.dataPath(theMessage.get(0).stringValue());
                PApplet.println("Saveing " + filename);
                preDrawTasks.queueTask(new Runnable() {
                    public void run() {
                        save(filename);
                    }
                });
            }

        }

        @Override
        public void oscStatus(OscStatus theStatus) {
            PApplet.println("osc status : " + theStatus.id());
        }
    }
}
