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
 * Abstract Effect class
 *
 * represents the basic Effect Interface
 *
 */
package de.hawilux.mapper.effects;

import processing.core.PApplet;
import de.hawilux.mapper.ui.Gui;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractEffect.
 */
public abstract class AbstractEffect {

    /** The parent. */
    protected PApplet parent;

    /**
     * Adds the effect controllers to gui.
     * 
     * @param gui
     *            the gui
     */
    public abstract void addEffectControllersToGui(Gui gui);

    /**
     * TODO: Removes the effect controllers from gui.
     * 
     * @param gui
     *            the gui
     */
    // public abstract void removeEffectControllersFromGui(Gui gui);

    /**
     * Update.
     */
    public abstract void update();

    /**
     * Display.
     */
    public abstract void display();
}
