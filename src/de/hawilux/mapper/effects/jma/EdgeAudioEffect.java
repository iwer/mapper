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
package de.hawilux.mapper.effects.jma;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Slider2D;
import controlP5.Toggle;
import ddf.minim.AudioInput;
import de.hawilux.mapper.effects.AbstractEffect;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.ui.VolumeBar;

public class EdgeAudioEffect extends AbstractEffect implements PConstants {
    HashMap<Integer, Edge> edges;

    AudioInput in;

    ArrayDeque<ExpandingCircle> circles;
    ArrayList<ExpandingCircle> circlesToRemove;
    PGraphics buffer;

    int cBlack;
    int cWhite;
    int cRed;

    float threshold = .45f;
    float speed = 70;
    float duration = 30;
    PVector position;
    boolean debug = false;

    Slider slThreshold;
    Slider slSpeed;
    Slider slDuration;
    Slider2D slPosition;
    Toggle tglDebug;
    VolumeBar volumeBar;

    public EdgeAudioEffect(PApplet parent_, HashMap<Integer, Edge> edges_,
            AudioInput in_) {
        super(parent_, "edgeaudio");
        edges = edges_;

        cBlack = parent.color(0);
        cWhite = parent.color(255);
        cRed = parent.color(255, 0, 0);

        buffer = parent.createGraphics(parent.width, parent.height, JAVA2D);
        circles = new ArrayDeque<ExpandingCircle>();
        circlesToRemove = new ArrayList<ExpandingCircle>();
        position = new PVector(parent.width / 2, parent.height / 2);

        in = in_;
    }

    public void addEffectControllersToGui(Gui gui) {
        volumeBar = new VolumeBar(gui.getCp5(), in, name, 10, 10, 100, 10);
        volumeBar.getRmsbarSlider().moveTo(grpEffectParams);
        slThreshold = gui.getCp5().addSlider("edgeAudioThreshold")
                .setCaptionLabel("threshold").setPosition(10, 35)
                .setValue(threshold).setColor(gui.getC())
                .setRange(0.001f, .999f).moveTo(grpEffectParams);
        slThreshold.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    threshold = slThreshold.getValue();
                }
            }
        });
        slSpeed = gui.getCp5().addSlider("edgeAudioSpeed")
                .setCaptionLabel("speed").setPosition(10, 60).setValue(speed)
                .setColor(gui.getC()).setRange(10, 200).moveTo(grpEffectParams);
        slSpeed.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    speed = slSpeed.getValue();
                }
            }
        });
        slDuration = gui.getCp5().addSlider("edgeAudioDuration")
                .setCaptionLabel("duration").setPosition(10, 85)
                .setValue(duration).setColor(gui.getC()).setRange(10, 200)
                .moveTo(grpEffectParams);
        slDuration.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    duration = slDuration.getValue();
                }
            }
        });
        slPosition = gui
                .getCp5()
                .addSlider2D("edgeAudioPosition")
                .setCaptionLabel("position")
                .setPosition(10, 110)
                .setColor(gui.getC())
                .setSize(100, 100)
                .setMinX(0)
                .setMaxX(parent.width)
                .setMinY(0)
                .setMaxY(parent.height)
                .setArrayValue(
                        new float[] { parent.width / 2, parent.height / 2 })
                .moveTo(grpEffectParams);
        slPosition.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    float pos[] = slPosition.getArrayValue();
                    position = new PVector(pos[0], pos[1]);
                }
            }
        });

        tglDebug = gui.getCp5().addToggle("edgeAudioDebug")
                .setCaptionLabel("debug").setPosition(10, 225).setValue(debug)
                .setColor(gui.getC()).moveTo(grpEffectParams);
        tglDebug.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    debug = (tglDebug.getValue() == 1);
                }
            }
        });

    }

    public void update() {
        volumeBar.update();
        float value = in.mix.level();
        if (value > threshold) {
            circles.add(new ExpandingCircle(parent.frameCount, position, value));
        }

        buffer.beginDraw();
        buffer.background(0);
        buffer.pushMatrix();
        int count = 0;
        for (ExpandingCircle ec : circles) {
            ec.display();
            if (ec.r > parent.width * 2 || count > 4) {
                circlesToRemove.add(ec);
            } else {
                count++;
            }
        }
        buffer.endDraw();
        buffer.popMatrix();

        circles.removeAll(circlesToRemove);
        circlesToRemove = new ArrayList<ExpandingCircle>();
    }

    public void display() {
        // for debug
        if (debug) {
            parent.blendMode(ADD);
            parent.image(buffer, 0, 0);
        }
        for (Edge e : edges.values()) {
            if (buffer
                    .get((int) (e.getCentroid().x), (int) (e.getCentroid().y)) == cRed) {
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
            circle = parent.createShape(ELLIPSE, pos.x - (r / 2), pos.y
                    - (r / 2), r, r);
            circle.setFill(false);
            circle.setStroke(cRed);
            circle.setStrokeWeight(duration);
            buffer.shape(circle);
        }
    }
}
