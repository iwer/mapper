package de.hawilux.mapper;

import java.util.HashMap;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscStatus;
import processing.core.PApplet;
import processing.core.PConstants;
import de.hawilux.mapper.Mapper.MapperControlListener;
import de.hawilux.mapper.effects.AbstractEffect;
import de.hawilux.mapper.net.OscMessagePaths;
import de.hawilux.mapper.net.OscStack;
import de.hawilux.mapper.schedule.TaskQueue;
import de.hawilux.mapper.shapes.IEdge;
import de.hawilux.mapper.shapes.IFace;
import de.hawilux.mapper.shapes.IPoint;
import de.hawilux.mapper.ui.FileChooser;
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
     * The file chooser. A gui element to choose files for loading and saving
     */
    FileChooser fileChooser;

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

    /** The task queue. */
    TaskQueue preDrawTasks;

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
    OscEventListener listener;

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

        preDrawTasks = new TaskQueue();

        oscStack = OscStack.getInstance(parent, null, "239.0.0.1", 7777);

        listener = new OscMapperServerListener();

        oscStack.registerOscMessageCallback(listener, OscMessagePaths.HELLO);
        oscStack.registerOscMessageCallback(listener, OscMessagePaths.MODESETUP);
        oscStack.registerOscMessageCallback(listener,
                OscMessagePaths.MODEEFFECT);
        oscStack.registerOscMessageCallback(listener, OscMessagePaths.MODEFILE);
        oscStack.registerOscMessageCallback(listener,
                OscMessagePaths.MOUSECOORDS);
        oscStack.registerOscMessageCallback(listener,
                OscMessagePaths.MOUSEBUTTON);

        fileChooser = new FileChooser(parent);
        formContainer = new ServerFormContainer(parent, this);
        cursor = new ServerCursor(parent, this);

        formContainer.setFileChooser(fileChooser);

        effectManager = new ServerEffectManager(this);

        parent.registerMethod("draw", this);
        parent.registerMethod("post", this);
        parent.registerMethod("pre", this);

        PApplet.println("mapper " + version
                + ", Copyright (C) 2013 Iwer Petersen.");

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

    protected void pressMouse() {
        if (setupMode && rightButton) {
            formContainer.select();
        }
        if (setupMode && middleButton) {
            if (formContainer.getSelectMode() == formContainer.SELECT_POINTS) {
                formContainer.addPoint();
            } else if (formContainer.getSelectMode() == formContainer.SELECT_EDGES) {
                formContainer.addEdgeToFace();
            }
        }
        OscMessage myOscMessage = new OscMessage("/mapper/info/selectedpoint");
        myOscMessage.add(formContainer.selectedPoint);
        oscStack.sendOscMessage(myOscMessage);
    }

    protected void dragMouse() {
        if (setupMode && rightButton) {
            formContainer.movePoint();
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
        fileChooser.post();

    }

    public void pre() {
        preDrawTasks.execute();
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
        oscStack.registerOscMessageCallback(listener, "/mapper/effect/"
                + effect.getName().toLowerCase());
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

    class OscMapperServerListener implements OscEventListener { // TODO: This
                                                                // has to be
                                                                // registered at
                                                                // OscStack

        /* incoming osc message are forwarded to the oscEvent method. */
        public void oscEvent(OscMessage theOscMessage) {
            /*
             * print the address pattern and the typetag of the received
             * OscMessage
             */
            OscStack.messageDebug(theOscMessage);

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
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.MODESETUP) == true) {
                if (theOscMessage.get(0).intValue() == 1) {
                    setupMode = true;
                    effectMode = false;
                } else {
                    setupMode = false;
                }
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.MODEEFFECT) == true) {
                if (theOscMessage.get(0).intValue() == 1) {
                    effectMode = true;
                    setupMode = false;
                } else {
                    effectMode = false;
                }
            } else if (theOscMessage.checkAddrPattern(OscMessagePaths.MODEFILE) == true) {
                effectMode = false;
                setupMode = false;
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.MOUSECOORDS) == true) {
                mouseX = theOscMessage.get(0).intValue();
                mouseY = theOscMessage.get(1).intValue();
                preDrawTasks.queueTask(new Runnable() {
                    public void run() {
                        dragMouse();
                    }
                });
            } else if (theOscMessage
                    .checkAddrPattern(OscMessagePaths.MOUSEBUTTON) == true) {
                int buttonNr = theOscMessage.get(0).intValue();
                switch (buttonNr) {
                case 1:
                    middleButton = theOscMessage.typetag().equals("iT");
                    preDrawTasks.queueTask(new Runnable() {
                        public void run() {
                            pressMouse();
                        }
                    });
                    break;
                case 2:
                    rightButton = theOscMessage.typetag().equals("iT");
                    preDrawTasks.queueTask(new Runnable() {
                        public void run() {
                            pressMouse();
                        }
                    });
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
