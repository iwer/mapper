package de.hawilux.mapper.net;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class OscStack {
    OscP5 oscP5;
    OscEventListener listener;

    public OscStack(PApplet parent, OscEventListener listener,
            String multicastIP, int multicastPort) {
        oscP5 = new OscP5(parent, multicastIP, multicastPort, OscP5.MULTICAST);
        this.listener = listener;
        oscP5.addListener(listener);
    }

    public void sendOscMessage(OscMessage message) {
        oscP5.send(message);
    }

    public void sendOscMessage(String path, int value) {
        OscMessage myOscMessage = new OscMessage(path);
        myOscMessage.add(value);
        oscP5.send(myOscMessage);
    }

    public void oscMessage(OscMessage theOscMessage) {
    }

	public void sendOscMessage(String path, boolean b) {
        OscMessage myOscMessage = new OscMessage(path);
        myOscMessage.add(b);
        oscP5.send(myOscMessage);		
	}
}
