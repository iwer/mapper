package de.hawilux.mapper.net;

import oscP5.OscMessage;
import processing.core.PApplet;

public class OscReceiver {

    void processOscEvent(OscMessage theOscMessage) {
        /* print the address pattern and the typetag of the received OscMessage */
        PApplet.print("### received an osc message.");
        PApplet.print(" addrpattern: " + theOscMessage.addrPattern());
        PApplet.println(" typetag: " + theOscMessage.typetag() + " value: "
                + theOscMessage.get(0).intValue());

        /* check if theOscMessage has the address pattern we are looking for. */

        if (theOscMessage.checkAddrPattern(OscMessagePaths.HELLO) == true) {

        } else if (theOscMessage.checkAddrPattern(OscMessagePaths.NEXT) == true) {

        } else if (theOscMessage.checkAddrPattern(OscMessagePaths.PREVIOUS) == true) {

        } else if (theOscMessage.checkAddrPattern(OscMessagePaths.UP) == true) {

        } else if (theOscMessage.checkAddrPattern(OscMessagePaths.DOWN) == true) {

        } else if (theOscMessage.checkAddrPattern(OscMessagePaths.LEFT) == true) {

        } else if (theOscMessage.checkAddrPattern(OscMessagePaths.RIGHT) == true) {

        } else if (theOscMessage.checkAddrPattern(OscMessagePaths.HELPER) == true) {

        }

    }
}
