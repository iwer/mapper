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

public class Cursor {
    private PApplet parent;
    private int lineWidth = 1;
    private int circleSize = 50;

    public Cursor(PApplet parent_) {
        parent = parent_;
    }

    public void display() {
        parent.stroke(255, 255, 0);
        parent.noFill();
        parent.strokeWeight(lineWidth);
        // x-line
        parent.line(0, parent.mouseY, parent.width, parent.mouseY);
        // y-line
        parent.line(parent.mouseX, 0, parent.mouseX, parent.height);
        // circle
        parent.ellipse(parent.mouseX, parent.mouseY, circleSize, circleSize);
    }
}