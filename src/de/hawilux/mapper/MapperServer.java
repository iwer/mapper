package de.hawilux.mapper;

import java.util.HashMap;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscStatus;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import de.hawilux.mapper.effects.AbstractEffect;
import de.hawilux.mapper.net.OscMessagePaths;
import de.hawilux.mapper.net.OscStack;
import de.hawilux.mapper.shapes.IEdge;
import de.hawilux.mapper.shapes.IFace;
import de.hawilux.mapper.shapes.IPoint;
import de.hawilux.mapper.ui.ServerCursor;

public class MapperServer implements PConstants {

    /** The parent PApplet. */
    PApplet parent;

    /**
     * The form container. Contains the points, edges and faces and provides
     * methods to manipulate them
     */
    ServerFormContainer formContainer;

    /**
     * The effect manager. Contains Available Effects and tracks active Effects
     */
    ServerEffectManager effectManager;

    /**
     * The cursor. To find your mouse on the projection
     */
    ServerCursor cursor;

    /**
     * The Mapper Control listener. hooks to ControlP5 to catch events from our
     * gui parts
     */
    MapperControlListener myListener;

    OscStack oscStack;

    /** The show gui flag. */
    boolean showGUI = false;

    /** The show console flag. */
    boolean showConsole = false;

    /** The setup mode flag. */
    boolean setupMode = false;

    /** The effect mode flag. */
    boolean effectMode = false;

    public boolean clientJustConnected;

    public int mouseX;
    public int mouseY;
    boolean middleButton = false;
    boolean rightButton = false;

    /** The version string. */
    String version = "0.1.0";

    /** The Mapper instance. */
    static MapperServer theInstance;

    /**
     * Gets the single instance of Mapper.
     * 
     * @param parent
     *            the parent PApplet
     * @param cp5
     *            the ControlP5 object
     * @return single instance of Mapper
     */
    public static MapperServer getInstance(PApplet parent) {
        if (theInstance == null) {
            theInstance = new MapperServer(parent);
        }
        return theInstance;
    }

    /**
     * Gets the existing instance AFTER getInstance has been called at least
     * once.
     * 
     * Does not need to know the PApplet or ControlP5 object
     * 
     * @return the existing instance
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static MapperServer getExistingInstance()
            throws IllegalAccessException {
        if (theInstance != null) {
            return theInstance;
        } else {
            throw new IllegalAccessException(
                    "Can only be called after getInstance");
        }
    }

    /**
     * Instantiates a new mapper.
     * 
     * @param parent
     *            the parent PApplet
     * @param cp5
     *            the ControlP5 object
     */
    private MapperServer(PApplet parent) {
        this.parent = parent;

        formContainer = new ServerFormContainer(parent, this);
        cursor = new ServerCursor(parent, this);

        effectManager = new ServerEffectManager(this);

        parent.registerMethod("draw", this);
        // parent.registerMethod("dispose", this);
        parent.registerMethod("mouseEvent", this);
        parent.registerMethod("keyEvent", this);
        parent.registerMethod("post", this);

        myListener = new MapperControlListener();

        oscStack = new OscStack(parent, new mapperOscStubProcessor(),
                "239.0.0.1", 7777);

        PApplet.println("mapper "
                + version
                + ", Copyright (C) 2013 Iwer Petersen.\n\n"
                + "  Keys:\n  h - help\n  m - show/hide menus\n  c - show/hide console\n\nHave fun mapping!");

    }

    /**
     * Help.
     */
    void help() {
        PApplet.println("You'll need a 3-button mouse. Points are created by clicking with the middle mouse button in point selection mode. "
                + "Edges are created by selecting two Points with the middle button in point mode, faces are created by"
                + "selecting a loop of edges with the middle button in edge mode."
                + "In points selection mode, points can be moved with the right mouse button. "
                + "Every form can be deleted by selecting it in its selection mode");
    }

    /**
     * Draw.
     * 
     * Gets called by processing, DO NOT CALL YOURSELF!
     */
    public void draw() {
        parent.blendMode(ADD);
        if (setupMode) {
            formContainer.display(setupMode);
            cursor.display();
        } else if (effectMode) {
            for (AbstractEffect ae : effectManager.effectsEnabled.values()) {
                ae.update();
                ae.display();
            }
        } else {
            formContainer.display(setupMode);
        }
    }

    /**
     * Mouse event.
     * 
     * Gets called by processing, DO NOT CALL YOURSELF!
     * 
     * @param event
     *            the event
     */
    public void mouseEvent(MouseEvent event) {
        // int x = event.getX();
        // int y = event.getY();

        switch (event.getAction()) {
        case MouseEvent.PRESS:
            // do something for the mouse being pressed
            pressMouse();
            break;
        case MouseEvent.RELEASE:
            // do something for mouse released
            break;
        case MouseEvent.CLICK:
            // do something for mouse clicked
            break;
        case MouseEvent.DRAG:
            // do something for mouse dragged
            dragMouse();
            break;
        case MouseEvent.MOVE:
            // umm... forgot
            break;
        }
    }

    protected void pressMouse() {
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
        // send selected point
        OscMessage myOscMessage = new OscMessage("/mapper/info/selectedpoint");
        myOscMessage.add(formContainer.selectedPoint);
        oscStack.sendOscMessage(myOscMessage);
    }

    protected void dragMouse() {
        if (setupMode && parent.mouseButton == RIGHT) {
            formContainer.movePoint();
        }
    }

    /**
     * Key event.
     * 
     * Gets called by processing, DO NOT CALL YOURSELF!
     * 
     * @param event
     *            the event
     */
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
                } else if (event.getKey() == 'c') {
                } else if (event.getKey() == 'h') {
                    help();
                } else if (event.getKey() == 's') {
                } else if (event.getKey() == 'l') {
                }
            }
        }
    }

    /**
     * Dispose.
     * 
     * Gets called by processing, DO NOT CALL YOURSELF!
     */
    public void dispose() {
        // Anything in here will be called automatically when
        // the parent sketch shuts down. For instance, this might
        // shut down a thread used by this library.
    }

    /**
     * Post.
     * 
     * Gets called by processing, DO NOT CALL YOURSELF!
     */
    public void post() {
    }

    /**
     * Gets the points.
     * 
     * @return the points
     */
    public HashMap<Integer, IPoint> getPoints() {
        return formContainer.getPoints();
    }

    /**
     * Gets the edges.
     * 
     * @return the edges
     */
    public HashMap<Integer, IEdge> getEdges() {
        return formContainer.getEdges();
    }

    /**
     * Gets the faces.
     * 
     * @return the faces
     */
    public HashMap<Integer, IFace> getFaces() {
        return formContainer.getFaces();
    }

    /**
     * Register effect.
     * 
     * @param effect
     *            an Extension of an AbstractEffect
     */
    public void registerEffect(AbstractEffect effect) {
        effectManager.registerEffect(effect);
    }

    /**
     * List effects available.
     */
    public void listEffectsAvailable() {
        for (String s : effectManager.getEffectsAvailable()) {
            PApplet.println(s);
        }
    }

    /**
     * Enable effect.
     * 
     * @param effectName
     *            the effect name
     */
    public void enableEffect(String effectName) {
        effectManager.enableEffect(effectName);
    }

    /**
     * Disable effect.
     * 
     * @param effectName
     *            the effect name
     */
    public void disableEffect(String effectName) {
        effectManager.disableEffect(effectName);
    }

    /**
     * The listener interface for receiving mapperControl events. The class that
     * is interested in processing a mapperControl event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>addMapperControlListener<code> method. When
     * the mapperControl event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see MapperControlEvent
     */
    class MapperControlListener implements ControlListener {

        /*
         * (non-Javadoc)
         * 
         * @see controlP5.ControlListener#controlEvent(controlP5.ControlEvent)
         */
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
                }

            } else if (theEvent.isController()) {
                // PApplet.println("got something from a controller "
                // + theEvent.getController().getName() + " "
                // + theEvent.getController().getValue());
            }

        }
    }

    class mapperOscStubProcessor implements OscEventListener {

        void selectNextPoint() {
            int currentId = formContainer.selectedPoint + 1;
            while (!formContainer.points.containsKey(currentId)) {
                currentId++;
                if (currentId > formContainer.maxPointId) {
                    currentId = 0;
                }
                if (currentId == formContainer.selectedPoint) {
                    return;
                }
            }
            formContainer.selectedPoint = currentId;
        }

        void selectPreviousPoint() {
            int currentId = formContainer.selectedPoint - 1;
            while (!formContainer.points.containsKey(currentId)) {
                currentId--;
                if (currentId < 0) {
                    currentId = formContainer.maxPointId;
                }
                if (currentId == formContainer.selectedPoint) {
                    return;
                }
            }
            formContainer.selectedPoint = currentId;
        }

        /* incoming osc message are forwarded to the oscEvent method. */
        public void oscEvent(OscMessage theOscMessage) {
            /*
             * print the address pattern and the typetag of the received
             * OscMessage
             */
            PApplet.print("### received an osc message.");
            PApplet.print(" addrpattern: " + theOscMessage.addrPattern());
            PApplet.println(" typetag: " + theOscMessage.typetag());

            if (theOscMessage.checkAddrPattern(OscMessagePaths.HELLO) == true) {
                PApplet.println("Got discovery from "
                        + theOscMessage.netAddress().toString());
                if (theOscMessage.get(0).intValue() == 1) {
                    OscMessage effectMessage = new OscMessage(
                            OscMessagePaths.EFFECTLIST);
                    for (String s : effectManager.getEffectsAvailable()) {
                        effectMessage.add(s);
                    }
                    oscStack.sendOscMessage(effectMessage);

                    OscMessage myOscMessage = new OscMessage(
                            OscMessagePaths.SELECTEDPOINT);
                    myOscMessage.add(formContainer.selectedPoint);
                    oscStack.sendOscMessage(myOscMessage);

                    clientJustConnected = true;
                }
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.NEXT) == true) {
                if (theOscMessage.get(0).intValue() == 1) {
                    OscMessage myOscMessage = new OscMessage(
                            OscMessagePaths.SELECTEDPOINT);
                    selectNextPoint();
                    myOscMessage.add(formContainer.selectedPoint);
                    oscStack.sendOscMessage(myOscMessage);
                }
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.PREVIOUS) == true) {
                if (theOscMessage.get(0).intValue() == 1) {
                    OscMessage myOscMessage = new OscMessage(
                            OscMessagePaths.SELECTEDPOINT);
                    selectPreviousPoint();
                    myOscMessage.add(formContainer.selectedPoint);
                    oscStack.sendOscMessage(myOscMessage);
                }
            } else if (theOscMessage.checkAddrPattern("/mapper/mode/setup") == true) {
                if (theOscMessage.get(0).intValue() == 1) {
                    setupMode = true;
                    effectMode = false;
                } else {
                    setupMode = false;
                }
            } else if (theOscMessage.checkAddrPattern("/mapper/mode/effect") == true) {
                if (theOscMessage.get(0).intValue() == 1) {
                    effectMode = true;
                    setupMode = false;
                } else {
                    effectMode = false;
                }
            } else if (theOscMessage.checkAddrPattern("/mapper/mode/file") == true) {
                effectMode = false;
                setupMode = false;
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.HELPER) == true) {
                if (theOscMessage.get(0).intValue() == 1) {
                    formContainer.setHelper(true);
                } else {
                    formContainer.setHelper(false);
                }
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.UP) == true) {
                formContainer.points.get(formContainer.selectedPoint).move(0,
                        -1);
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.DOWN) == true) {
                formContainer.points.get(formContainer.selectedPoint)
                        .move(0, 1);
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.LEFT) == true) {
                formContainer.points.get(formContainer.selectedPoint).move(-1,
                        0);
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.RIGHT) == true) {
                formContainer.points.get(formContainer.selectedPoint)
                        .move(1, 0);
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.SELECTMODE) == true) {
                formContainer.setSelectMode(theOscMessage.get(0).intValue());
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.NEWCONFIG) == true) {
                formContainer.clear();
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.REQFILES) == true) {
                OscMessage filelist = new OscMessage(OscMessagePaths.FILELIST);
                String[] files = { "test.xml", "test1.xml" };// get from
                                                             // filehandler
                filelist.add(files);
                oscStack.sendOscMessage(filelist);
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.LOADCONFIG) == true) {
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.SAVECONFIG) == true) {
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.MOUSECOORDS) == true) {
                mouseX = theOscMessage.get(0).intValue();
                mouseY = theOscMessage.get(1).intValue();
                dragMouse();
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.MOUSEBUTTON) == true) {
                int buttonNr = theOscMessage.get(0).intValue();
                switch (buttonNr) {
                case 1:
                    middleButton = theOscMessage.typetag().equals("iT");
                    pressMouse();
                    break;
                case 2:
                    rightButton = theOscMessage.typetag().equals("iT");
                    pressMouse();
                    break;
                }
            } else {
                for (String s : effectManager.getEffectsAvailable()) {
                    if (theOscMessage.checkAddrPattern("/mapper/effect/"
                            + s.toLowerCase()) == true) {
                        PApplet.println("State of " + s + " is "
                                + theOscMessage.get(0).intValue());
                        if (theOscMessage.get(0).intValue() == 0) {
                            effectManager.disableEffect(s);
                        } else
                            effectManager.enableEffect(s);
                    }
                }
            }
        }

        @Override
        public void oscStatus(OscStatus theStatus) {
            PApplet.println("OSC Status: " + theStatus);

        }
    }
}
