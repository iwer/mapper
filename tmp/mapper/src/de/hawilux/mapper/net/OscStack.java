package de.hawilux.mapper.net;

import java.util.HashMap;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import processing.core.PApplet;

public class OscStack implements OscEventListener {

    static OscStack                   theInstance;

    OscP5                             oscP5;
    // OscEventListener listener; // TODO: this out for this:
    HashMap<String, OscEventListener> oscReceivers;

    public static OscStack getInstance(PApplet parent,
            OscEventListener listener_, String multicastIP, int multicastPort) {
        if (theInstance == null) {
            theInstance = new OscStack(parent, listener_, multicastIP,
                    multicastPort);
        }
        return theInstance;
    }

    public static OscStack getExistingInstance() throws IllegalAccessException {
        if (theInstance != null) {
            return theInstance;
        } else {
            throw new IllegalAccessException(
                    "Can only be called after getInstance");
        }
    }

    private OscStack(PApplet parent, OscEventListener listener_,
            String multicastIP, int multicastPort) {
        oscP5 = new OscP5(parent, multicastIP, multicastPort, OscP5.MULTICAST);

        oscReceivers = new HashMap<String, OscEventListener>();

        // this.listener = listener_;
        // oscP5.addListener(listener);

        oscP5.addListener(this);
    }

    public void registerOscMessageCallback(OscEventListener listener_,
            String oscPath_) {
        if (oscReceivers.get(oscPath_) == null) {
            PApplet.println("Registering receiver for '" + oscPath_ + "'");
            oscReceivers.put(oscPath_, listener_);
        } else {
            PApplet.println("Receiver for '" + oscPath_
                    + "' already registered");
        }
    }

    public void oscEvent(OscMessage theOscMessage) {
        String path = theOscMessage.addrPattern();
        OscEventListener l = oscReceivers.get(path);
        if (l != null) {
            l.oscEvent(theOscMessage);
        }
    }

    public void sendOscMessage(OscMessage message) {
        oscP5.send(message);
    }

    public void sendOscMessage(String path, int value) {
        OscMessage myOscMessage = new OscMessage(path);
        myOscMessage.add(value);
        oscP5.send(myOscMessage);
    }

    public void sendOscMessage(String path, float f) {
        OscMessage myOscMessage = new OscMessage(path);
        myOscMessage.add(f);
        oscP5.send(myOscMessage);
    }

    public void sendOscMessage(String path, boolean b) {
        OscMessage myOscMessage = new OscMessage(path);
        myOscMessage.add(b);
        oscP5.send(myOscMessage);
    }

    @Override
    public void oscStatus(OscStatus theStatus) {
    }

    public static void messageDebug(OscMessage theMessage) {
        // PApplet.print("#-----# OSC #");
        // PApplet.print(" addrpattern: " + theMessage.addrPattern());
        // PApplet.print(" typetag: " + theMessage.typetag());
        byte[] typeTag = theMessage.getTypetagAsBytes();
        for (int i = 0; i < typeTag.length; i++) {
            switch (typeTag[i]) {
            case 's':
                PApplet.print(" " + theMessage.get(i).stringValue() + ",");
                break;
            case 'f':
                PApplet.print(" " + theMessage.get(i).floatValue() + ",");
                break;
            case 'i':
                PApplet.print(" " + theMessage.get(i).intValue() + ",");
                break;
            default:
                break;
            }
        }
        PApplet.println();
    }
}
