package de.hawilux.mapper.shapes;

import java.util.ArrayList;
import java.util.HashSet;

import processing.core.PShape;
import processing.core.PVector;

public interface IFace {
	/**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId();
    public PVector getCentroid();
	/**
	 * Gets the connected edges.
	 * 
	 * @return the connected edges
	 */
	public abstract ArrayList<IEdge> getConnectedEdges();

	/**
	 * Gets the shape.
	 * 
	 * @return the shape
	 */
	public abstract PShape getShape();

	/**
	 * Gets the points.
	 * 
	 * @return the points
	 */
	public abstract HashSet<IPoint> getPoints();

	/**
	 * Gets the vertices.
	 * 
	 * @return the vertices
	 */
	public abstract ArrayList<PVector> getVertices();

	/**
	 * Adds the edge.
	 * 
	 * @param e_
	 *            the e_
	 */
	public abstract void addEdge(IEdge e_);

	/**
	 * Prepare delete.
	 */
	public abstract void prepareDelete();

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
	 * Update.
	 */
	public abstract void update();

	/**
	 * Select.
	 * 
	 * @return the int
	 */
	public abstract int select();

}