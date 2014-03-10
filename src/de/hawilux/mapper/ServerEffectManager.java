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
import java.util.Set;

import processing.core.PApplet;
import de.hawilux.mapper.effects.AbstractEffect;
import de.hawilux.mapper.net.OscMessagePaths;

// TODO: Auto-generated Javadoc
/**
 * The Class EffectManager.
 */
class ServerEffectManager {

	/** The mapper. */
	MapperServer mapper;

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
	public ServerEffectManager(MapperServer mapper) {
		this.mapper = mapper;
		this.effectsAvailable = new HashMap<String, AbstractEffect>();
		this.effectsEnabled = new HashMap<String, AbstractEffect>();
	}

	/**
	 * Register effect.
	 * 
	 * Registers the effect as available and creates visible and hidden gui
	 * elements that appear when effect is activated.
	 * 
	 * @param effect
	 *            the effect
	 */
	public void registerEffect(AbstractEffect effect) {
		PApplet.println("Registering effect - " + effect.getName() + "'"
				+ effect.getClass().getName() + "'");
		effectsAvailable.put(effect.getName(), effect);
		OscMessagePaths.addEffectPath(effect.getName());
	}

	/**
	 * Enable effect.
	 * 
	 * Adds the effect to the enabled effects and unhides the effect specific
	 * gui elements.
	 * 
	 * @param effectName
	 *            the effect name
	 */
	public void enableEffect(String effectName) {
		AbstractEffect effect = effectsAvailable.get(effectName);
		if (effect != null) {
			PApplet.println("Enabling effect - " + effectName);
			effectsEnabled.put(effectName, effect);
			effect.enable();
		}
	}

	/**
	 * Disable effect.
	 * 
	 * Removes effect from enabled effects, and hides effect specific gui
	 * elements.
	 * 
	 * @param effectName
	 *            the effect name
	 */
	public void disableEffect(String effectName) {
		AbstractEffect effect = effectsEnabled.get(effectName);
		if (effect != null) {
			PApplet.println("Disabling effect - " + effectName);
			effectsEnabled.remove(effectName);
			effect.disable();
		}
	}

	/**
	 * Gets the effects available.
	 * 
	 * @return the effects available
	 */
	public Set<String> getEffectsAvailable() {
		return effectsAvailable.keySet();
	}

	/**
	 * Gets the effects enabled.
	 * 
	 * @return the effects enabled
	 */
	public Set<String> getEffectsEnabled() {
		return effectsEnabled.keySet();
	}
}
