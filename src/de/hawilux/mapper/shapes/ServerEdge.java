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
 * Edge class
 * 
 * represents a line between two Points
 *
 */
package de.hawilux.mapper.shapes;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;
import de.hawilux.mapper.MapperServer;

/**
 * The Class ServerEdge.
 */
public class ServerEdge extends Edge implements PConstants, IEdge {
	MapperServer server;

    public ServerEdge(PApplet parent_, PShape shapeGroup_, MapperServer server_, int id_,
            IPoint a_, IPoint b_, boolean helper_) {
        super(parent_, shapeGroup_,id_,a_,b_,helper_);
        server = server_;
    }

    /**
     * Mouse over.
     * 
     * @return true, if successful
     */
    protected boolean mouseOver() {
        PVector dist = new PVector();

        dist.x = centroid.x - server.mouseX;
        dist.y = centroid.y - server.mouseY;

        float len2 = dist.magSq();
        if (len2 < 100) {
            return true;
        } else {
            return false;
        }
    }
}