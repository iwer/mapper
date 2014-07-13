/*
 *   An effect for Mapper2K
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   Belongs to Janina Schlichtes Masterthesis until further notice
 *
 * EdgeAudioEffect class
 *
 *  
 *
 */
import ddf.minim.*;
import java.util.*;

class EdgeAudioEffect extends AbstractEffect {
  HashMap<Integer, Edge> edges;

  AudioInput in;

  ArrayDeque<ExpandingCircle> circles;
  ArrayList<ExpandingCircle> circlesToRemove;
  PGraphics buffer;

  color cBlack = color(0);
  color cWhite = color(255);
  color cRed = color(255, 0, 0);

  float threshold = .45;
  float speed = 70;
  float duration = 30;
  PVector position;
  boolean debug = false;

  Group grpEffectParams;
  Slider slThreshold;
  Slider slSpeed;
  Slider slDuration;
  Slider2D slPosition;
  Toggle tglDebug;

  EdgeAudioEffect(PApplet parent_, HashMap<Integer, Edge> edges_, AudioInput in_) {
    parent = parent_;
    edges = edges_;

    buffer = createGraphics(parent.displayWidth, parent.displayHeight, JAVA2D);
    circles = new ArrayDeque<ExpandingCircle>();
    circlesToRemove = new ArrayList<ExpandingCircle>();
    position = new PVector(parent.width/2, parent.height/2);

    in = in_;
  }
  public void addEffectControllersToGui(Gui gui) {
    grpEffectParams =  gui.getCp5().addGroup("edgeaudio").setColor(gui.getC()).setBackgroundHeight(235);
    gui.getEffectAccordion().addItem(grpEffectParams);
    slThreshold = gui.getCp5().addSlider("edgeAudioThreshold").setCaptionLabel("threshold").setPosition(10, 10).setValue(threshold).setColor(gui.getC()).setRange(0.001, .999).moveTo(grpEffectParams);
    slThreshold.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          threshold = slThreshold.getValue();
        }
      }
    }
    );
    slSpeed  = gui.getCp5().addSlider("edgeAudioSpeed").setCaptionLabel("speed").setPosition(10, 35).setValue(speed).setColor(gui.getC()).setRange(10, 200).moveTo(grpEffectParams);
    slSpeed.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          speed = slSpeed.getValue();
        }
      }
    }
    );
    slDuration = gui.getCp5().addSlider("edgeAudioDuration").setCaptionLabel("duration").setPosition(10, 60).setValue(duration).setColor(gui.getC()).setRange(10, 200).moveTo(grpEffectParams);
    slDuration.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          duration = slDuration.getValue();
        }
      }
    }
    );
    slPosition = gui.getCp5().addSlider2D("edgeAudioPosition").setCaptionLabel("position").setPosition(10, 85).setColor(gui.getC()).setSize(100, 100).setMinX(0).setMaxX(parent.width).setMinY(0).setMaxY(parent.height).setArrayValue(new float[] {
      parent.width/2, parent.height/2
    }
    ).moveTo(grpEffectParams);
    slPosition.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          float pos[] = slPosition.getArrayValue();
          position = new PVector(pos[0], pos[1]);
        }
      }
    }
    );

    tglDebug = gui.getCp5().addToggle("edgeAudioDebug").setCaptionLabel("debug").setPosition(10, 200).setValue(debug).setColor(gui.getC()).moveTo(grpEffectParams);
    tglDebug.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
          debug = (tglDebug.getValue() == 1);
        }
      }
    }
    );
    //gui.getRdbEffects().addItem("EdgeAudioEffect", AbstractEffect.EDGE_AUDIO);
  }
  public void update() {
    for (int i = 0; i < 1; i++) {
      float value = in.mix.get(i); 
      if (value > threshold) {
        circles.add(new ExpandingCircle(parent.frameCount, position, value));
      }
    }

    buffer.beginDraw();
    buffer.background(0);
    buffer.pushMatrix();
    int count = 0;
    for (ExpandingCircle ec : circles) {
      ec.display();
      if (ec.r > parent.width * 1|| count > 4) {
        circlesToRemove.add(ec);
      } 
      else {
        count++;
      }
    }
    buffer.endDraw();
    buffer.popMatrix();

    circles.removeAll(circlesToRemove);
    circlesToRemove = new ArrayList<ExpandingCircle>();
  }
  public void display() {
    //for debug
    if (debug) {
      parent.image(buffer, 0, 0);
    }
    for (Edge e : edges.values()) {
      if (buffer.get(int(e.getGrabberPos().x), int(e.getGrabberPos().y)) == cRed) {
        e.getShape().setStroke(cWhite);
        parent.shapeMode(CORNERS);
        parent.shape(e.getShape());
      }
    }
  }

  class ExpandingCircle {
    int time;
    PVector pos;
    float value;
    float r = 0;

    PShape circle;

    ExpandingCircle(int time_, PVector pos_, float value_) {
      time = time_;
      pos = pos_;
      value = value_;
    }

    void display() {
      int timeDiff = parent.frameCount - time;
      r = timeDiff * speed * value;
      circle = createShape(ELLIPSE, pos.x-(r/2), pos.y-(r/2), r, r);
      circle.setFill(false);
      circle.setStroke(color(255, 0, 0));
      circle.setStrokeWeight(duration);
      buffer.shape(circle);
    }
  }
}
