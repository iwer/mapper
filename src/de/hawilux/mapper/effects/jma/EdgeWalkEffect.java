/*
 *   A 2D Video Mapping Tool created from experiences in the HAWilux project
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this program; if not, write to the Free Software Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *
 * EdgeWalkEffect class
 *
 * a edge Walker effect. Simply lines that walk along all Edges 
 *
 */
package de.hawilux.mapper.effects.jma;

import java.util.HashMap;

import processing.core.PApplet;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Slider;
import de.hawilux.mapper.effects.AbstractEffect;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.ui.Gui;

public class EdgeWalkEffect extends AbstractEffect {
    float start;
    float end;
    protected float time = 1.f;
    HashMap<Integer, Edge> edges;

    Slider timeSlider;

    public EdgeWalkEffect(PApplet parent_, HashMap<Integer, Edge> edges_) {
        super(parent_, "edgewalk");
        edges = edges_;
    }

    public void addEffectControllersToGui(Gui gui) {
        timeSlider = gui.getCp5().addSlider("effectTime").setPosition(10, 10)
                .setColor(gui.getC()).setRange(1, 10).setNumberOfTickMarks(10)
                .moveTo(grpEffectParams);
        timeSlider.addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    time = timeSlider.getValue();
                }
            }
        });
    }

    public void setTime(float time_) {
        time = time_;
    }

    public void update() {
        float timepos = (parent.frameCount % (100 * time)) * .02f * 1.f / time;
        start = PApplet.min(1.f, timepos);
        end = PApplet.max(0.f, timepos - 1);
    }

    public void display() {
        parent.stroke(parent.color(255, 255, 255));
        parent.strokeWeight(2);

        for (Edge e : edges.values()) {
            float x1 = PApplet.lerp(e.getA().getX(), e.getB().getX(), start);
            float y1 = PApplet.lerp(e.getA().getY(), e.getB().getY(), start);
            float x2 = PApplet.lerp(e.getA().getX(), e.getB().getX(), end);
            float y2 = PApplet.lerp(e.getA().getY(), e.getB().getY(), end);
            parent.line(x1, y1, x2, y2);
        }
    }
}
