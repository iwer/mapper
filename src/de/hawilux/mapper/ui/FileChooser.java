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
 * FileChooser GUI class for cp5
 *
 * Inspired by:
 * http://forum.processing.org/one/topic/controlp5-pop-up-dialogs.html
 *
 */
package de.hawilux.mapper.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.Group;
import controlP5.ListBox;
import controlP5.ListBoxItem;
import controlP5.Textfield;

// TODO: Auto-generated Javadoc
/**
 * The Class FileChooser.
 */
public class FileChooser {

    /** The parent. */
    PApplet parent;

    /** The gui. */
    Gui gui;

    /** The runs. */
    final ArrayList<Runnable> runs = new ArrayList<Runnable>();

    /** The enabled. */
    boolean enabled;

    /** The filename. */
    String filename;

    /** The action button. */
    Button actionButton;

    /**
     * Instantiates a new file chooser.
     * 
     * @param parent_
     *            the parent_
     */
    public FileChooser(PApplet parent_) {
        parent = parent_;
        filename = new String();
        enabled = false;
    }

    /**
     * Gets the filename.
     * 
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Gets the action button.
     * 
     * @return the action button
     */
    public Button getActionButton() {
        return actionButton;
    }

    /**
     * Checks if is enabled.
     * 
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the gui.
     * 
     * @param gui_
     *            the new gui
     */
    public void setGui(Gui gui_) {
        gui = gui_;
    }

    /**
     * Adds the file chooser.
     * 
     * @param action
     *            the action
     */
    public void addFileChooser(String action) {
        enabled = true;
        final Group fc = gui.cp5.addGroup("filechooser").setPosition(10, 250)
                .setSize(300, 300).setColor(gui.c).hideBar();

        final Textfield tf = gui.cp5.addTextfield("filename")
                .setPosition(8, 210).setSize(284, 20).setGroup("filechooser")
                .setColor(gui.c);

        tf.getCaptionLabel().hide();

        final ListBox lb = gui.cp5.addListBox("selectfiles").setSize(300, 200)
                .setPosition(0, 0).setColor(gui.c).hideBar()
                .setStringValue(new File(parent.dataPath("")).toString())
                .setGroup("filechooser")
                // what is this? see bottom of the sketch
                .addListener(new ControlListener() {
                    public void controlEvent(final ControlEvent ev) {
                        runs.add(new Runnable() {
                            public void run() {
                                updateFileChooser(((ListBox) ev.getGroup()),
                                        tf, (int) ev.getValue());
                            }
                        });
                    }
                });

        actionButton = gui.cp5.addButton(action).setPosition(7, 240)
                .setSize(120, 20).setGroup("filechooser").setColor(gui.c)
                .addListener(new ControlListener() {
                    public void controlEvent(ControlEvent ev) {
                        filename = new File(new File(lb.getStringValue()), tf
                                .getText()).toString();
                        runs.add(new Runnable() {
                            public void run() {
                                fc.remove();
                                enabled = false;
                            }
                        });
                    }
                });

        gui.cp5.addButton("cancel").setPosition(173, 240).setSize(120, 20)
                .setGroup("filechooser").setColor(gui.c)
                .addListener(new ControlListener() {
                    // when cancel is triggered, add a new runnable to
                    // safely remove the filechooser from controlP5 in
                    // a post event.
                    public void controlEvent(ControlEvent ev) {
                        runs.add(new Runnable() {
                            public void run() {
                                fc.remove();
                                enabled = false;
                            }
                        });
                    }
                });
        updateFileChooser(lb, tf, 0);
    }

    /**
     * Adds the remote file chooser.
     * 
     * @param action
     *            the action
     */
    public void addRemoteFileChooser(String action) {
        enabled = true;
        final Group fc = gui.cp5.addGroup("filechooser").setPosition(10, 250)
                .setSize(300, 300).setColor(gui.c).hideBar();

        final Textfield tf = gui.cp5.addTextfield("filename")
                .setPosition(8, 210).setSize(284, 20).setGroup("filechooser")
                .setColor(gui.c);

        tf.getCaptionLabel().hide();

        final ListBox lb = gui.cp5.addListBox("selectfiles").setSize(300, 200)
                .setPosition(0, 0).setColor(gui.c).hideBar()
                // .setStringValue(new File(parent.dataPath("")).toString())
                .setStringValue("Bla").setGroup("filechooser")
                // what is this? see bottom of the sketch
                .addListener(new ControlListener() {
                    public void controlEvent(final ControlEvent ev) {
                        runs.add(new Runnable() {
                            public void run() {
                                updateFileChooser(((ListBox) ev.getGroup()),
                                        tf, (int) ev.getValue());
                            }
                        });
                    }
                });

        actionButton = gui.cp5.addButton(action).setPosition(7, 240)
                .setSize(120, 20).setGroup("filechooser").setColor(gui.c)
                .addListener(new ControlListener() {
                    public void controlEvent(ControlEvent ev) {
                        filename = "new_file.xml";
                        // new File(new File(lb.getStringValue()), tf
                        // .getText()).toString();
                        runs.add(new Runnable() {
                            public void run() {
                                fc.remove();
                                enabled = false;
                            }
                        });
                    }
                });

        gui.cp5.addButton("cancel").setPosition(173, 240).setSize(120, 20)
                .setGroup("filechooser").setColor(gui.c)
                .addListener(new ControlListener() {
                    // when cancel is triggered, add a new runnable to
                    // safely remove the filechooser from controlP5 in
                    // a post event.
                    public void controlEvent(ControlEvent ev) {
                        runs.add(new Runnable() {
                            public void run() {
                                fc.remove();
                                enabled = false;
                            }
                        });
                    }
                });
        updateFileChooser(lb, tf, 0);
    }

    /**
     * Update file chooser.
     * 
     * @param lb
     *            the lb
     * @param tf
     *            the tf
     * @param theValue
     *            the the value
     */
    final void updateFileChooser(ListBox lb, Textfield tf, int theValue) {

        String s = (lb.getListBoxItems().length == 0) ? "" : lb
                .getListBoxItems()[theValue][0];

        File f;

        if (s.equals("..")) {
            f = new File(lb.getStringValue()).getParentFile();
        } else {
            f = new File(new File(lb.getStringValue()), s);
        }

        if (f != null) {
            if (f.isDirectory()) {
                String[] strs = f.list();
                lb.clear();
                lb.setColor(gui.c);
                int n = 0;
                lb.addItem(f.getName(), n++).setColorBackground(
                        parent.color(80));
                lb.addItem("..", n++);
                for (String s1 : strs) {
                    ListBoxItem item = lb.addItem(s1, n++);
                    if (new File(f, s1).isDirectory()) {
                        item.setColorBackground(parent.color(60, 90, 100));
                    }
                }
                lb.scroll(0);
                lb.setStringValue(f.getAbsolutePath().toString());
            } else if (theValue != 0) {
                PApplet.println("file selected : " + f.getAbsolutePath());
                tf.setText(f.getName());
            }
        }
    }

    // for some controlP5 events we need to handle events after the main draw
    // and
    // input events have been executed and completed. For this we use a list to
    // store objects of type runnable. Runnable is an interface that implements
    // 1 method - run() - and this suits us well here since we only need to
    // execute an event once by calling run and then remove it.
    // For example, whenever an event of a filechooser's listbox has been
    // triggered, an
    // anonymous class oftype Runnable will be added to List 'runs', executed
    // once in post() and is then removed.

    /**
     * Post.
     */
    public void post() {
        Iterator<Runnable> it = runs.iterator();
        while (it.hasNext()) {
            it.next().run();
            it.remove();
        }
    }
}