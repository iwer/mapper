import de.hawilux.mapper.ui.cp5view.ArrowButton;
import de.hawilux.mapper.net.OscStack;
import de.hawilux.mapper.net.OscMessagePaths;
import oscP5.*;
import controlP5.*;

ControlP5 cp5;
Accordion mainAccordion;
Group effectGroup;
Group setupGroup;
Textlabel selectedPointsLabel;
CColor colorScheme;

OscStack oscStack;

boolean connection_initialized;

public void setup() {
  size(displayWidth, displayHeight, P2D);
  // size(480, 800, P2D);
  frameRate(15);
  orientation(PORTRAIT);

  cp5 = new ControlP5(this);

  oscStack = new OscStack(this, new mapperRemoteAndroidProcessor(), 
  "239.0.0.1", 7777);

  PFont pfont = createFont("Mono", 20, true); // use true/false for
  // smooth/no-smooth
  ControlFont font = new ControlFont(pfont, 30);
  ControlFont smallFont = cp5.getFont();

  colorScheme = ControlP5.getColor();

  cp5.setFont(font);

  cp5.addFrameRate().setInterval(10).setPosition(10, 10)
    .setFont(smallFont);
  cp5.addTextlabel("fps").setPosition(25, 10).setFont(smallFont)
    .setText("FPS");

  setupGroup = cp5.addGroup("setup").setBarHeight(50)
    .setBackgroundHeight(600);
  // setupGroup.getCaptionLabel().getStyle().setPadding(5,5,4,5);

  cp5.addButton("previous").setSize(width / 2 - 20, 50)
    .setPosition(0, 10).moveTo(setupGroup);
  cp5.addButton("next").setSize(width / 2 - 20, 50)
    .setPosition(width / 2, 10).moveTo(setupGroup);

  selectedPointsLabel = cp5.addTextlabel("selected").setPosition(0, 80)
    .setText("Nr: ").moveTo(setupGroup);

  cp5.addButton("up").setCaptionLabel("").setPosition(width / 2 - 60, 80)
    .setSize(100, 100)
      .setView(new ArrowButton(this, ArrowButton.UP))
        .moveTo(setupGroup);
  cp5.addButton("left").setCaptionLabel("")
    .setPosition(width / 2 - 160, 180).setSize(100, 100)
      .setView(new ArrowButton(this, ArrowButton.LEFT))
        .moveTo(setupGroup);
  cp5.addButton("right").setCaptionLabel("")
    .setPosition(width / 2 + 40, 180).setSize(100, 100)
      .setView(new ArrowButton(this, ArrowButton.RIGHT))
        .moveTo(setupGroup);
  cp5.addButton("down").setCaptionLabel("")
    .setPosition(width / 2 - 60, 280).setSize(100, 100)
      .setView(new ArrowButton(this, ArrowButton.DOWN))
        .moveTo(setupGroup);

  cp5.addToggle("helper").setSize(width / 2 - 20, 50).setPosition(0, 400)
    .moveTo(setupGroup);

  effectGroup = cp5.addGroup("effect").setBarHeight(50);

  mainAccordion = cp5.addAccordion("acc").setPosition(10, 30)
    .setWidth(width - 20).addItem(setupGroup).addItem(effectGroup);
  mainAccordion.setCollapseMode(Accordion.SINGLE);

  cp5.hide();
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

int effectCnt = 0;
int yPos = 10;

void addEffectToggle(String effectname) {
  Toggle t;
  if (effectCnt % 2 == 0) {
    t = cp5.addToggle(effectname.toLowerCase())
      .setSize(width / 2 - 20, 50).setPosition(0, yPos)
        .moveTo(effectGroup);
  } 
  else {
    t = cp5.addToggle(effectname.toLowerCase())
      .setSize(width / 2 - 20, 50).setPosition(width / 2, yPos)
        .moveTo(effectGroup);
    yPos += 100;
  }

  t.addCallback(new CallbackListener() {
    @Override
      public void controlEvent(CallbackEvent theEvent) {
      if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
        oscStack.sendOscMessage("/mapper/effect/"
          + theEvent.getController().getName(), (int) theEvent
          .getController().getValue());
      }
    }
  }
  );
  effectCnt++;
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
      println("failure");
      exit();
      return;
    }
  } 
  else {
    background(0);
    cp5.show();
  }
}

class mapperRemoteAndroidProcessor implements OscEventListener {
  public void oscEvent(OscMessage theOscMessage) {
    println("Received message: \n Path: " + theOscMessage.addrPattern()
      + "\n Types: " + theOscMessage.typetag());
    if (theOscMessage.checkAddrPattern(OscMessagePaths.SELECTEDPOINT) == true) {
      connection_initialized = true;
      selectedPointsLabel.setText("Nr: "
        + theOscMessage.get(0).intValue());
    }
    if (theOscMessage.checkAddrPattern(OscMessagePaths.EFFECTLIST) == true) {
      connection_initialized = true;
      byte[] typeTag = theOscMessage.getTypetagAsBytes();
      for (int i = 0; i < typeTag.length; i++) {
        if (typeTag[i] == 's') {
          println(theOscMessage.get(i).stringValue());
          addEffectToggle(theOscMessage.get(i).stringValue());
        }
      }
    }
  }

  public void oscStatus(OscStatus theStatus) {
    // TODO Auto-generated method stub
  }
}

