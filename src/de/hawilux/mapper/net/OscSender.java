package de.hawilux.mapper.net;

import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class OscSender {
    OscP5 oscP5;

    public OscSender(PApplet parent, String multicastIP, int multicastPort) {
        oscP5 = new OscP5(parent, multicastIP, multicastPort, OscP5.MULTICAST);
    }

    void sendOscMessage(String path, int value) {
        OscMessage myOscMessage = new OscMessage(path);
        myOscMessage.add(value);
        oscP5.send(myOscMessage);
    }
}
