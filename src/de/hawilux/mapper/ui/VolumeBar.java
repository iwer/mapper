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

import controlP5.ControlP5;
import controlP5.Slider;
import ddf.minim.AudioInput;

/**
 * The Class VolumeBar.
 */
public class VolumeBar {

    /** The rmsbar. */
    Slider rmsbarSlider;
    AudioInput in;
    ControlP5 cp5;

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
        rmsbarSlider = cp5.addSlider("volume" + name).setCaptionLabel("Volume")
                .setPosition(posX, posY).setSize(sizeX, sizeY).setRange(0, 1)
                .setValue(0);
    }

    /**
     * Gets the rmsbar slider.
     * 
     * @return the rmsbar slider
     */
    public Slider getRmsbarSlider() {
        return rmsbarSlider;
    }

    /**
     * Update.
     */
    public void update() {
        rmsbarSlider.setValue(in.mix.level());
    }
}
