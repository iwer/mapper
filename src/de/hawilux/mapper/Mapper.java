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
 * Mapper class
 *
 * The library's main class. Makes sure everything is set up correctly
 */
package de.hawilux.mapper;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.shapes.Face;
import de.hawilux.mapper.shapes.Point;
import de.hawilux.mapper.ui.Cursor;
import de.hawilux.mapper.ui.FileChooser;
import de.hawilux.mapper.ui.Gui;

public class Mapper implements PConstants {
    PApplet parent;

    Gui gui;
    FormContainer formContainer;
    FileChooser fileChooser;
    Cursor cursor;

    MapperControlListener myListener;

    boolean showGUI = false;
    boolean showConsole = false;

    boolean setupMode = false;
    boolean effectMode = false;
    String version = "0.1.0";

    public Mapper(PApplet parent, ControlP5 cp5) {
        this.parent = parent;

        fileChooser = new FileChooser(parent);
        formContainer = new FormContainer(parent);
        cursor = new Cursor(parent);

        gui = new Gui(parent, cp5);
        fileChooser.setGui(gui);
        formContainer.setFileChooser(fileChooser);
        formContainer.addFileGui(gui);

        parent.registerMethod("draw", this);
        // parent.registerMethod("dispose", this);
        parent.registerMethod("mouseEvent", this);
        parent.registerMethod("keyEvent", this);
        parent.registerMethod("post", this);

        showGUI = true;
        showConsole = true;

        myListener = new MapperControlListener();

        gui.getCp5().addListener(myListener);

        PApplet.println("mapper "
                + version
                + ", Copyright (C) 2013 Iwer Petersen.\n\n"
                + "  Keys:\n  h - help\n  m - show/hide menus\n  c - show/hide console\n\nHave fun mapping!");

    }

    void help() {
        PApplet.println("You'll need a 3-button mouse. Points are created by clicking with the middle mouse button in point selection mode. "
                + "Edges are created by selecting two Points with the middle button in point mode, faces are created by"
                + "selecting a loop of edges with the middle button in edge mode."
                + "In points selection mode, points can be moved with the right mouse button. "
                + "Every form can be deleted by selecting it in its selection mode");
    }

    public void draw() {
        if (setupMode) {
            formContainer.display(setupMode);
            cursor.display();
        } else {
            formContainer.display(setupMode);
        }
    }

    public void mouseEvent(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();

        switch (event.getAction()) {
        case MouseEvent.PRESS:
            // do something for the mouse being pressed
            if (setupMode && parent.mouseButton == RIGHT) {
                formContainer.select();
            }
            if (setupMode && parent.mouseButton == CENTER) {
                if (formContainer.getSelectMode() == formContainer.SELECT_POINTS) {
                    formContainer.addPoint();
                } else if (formContainer.getSelectMode() == formContainer.SELECT_EDGES) {
                    formContainer.addEdgeToFace();
                }
            }
            break;
        case MouseEvent.RELEASE:
            // do something for mouse released
            break;
        case MouseEvent.CLICK:
            // do something for mouse clicked
            break;
        case MouseEvent.DRAG:
            // do something for mouse dragged
            if (setupMode && parent.mouseButton == RIGHT) {
                formContainer.movePoint();
            }
            break;
        case MouseEvent.MOVE:
            // umm... forgot
            break;
        }
    }

    public void keyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.PRESS) {
            if (event.getKey() == CODED) {
                if (event.getKeyCode() == UP) {
                    formContainer.movePoint(0, -1);
                } else if (event.getKeyCode() == DOWN) {
                    formContainer.movePoint(0, 1);
                } else if (event.getKeyCode() == LEFT) {
                    formContainer.movePoint(-1, 0);
                } else if (event.getKeyCode() == RIGHT) {
                    formContainer.movePoint(1, 0);
                }
            } else {
                if (event.getKey() == 'm') {
                    if (gui != null) {
                        showGUI = !showGUI;
                        gui.show(showGUI);
                    }
                } else if (event.getKey() == 'c') {
                    if (gui != null) {
                        showConsole = !showConsole;
                        gui.setConsoleEnabled(showConsole);
                    }
                } else if (event.getKey() == 'h') {
                    help();
                } else if (event.getKey() == 's') {
                    gui.saveGuiProperties();
                } else if (event.getKey() == 'l') {
                    gui.loadGuiProperties();
                }
            }
        }
    }

    public void dispose() {
        // Anything in here will be called automatically when
        // the parent sketch shuts down. For instance, this might
        // shut down a thread used by this library.
    }

    public void post() {
        fileChooser.post();
    }

    public HashMap<Integer, Point> getPoints() {
        return formContainer.getPoints();
    }

    public HashMap<Integer, Edge> getEdges() {
        return formContainer.getEdges();
    }

    public HashMap<Integer, Face> getFaces() {
        return formContainer.getFaces();
    }

    class MapperControlListener implements ControlListener {
        public void controlEvent(ControlEvent theEvent) {
            if (theEvent.isGroup()) {
                PApplet.println("got an event from group "
                        + theEvent.getGroup().getName() + ", isOpen? "
                        + theEvent.getGroup().isOpen());
                if (theEvent.getGroup().getName() == "selectmode") {
                    int smode = (int) (theEvent.getGroup().getValue()) - 1;
                    formContainer.setSelectMode(smode);
                    if (formContainer.getRdbSelectMode() != null) {
                        if (smode == formContainer.SELECT_EDGES) {
                            formContainer.setLineEditorEnabled(true);
                        } else {
                            formContainer.setLineEditorEnabled(false);
                        }
                    }
                } else if (theEvent.getGroup().getName() == "file") {
                    setupMode = false;
                    effectMode = false;
                } else if (theEvent.getGroup().getName() == "setup") {
                    setupMode = theEvent.getGroup().isOpen();
                    effectMode = false;
                } else if (theEvent.getGroup().getName() == "effect") {
                    effectMode = theEvent.getGroup().isOpen();
                    setupMode = false;
                } else if (theEvent.getGroup().getName() == "chooseneffect") {
                    int effectNr = (int) (theEvent.getGroup().getValue());
                    switch (effectNr) {
                    case 1:
                        effectMode = false;
                        break;
                    default:
                        setupMode = false;
                        break;
                    }
                }
            } else if (theEvent.isController()) {
                PApplet.println("got something from a controller "
                        + theEvent.getController().getName() + " "
                        + theEvent.getController().getValue());
            }

        }
    }
}
