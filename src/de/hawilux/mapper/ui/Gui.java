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
 * GUI class
 *
 * defines the User Interface
 *
 */
package de.hawilux.mapper.ui;

import processing.core.PApplet;
import controlP5.Accordion;
import controlP5.CColor;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Println;
import controlP5.RadioButton;
import controlP5.Textarea;
import controlP5.Textlabel;
import controlP5.Toggle;

// TODO: Auto-generated Javadoc
/**
 * The Class Gui.
 */
public class Gui {

    /** The parent. */
    PApplet parent;

    /** The cp5. */
    ControlP5 cp5;

    /** The cb. */
    CallbackListener cb;

    /** The c active. */
    int cActive;

    /** The c background. */
    int cBackground;

    /** The c caption label. */
    int cCaptionLabel;

    /** The c foreground. */
    int cForeground;

    /** The c value label. */
    int cValueLabel;

    /** The c. */
    CColor c;

    /** The console. */
    Println console;

    /** The console text area. */
    Textarea consoleTextArea;

    /** The fps label. */
    Textlabel fpsLabel;

    /** The main accordion. */
    Accordion mainAccordion;

    /** The effect accordion. */
    Accordion effectAccordion;

    /** The rdb effects. */
    RadioButton rdbEffects;

    /** The effect group. */
    Group effectGroup;

    /** The file group. */
    Group fileGroup;

    /** The setup group. */
    Group setupGroup;

    /** The setup select mode group. */
    Group setupSelectModeGroup;

    /** The setup edit group. */
    Group setupEditGroup;

    /** The filename. */
    String filename;

    private int nextEffectTogglePos = 10;

    /**
     * Instantiates a new gui.
     * 
     * @param parent_
     *            the parent_
     */
    public Gui(PApplet parent_) {
        this(parent_, new ControlP5(parent_));
    }

    /**
     * Instantiates a new gui.
     * 
     * @param parent_
     *            the parent_
     * @param cp5_
     *            the cp5_
     */
    public Gui(PApplet parent_, ControlP5 cp5_) {
        this.parent = parent_;

        cActive = parent.color(203, 70, 144);
        cBackground = parent.color(112, 70, 203);
        cCaptionLabel = parent.color(0, 0, 0);
        cForeground = parent.color(156, 70, 203);
        cValueLabel = parent.color(0, 0, 0);

        c = new CColor();// (cForeground, cBackground, cActive,
        // cCaptionLabel, cValueLabel);
        cp5 = cp5_;

        // frameRate at top
        cp5.addFrameRate().setInterval(10).setPosition(10, 10);
        cp5.addTextlabel("fps").setPosition(25, 10).setText("FPS");

        // file group
        fileGroup = cp5.addGroup("file").setColor(c);

        // setup group
        setupGroup = cp5.addGroup("setup").setColor(c).setBackgroundHeight(200);

        // setup -> selectmode
        setupSelectModeGroup = cp5.addGroup("selectmodegroup")
                .setLabel("selectmode").setPosition(0, 55).setColor(c)
                .disableCollapse().moveTo(setupGroup);

        // setup -> edit
        setupEditGroup = cp5.addGroup("editgroup").setLabel("edit")
                .setPosition(0, 100).setColor(c).disableCollapse()
                .moveTo(setupGroup);

        // effect group
        effectGroup = cp5.addGroup("effect").setColor(c);

        // accordion
        mainAccordion = cp5.addAccordion("acc").setPosition(10, 40)
                .setWidth(100).addItem(fileGroup).addItem(setupGroup)
                .addItem(effectGroup);
        mainAccordion.setCollapseMode(Accordion.SINGLE);

        effectAccordion = cp5.addAccordion("effAcc")
                .setPosition(parent.width - 210, 40).setWidth(100);
        mainAccordion.setCollapseMode(Accordion.SINGLE);

        // console
        consoleTextArea = cp5.addTextarea("console")
                .setPosition(10, parent.height - 230).setSize(500, 200)
                .setFont(parent.createFont("", 10)).setLineHeight(14)
                .setColor(c);
        ;

        console = cp5.addConsole(consoleTextArea);//
    }

    /**
     * Adds the effect enable toggle.
     * 
     * @param effectPrefix
     *            the effect prefix
     * @param callback
     *            the callback
     * @return the toggle
     */
    public Toggle addEffectEnableToggle(String effectPrefix,
            CallbackListener callback) {
        Toggle ret = cp5.addToggle(effectPrefix + "enable")
                .setCaptionLabel(effectPrefix).setSize(50, 10)
                .setPosition(10, nextEffectTogglePos).setValue(false)
                .moveTo(effectGroup).addCallback(callback);
        nextEffectTogglePos += 30;
        return ret;
    }

    public Group getEffectGroup() {
        return effectGroup;
    }

    /**
     * Gets the cp5.
     * 
     * @return the cp5
     */
    public ControlP5 getCp5() {
        return cp5;
    }

    /**
     * Gets the c.
     * 
     * @return the c
     */
    public CColor getC() {
        return c;
    }

    /**
     * Gets the setup select mode group.
     * 
     * @return the setup select mode group
     */
    public Group getSetupSelectModeGroup() {
        return setupSelectModeGroup;
    }

    /**
     * Gets the setup edit group.
     * 
     * @return the setup edit group
     */
    public Group getSetupEditGroup() {
        return setupEditGroup;
    }

    /**
     * Gets the file group.
     * 
     * @return the file group
     */
    public Group getFileGroup() {
        return fileGroup;
    }

    /**
     * Gets the setup group.
     * 
     * @return the setup group
     */
    public Group getSetupGroup() {
        return setupGroup;
    }

    /**
     * Gets the effect accordion.
     * 
     * @return the effect accordion
     */
    public Accordion getEffectAccordion() {
        return effectAccordion;
    }

    /**
     * Gets the rdb effects.
     * 
     * @return the rdb effects
     */
    public RadioButton getRdbEffects() {
        return rdbEffects;
    }

    // change gui functions
    /**
     * Show.
     * 
     * @param state
     *            the state
     */
    public void show(boolean state) {
        if (state == true) {
            cp5.show();
        } else {
            cp5.hide();
        }
    }

    /**
     * Sets the console enabled.
     * 
     * @param state
     *            the new console enabled
     */
    public void setConsoleEnabled(boolean state) {
        if (state == true) {
            consoleTextArea.show();
        } else {
            consoleTextArea.hide();
        }
    }

    /**
     * Save gui properties.
     */
    public void saveGuiProperties() {
        console.clear();
        System.out.flush();
        cp5.saveProperties(("gui.properties"));
    }

    /**
     * Load gui properties.
     */
    public void loadGuiProperties() {
        System.out.flush();
        cp5.loadProperties(("gui.properties"));
    }
}
