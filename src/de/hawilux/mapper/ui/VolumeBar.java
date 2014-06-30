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
 * VolumeBar class for the sound reactive effects
 *
 *
 */
package de.hawilux.mapper.ui;

import controlP5.Chart;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Slider;
import ddf.minim.AudioInput;

/**
 * The Class VolumeBar.
 */
public class VolumeBar {

    /** The rmsbar. */
    Slider     rmsbarSlider;
    Chart      rmsHistory;

    AudioInput in;
    ControlP5  cp5;

    /**
     * Instantiates a new volume bar.
     * 
     * @param cp5
     *            the cp5
     * @param in
     *            the in
     * @param posX
     *            the pos x
     * @param posY
     *            the pos y
     * @param sizeX
     *            the size x
     * @param sizeY
     *            the size y
     */
    public VolumeBar(ControlP5 cp5, AudioInput in, String name, int posX,
            int posY, int sizeX, int sizeY) {
        this.cp5 = cp5;
        this.in = in;

        rmsHistory = cp5.addChart("volhist" + name).setPosition(posX, posY)
                .setSize(sizeX, sizeY * 2 / 5).setRange(0, 1)
                .setView(Chart.BAR);
        rmsHistory.addDataSet("volume");
        rmsHistory.setData("volume", new float[64]);
        rmsHistory.setStrokeWeight(1.5f);
        rmsbarSlider = cp5.addSlider("volume" + name).setCaptionLabel("Volume")
                .setPosition(posX, posY + sizeY * 3 / 5)
                .setSize(sizeX, sizeY * 2 / 5).setRange(0, 1).setValue(0);
    }

    /**
     * Gets the rmsbar slider.
     * 
     * @return the rmsbar slider
     * @deprecated Use moveTogroup()
     */
    @Deprecated
    public Slider getRmsbarSlider() {
        return rmsbarSlider;
    }

    public void moveToGroup(Group group) {
        rmsHistory.moveTo(group);
        rmsbarSlider.moveTo(group);
    }

    /**
     * Update.
     */
    public void update() {
        float level = in.mix.level();
        rmsbarSlider.setValue(level);
        rmsHistory.push("volume", level);
    }
}
