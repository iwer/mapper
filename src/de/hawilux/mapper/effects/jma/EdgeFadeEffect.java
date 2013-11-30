/*
 *   An effect for Mapper2K
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   Belongs to Janina Schlichtes Masterthesis until further notice
 *
 * EdgeFadeEffect class
 *
 * a edge hit effect. 
 *
 */
package de.hawilux.mapper.effects.jma;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PConstants;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Slider;
import de.hawilux.mapper.effects.AbstractEffect;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.ui.Gui;

class EdgeFadeEffect extends AbstractEffect implements PConstants {
    HashMap<Integer, Edge> edges;
    HashMap<Integer, Integer> fadeInterval;

    int currentTime;
    float fadetime = 1;

    Group grpEffectParams;
    Slider slFadetime;

    EdgeFadeEffect(PApplet parent_, HashMap<Integer, Edge> edges_) {
        parent = parent_;
        edges = edges_;
        fadeInterval = new HashMap<Integer, Integer>();
        for (Edge f : edges.values()) {
            fadeInterval.put(f.getId(), (int) (parent.random(360, 720)));
        }
    }

    public void addEffectControllersToGui(Gui gui) {
        grpEffectParams = gui.getCp5().addGroup("edgefade")
                .setColor(gui.getC());
        gui.getEffectAccordion().addItem(grpEffectParams);
        slFadetime = gui.getCp5().addSlider("edgeFadeFadeTime")
                .setCaptionLabel("fadetime").setPosition(10, 10)
                .setColor(gui.getC()).setRange(.001f, 50).setValue(fadetime)
                .moveTo(grpEffectParams);
        slFadetime.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    fadetime = slFadetime.getValue();
                }
            }
        });
        // gui.getRdbEffects().addItem("EdgeFadeEffect",
        // AbstractEffect.EDGE_FADE);
    }

    public void update() {
        currentTime = parent.frameCount;
    }

    public void display() {
        for (Edge f : edges.values()) {
            if (fadeInterval.get(f.getId()) == null) {
                fadeInterval.put(f.getId(), (int) (parent.random(360, 720)));
            }
            float progress = (currentTime * fadetime)
                    % fadeInterval.get(f.getId()) / fadeInterval.get(f.getId());
            float tint = PApplet
                    .map(PApplet.sin(PApplet.radians(progress * 360)), -1, 1,
                            0, 255);
            f.getShape().setStroke(parent.color(tint, tint, tint));
            parent.shapeMode(CORNERS);
            parent.shape(f.getShape());
        }
    }
}