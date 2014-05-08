package de.hawilux.mapper.effects;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import controlP5.Group;
import de.hawilux.mapper.ui.Gui;

public abstract class AbstractActivationTexture implements PConstants {
    protected PApplet parent;
    protected PGraphics buffer;
    protected String name;

    public AbstractActivationTexture(PApplet parent, String name) {
        this.parent = parent;
        this.name = name;
        buffer = parent.createGraphics(parent.width, parent.height, JAVA2D);
    }

    public PGraphics getBuffer() {
        return buffer;
    }

    public abstract void addEffectControllersToGui(Gui gui,
            Group grpEffectParams);

    public abstract void update();

    public abstract void display();

    public String getName() {
        return name;
    }
}
