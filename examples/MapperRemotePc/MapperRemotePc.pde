import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.ui.AndroidRemoteGui;
import de.hawilux.mapper.ui.cp5view.ArrowButton;
import de.hawilux.mapper.net.OscStack;
import de.hawilux.mapper.net.OscMessagePaths;
import oscP5.*;
import controlP5.*;

ControlP5 cp5;

OscStack oscStack;

PcRemoteGui gui;

boolean connection_initialized;

public void setup() {
  // size(displayWidth, displayHeight, P2D);
  size(800, 800, P2D);
  frameRate(15);
  orientation(PORTRAIT);

  cp5 = new ControlP5(this);
  gui = new PcRemoteGui(this, cp5);
  cp5.hide();

  oscStack = new OscStack(this, new mapperRemotePcProcessor(), 
  "239.0.0.1", 7777);
}

void connectToMapper() {
  oscStack.sendOscMessage(OscMessagePaths.HELLO, 1);
}

public void controlEvent(ControlEvent theEvent) {
  if (theEvent.isGroup()) {
    println("got an event from group " + theEvent.getGroup().getName()
      + ", isOpen? " + theEvent.getGroup().isOpen());
    if (theEvent.getGroup().getName().equals("setup")) {
      oscStack.sendOscMessage("/mapper/mode/setup", theEvent
        .getGroup().isOpen() ? 1 : 0);
    } 
    else if (theEvent.getGroup().getName().equals("effect")) {
      oscStack.sendOscMessage("/mapper/mode/effect", theEvent
        .getGroup().isOpen() ? 1 : 0);
    }
  } 
  else if (theEvent.isController()) {
    println("got something from a controller "
      + theEvent.getController().getName());
    if (theEvent.getController().getName().equals("previous")) {
      oscStack.sendOscMessage(OscMessagePaths.PREVIOUS, 1);
    } 
    else if (theEvent.getController().getName().equals("next")) {
      oscStack.sendOscMessage(OscMessagePaths.NEXT, 1);
    } 
    else if (theEvent.getController().getName().equals("up")) {
      oscStack.sendOscMessage(OscMessagePaths.UP, 1);
    } 
    else if (theEvent.getController().getName().equals("left")) {
      oscStack.sendOscMessage(OscMessagePaths.LEFT, 1);
    } 
    else if (theEvent.getController().getName().equals("right")) {
      oscStack.sendOscMessage(OscMessagePaths.RIGHT, 1);
    } 
    else if (theEvent.getController().getName().equals("down")) {
      oscStack.sendOscMessage(OscMessagePaths.DOWN, 1);
    } 
    else if (theEvent.getController().getName().equals("helper")) {
      if (theEvent.getController().getValue() == 0) {
        oscStack.sendOscMessage(OscMessagePaths.HELPER, 0);
      } 
      else {
        oscStack.sendOscMessage(OscMessagePaths.HELPER, 1);
      }
    }
  }
}

int connectTime = 0;
String startupText = "Connecting to mapper...";

public void draw() {
  background(0);
  if (!connection_initialized) {
    fill(255);
    textSize(30);
    text(startupText, 10, 10, width - 20, height - 20);
    // connect
    if (connectTime < 60) {
      if (connectTime % 10 == 0) {
        startupText = "Retrying...";
        connectToMapper();
      }
      delay(1000);
      startupText += ".";
      print(".");
      connectTime++;
    } 
    else {
      startupText += "failed!";
      println("connection failure");
      exit();
      return;
    }
  } 
  else {
    background(0);
    cp5.show();
  }
}

class mapperRemotePcProcessor implements OscEventListener {

  @Override
    public void oscEvent(OscMessage theOscMessage) {
    println("Received message: \n Path: " + theOscMessage.addrPattern()
      + "\n Types: " + theOscMessage.typetag());
    if (theOscMessage.checkAddrPattern(OscMessagePaths.SELECTEDPOINT) == true) {
      connection_initialized = true;
      gui.setSelectedPointsLabelText("Nr: "
        + theOscMessage.get(0).intValue());
    }
    if (theOscMessage.checkAddrPattern(OscMessagePaths.EFFECTLIST) == true) {
      connection_initialized = true;
//      byte[] typeTag = theOscMessage.getTypetagAsBytes();
//      for (int i = 0; i < typeTag.length; i++) {
//        if (typeTag[i] == 's') {
//          println(theOscMessage.get(i).stringValue());
//          gui.addEffectToggle(theOscMessage.get(i).stringValue(), 
//          oscStack);
//        }
//      }
    }
  }

  @Override
    public void oscStatus(OscStatus theStatus) {
    // TODO Auto-generated method stub
  }
}
