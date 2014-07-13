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
 * MapperMK2 is a 2D Line and Polygon Mapping tool
 * 
 * Created within the interdisciplinary university project HAWilux
 *
 */
import ddf.minim.*;
import de.hawilux.mapper.*;
import de.hawilux.mapper.shapes.Face;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.ui.Gui;
import controlP5.*;
import de.hawilux.mapper.effects.AbstractEffect;

public AbstractEffect currentEffect;

Minim minim;
AudioInput in;

Mapper mapper;
ControlP5 cp5;

void setup() {
  size(displayWidth, displayHeight, P2D);

  cp5 = new ControlP5(this);
  mapper = new Mapper(this, cp5);

  minim = new Minim(this);
  in = minim.getLineIn();

  currentEffect = new NDWEffectComposition(this, mapper.getEdges(), mapper.getFaces(), in);
  mapper.addEffectControllers(currentEffect);
} //<>//

void draw() {

    background(0);
    currentEffect.update();
    currentEffect.display();


}

void keyPressed() {

}

void mousePressed() {

}

void mouseDragged() {

}

//public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
//  super.mouseWheelMoved(e);
//  gui.getCp5().setMouseWheelRotation(e.getWheelRotation());
//}
