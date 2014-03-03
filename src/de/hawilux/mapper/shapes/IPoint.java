package de.hawilux.mapper.shapes;

import java.util.ArrayList;

import processing.core.PVector;

public interface IPoint {

	/**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId();
    public PVector getCentroid();
	public abstract boolean isUpdated();

	public abstract void setUpdated(boolean updated);

	/**
	 * Display.
	 * 
	 * @param config
	 *            the config
	 * @param selected
	 *            the selected
	 * @param mouseOverColor
	 *            the mouse over color
	 */
	public abstract void display(boolean config, boolean selected,
			boolean mouseOverColor);

	/**
	 * Display helper.
	 * 
	 * @param selected
	 *            the selected
	 * @param mouseOverColor
	 *            the mouse over color
	 */
	public abstract void displayHelper(boolean selected, boolean mouseOverColor);

	/**
	 * Select.
	 * 
	 * @return the int
	 */
	public abstract int select();

	/**
	 * Move.
	 */
	public abstract void move();

	/**
	 * Move.
	 * 
	 * @param dx
	 *            the dx
	 * @param dy
	 *            the dy
	 */
	public abstract void move(int dx, int dy);

	/**
	 * Update.
	 */
	public abstract void update();

	/**
	 * Adds the connected edge.
	 * 
	 * @param id_
	 *            the id_
	 */
	public abstract void addConnectedEdge(int id_);

	/**
	 * Removes the connected edge.
	 * 
	 * @param id_
	 *            the id_
	 */
	public abstract void removeConnectedEdge(int id_);

	/**
	 * Gets the connected edges.
	 * 
	 * @return the connected edges
	 */
	public abstract ArrayList<Integer> getConnectedEdges();

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public abstract float getX();

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	public abstract float getY();

}