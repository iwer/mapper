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
 * Cursor class
 * Big Cursor to help finding your mouse in the room while mapping
 *
 */
package de.hawilux.mapper.ui;

import processing.core.PApplet;
import processing.core.PGraphics;

// TODO: Auto-generated Javadoc
/**
 * The Class Cursor.
 */
public class Cursor {

    /** The parent. */
    private PApplet parent;

    /** The line width. */
    private int     lineWidth  = 1;

    /** The circle size. */
    private int     circleSize = 50;

    /**
     * Instantiates a new cursor.
     * 
     * @param parent_
     *            the parent_
     */
    public Cursor(PApplet parent_) {
        parent = parent_;
    }

    /**
     * Display.
     */
    public void display(PGraphics layer) {
        layer.stroke(255, 255, 0);
        layer.noFill();
        layer.strokeWeight(lineWidth);
        // x-line
        layer.line(0, MapperControlFrame.projectorMouseY, layer.width,
                MapperControlFrame.projectorMouseY);
        // y-line
        layer.line(MapperControlFrame.projectorMouseX, 0,
                MapperControlFrame.projectorMouseX, layer.height);
        // circle
        layer.ellipse(MapperControlFrame.projectorMouseX,
                MapperControlFrame.projectorMouseY, circleSize, circleSize);
    }
}