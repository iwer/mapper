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
package de.hawilux.mapper.gui;

import controlP5.Accordion;
import controlP5.CColor;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Println;
import controlP5.RadioButton;
import controlP5.Textarea;
import controlP5.Textlabel;
import processing.core.PApplet;

public class Gui {
    PApplet parent;
    ControlP5 cp5;

    CallbackListener cb;

    int cActive = parent.color(203, 70, 144);
    int cBackground = parent.color(112, 70, 203);
    int cCaptionLabel = parent.color(0, 0, 0);
    int cForeground = parent.color(156, 70, 203);
    int cValueLabel = parent.color(0, 0, 0);
    CColor c = new CColor();// (cForeground, cBackground, cActive,
                            // cCaptionLabel, cValueLabel);

    Println console;
    Textarea consoleTextArea;
    Textlabel fpsLabel;
    Accordion mainAccordion;
    Accordion effectAccordion;

    RadioButton rdbEffects;

    Group effectGroup;
    Group fileGroup;
    Group setupGroup;
    Group setupSelectModeGroup;
    Group setupEditGroup;

    String filename;

    Gui(PApplet parent_) {
        parent = parent_;
        cp5 = new ControlP5(parent);

        // frameRate at top
        cp5.addFrameRate().setInterval(10).setPosition(10, 10);

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
        rdbEffects = cp5.addRadioButton("chooseneffect").setPosition(10, 10)
                .setColor(c).setNoneSelectedAllowed(false).addItem("none", 1)
                .activate("none").moveTo(effectGroup);

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

    public ControlP5 getCp5() {
        return cp5;
    }

    public CColor getC() {
        return c;
    }

    public Group getSetupSelectModeGroup() {
        return setupSelectModeGroup;
    }

    public Group getSetupEditGroup() {
        return setupEditGroup;
    }
    
    public Group getFileGroup() {
        return fileGroup;
    }

    public Group getSetupGroup() {
        return setupGroup;
    }
    
    // change gui functions
    public void show(boolean state) {
        if (state == true) {
            cp5.show();
        } else {
            cp5.hide();
        }
    }

    void setConsoleEnabled(boolean state) {
        if (state == true) {
            consoleTextArea.show();
        } else {
            consoleTextArea.hide();
        }
    }

    void saveGuiProperties() {
        console.clear();
        System.out.flush();
        cp5.saveProperties(("gui.properties"));
    }

    void loadGuiProperties() {
        System.out.flush();
        cp5.loadProperties(("gui.properties"));
    }
}
