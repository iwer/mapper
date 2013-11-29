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
 * EffectManager.java class
 *
 */
package de.hawilux.mapper;

import java.util.HashMap;

import processing.core.PApplet;
import de.hawilux.mapper.effects.AbstractEffect;

// TODO: Auto-generated Javadoc
/**
 * The Class EffectManager.
 */
public class EffectManager {

    /** The mapper. */
    Mapper mapper;

    /** The effects available. */
    HashMap<String, AbstractEffect> effectsAvailable;

    /** The effects enabled. */
    HashMap<String, AbstractEffect> effectsEnabled;

    /**
     * Instantiates a new effect manager.
     * 
     * @param mapper
     *            the mapper
     */
    public EffectManager(Mapper mapper) {
        this.mapper = mapper;
        this.effectsAvailable = new HashMap<String, AbstractEffect>();
        this.effectsEnabled = new HashMap<String, AbstractEffect>();
    }

    /**
     * Register effect.
     * 
     * @param effectName
     *            the effect name
     * @param effect
     *            the effect
     */
    public void registerEffect(String effectName, AbstractEffect effect) {
        PApplet.println("Registering effect - " + effectName);
        effectsAvailable.put(effectName, effect);
    }

    /**
     * Enable effect.
     * 
     * @param effectName
     *            the effect name
     */
    public void enableEffect(String effectName) {
        AbstractEffect effect = effectsAvailable.get(effectName);
        if (effect != null) {
            PApplet.println("Enabling effect - " + effectName);
            effectsEnabled.put(effectName, effect);
            mapper.addEffectControllers(effect);
        }
    }

    /**
     * Disable effect.
     * 
     * @param effectName
     *            the effect name
     */
    public void disableEffect(String effectName) {
        PApplet.println("Disabling effect - " + effectName);
        effectsEnabled.remove(effectName);
    }

    /**
     * Gets the effects available.
     * 
     * @return the effects available
     */
    public String[] getEffectsAvailable() {
        return (String[]) effectsAvailable.keySet().toArray();
    }

    /**
     * Gets the effects enabled.
     * 
     * @return the effects enabled
     */
    public String[] getEffectsEnabled() {
        return (String[]) effectsEnabled.keySet().toArray();
    }
}
