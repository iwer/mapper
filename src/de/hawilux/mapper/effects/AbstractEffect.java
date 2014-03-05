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

    /** The grp effect params. */
    protected Group   grpEffectParams;

    /** The gui. */
    protected Gui     gui;

    /** The name. */
    protected String  name;

    /** The tgl enabled. */
    private Toggle    tglEnabled;

    /**
     * Instantiates a new abstract effect.
     * 
     * @param parent
     *            the parent
     * @param effectPrefix
     *            the effect name
     */
    public AbstractEffect(PApplet parent, String effectName) {
        this.parent = parent;
        this.name = effectName;
        this.gui = null;
    }

    /**
     * Adds the controllers to gui.
     * 
     * calls the addEffectControllersToGui method where custom gui elements can
     * be added.
     * 
     * @param gui
     *            the gui
     */
    public void addControllersToGui(Gui gui_) {
        this.gui = gui_;
        grpEffectParams = gui.getCp5().addGroup(name).setColor(gui.getC()).hide();
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
    public abstract void addEffectControllersToGui(Gui gui_);

    /**
     * Adds the enable toggle.
     * 
     * @param effectPrefix
     *            the effect prefix
     */
    protected void addEnableToggle(String effectPrefix) {
        if (gui != null) {
            tglEnabled = gui.addEffectToggle(name, new CallbackListener() {
                public void controlEvent(CallbackEvent theEvent) {
                    if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                        float value = theEvent.getController().getValue();
                        if (value == 0) {
                            try {
                                Mapper.getExistingInstance().disableEffect(name);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
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

    }

    /**
     * Disables the effect.
     */
    public void disable() {
        if (gui != null) {
            gui.getEffectAccordion().removeItem(grpEffectParams);
            grpEffectParams.hide();
            if (tglEnabled.getState()) {
                tglEnabled.setState(false);
            }
        }
    }

    /**
     * Displays the effect.
     */
    public abstract void display();

    /**
     * Enables the effect.
     */
    public void enable() {
        if (gui != null) {
            gui.getEffectAccordion().addItem(grpEffectParams);
            grpEffectParams.show();
            if (!tglEnabled.getState()) {
                tglEnabled.setState(true);
            }
        }
    }

    /**
     * Gets the effects name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Removes the effect controllers from gui.
     * 
     * @param gui
     *            the gui
     */
    @Deprecated
    public void removeEffectControllersFromGui(Gui gui_) {
        if (gui_ != null) {
            gui_.getEffectAccordion().removeItem(grpEffectParams);
        }
    }

    /**
     * Update.
     */
    public abstract void update();
}
