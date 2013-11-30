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
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Toggle;
import de.hawilux.mapper.Mapper;
import de.hawilux.mapper.ui.Gui;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractEffect.
 */
public abstract class AbstractEffect {

    /** The parent. */
    protected PApplet parent;
    protected Group grpEffectParams;
    protected Gui gui;
    protected String name;
    private Toggle tglEnabled;

    public AbstractEffect(PApplet parent, String effectPrefix) {
        this.parent = parent;
        this.name = effectPrefix;
    }

    public void addControllersToGui(Gui gui) {
        this.gui = gui;
        grpEffectParams = gui.getCp5().addGroup(name).setColor(gui.getC())
                .hide();
        gui.getEffectAccordion().addItem(grpEffectParams);

        addEffectControllersToGui(gui);

        addEnableToggle(name);

    }

    /**
     * Adds the effect controllers to gui.
     * 
     * @param gui
     *            the gui
     */
    public abstract void addEffectControllersToGui(Gui gui);

    /**
     * Removes the effect controllers from gui.
     * 
     * @param gui
     *            the gui
     */
    public void removeEffectControllersFromGui(Gui gui) {
        gui.getEffectAccordion().removeItem(grpEffectParams);
    }

    public void disable() {
        gui.getEffectAccordion().removeItem(grpEffectParams);
        grpEffectParams.hide();
        if (tglEnabled.getState()) {
            tglEnabled.setState(false);
        }
    }

    public void enable() {
        gui.getEffectAccordion().addItem(grpEffectParams);
        grpEffectParams.show();
        if (!tglEnabled.getState()) {
            tglEnabled.setState(true);
        }
    }

    protected void addEnableToggle(String effectPrefix) {
        tglEnabled = gui.addEffectEnableToggle(name, new CallbackListener() {
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    float value = theEvent.getController().getValue();
                    if (value == 0) {
                        // disable();
                        try {
                            Mapper.getExistingInstance().disableEffect(name);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Mapper.getExistingInstance().enableEffect(name);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    /**
     * Update.
     */
    public abstract void update();

    /**
     * Display.
     */
    public abstract void display();

    public String getName() {
        return name;
    }
}
