package de.hawilux.mapper.ui;

import controlP5.Accordion;
import controlP5.CColor;
import controlP5.ControlP5;

public interface IGui {

    /**
     * Gets the cp5.
     * 
     * @return the cp5
     */
    public abstract ControlP5 getCp5();

    /**
     * Gets the c.
     * 
     * @return the c
     */
    public abstract CColor getC();

    public abstract Accordion getEffectAccordion();

}