package de.hawilux.mapper.ui;

public interface GuiElement {

    /**
     * Adds the controllers to gui.
     * 
     * calls the addEffectControllersToGui method where custom gui elements can
     * be added.
     * 
     * @param gui
     *            the gui
     */
    public abstract void addControllersToGui(Gui gui_);

}