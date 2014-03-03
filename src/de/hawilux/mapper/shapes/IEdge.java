package de.hawilux.mapper.shapes;

import java.util.ArrayList;

import processing.core.PShape;
import processing.core.PVector;

public interface IEdge {
	/**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId();
    public PVector getCentroid();
	/**
	 * Gets the a.
	 * 
	 * @return the a
	 */
	public abstract IPoint getA();

	/**
	 * Gets the b.
	 * 
	 * @return the b
	 */
	public abstract IPoint getB();

	/**
	 * Gets the connected faces.
	 * 
	 * @return the connected faces
	 */
	public abstract ArrayList<Integer> getConnectedFaces();

	/**
	 * Prepare delete.
	 */
	public abstract void prepareDelete();

	/**
	 * Adds the connected face.
	 * 
	 * @param id_
	 *            the id_
	 */
	public abstract void addConnectedFace(int id_);

	/**
	 * Removes the connected face.
	 * 
	 * @param id_
	 *            the id_
	 */
	public abstract void removeConnectedFace(int id_);

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
	 * Update.
	 */
	public abstract void update();

	/**
	 * Select.
	 * 
	 * @return the int
	 */
	public abstract int select();
	public abstract PShape getShape();

}