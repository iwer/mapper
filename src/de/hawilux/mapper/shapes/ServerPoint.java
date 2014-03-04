package de.hawilux.mapper.shapes;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;
import de.hawilux.mapper.MapperServer;

public class ServerPoint extends Shape implements PConstants, IPoint {
    MapperServer server;

    /** The radius. */
    private int RADIUS = 15;

    /** The RADIUS to the power of 2. */
    protected int RADIUS2 = RADIUS * 2;

    /** The show helper. */
    private boolean showHelper;

    protected boolean updated;

    /** The connected edges. */
    private ArrayList<Integer> connectedEdges;

    /** The dot. */
    private PShape circle;

    public ServerPoint(PApplet parent_, MapperServer server_, int id_, int x_,
            int y_, boolean helper_) {
        super(parent_, id_);
        server = server_;
        centroid = new PVector(x_, y_);
        // selected = false;
        showHelper = helper_;
        connectedEdges = new ArrayList<Integer>();
        update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#isUpdated()
     */
    @Override
    public boolean isUpdated() {
        return updated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#setUpdated(boolean)
     */
    @Override
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    /**
     * Gets the color.
     * 
     * @param config
     *            the config
     * @param selected
     *            the selected
     * @param mouseOverColor
     *            the mouse over color
     * @return the color
     */
    int getColor(boolean config, boolean selected, boolean mouseOverColor) {
        int c = 0;
        if (!config) {
            c = parent.color(255, 0, 0);
        } else if (mouseOver(centroid) && mouseOverColor) {
            if (selected) {
                c = parent.color(0, 255, 255);
            } else {
                c = parent.color(255, 255, 0);
            }
        } else {
            if (selected) {
                c = parent.color(0, 255, 0);
            } else {
                c = parent.color(255, 0, 0);
            }
        }
        return c;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#display(boolean, boolean, boolean)
     */
    @Override
    public void display(boolean config, boolean selected, boolean mouseOverColor) {
        parent.pushMatrix();

        int c = getColor(config, selected, mouseOverColor);
        circle.setStroke(c);
        parent.shapeMode(CENTER);
        circle.resetMatrix();
        circle.translate(centroid.x, centroid.y);
        parent.shape(circle);
        shape.setStroke(c);
        shape.resetMatrix();
        shape.translate(centroid.x, centroid.y);
        parent.shape(shape);

        parent.popMatrix();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#displayHelper(boolean, boolean)
     */
    @Override
    public void displayHelper(boolean selected, boolean mouseOverColor) {
        int c = getColor(true, selected, mouseOverColor);
        parent.pushMatrix();
        parent.fill(c);
        parent.text(id, centroid.x - 20, centroid.y - 20);
        parent.popMatrix();
    }

    /**
     * Mouse over.
     * 
     * @param v
     *            the v
     * @return true, if successful
     */
    boolean mouseOver(PVector v) {
        PVector dist = new PVector(server.mouseX, server.mouseY);
        dist.sub(v);
        if (dist.magSq() > RADIUS2) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#select()
     */
    @Override
    public int select() {
        if (mouseOver(centroid)) {
            return id;
        } else {
            return -1;
        }
    }

    /**
     * Move.
     */
    public void move() {
        centroid.x = server.mouseX;
        centroid.y = server.mouseY;
        updated = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#move(int, int)
     */
    @Override
    public void move(int dx, int dy) {
        centroid.x += dx;
        centroid.y += dy;
        updated = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#update()
     */
    @Override
    public void update() {
        // circle = parent.createShape(ELLIPSE, centroid.x, centroid.y, RADIUS,
        // RADIUS);
        circle = parent.createShape(ELLIPSE, 0, 0, RADIUS, RADIUS);
        circle.setFill(false);
        circle.setStrokeWeight(3);
        shape = parent.createShape(ELLIPSE, 0, 0, 2, 2);
        shape.setFill(false);
        shape.setStrokeWeight(3);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#addConnectedEdge(int)
     */
    @Override
    public void addConnectedEdge(int id_) {
        connectedEdges.add(id_);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#removeConnectedEdge(int)
     */
    @Override
    public void removeConnectedEdge(int id_) {
        Integer toRemove = new Integer(id_);
        if (connectedEdges.contains(toRemove)) {
            connectedEdges.remove(toRemove);
        }
        // println("Connected Edges at point " + id + ":" + connectedEdges);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#getConnectedEdges()
     */
    @Override
    public ArrayList<Integer> getConnectedEdges() {
        return connectedEdges;
    }

    /**
     * Gets the location.
     * 
     * @return the location
     * @deprecated use getCentroid()
     */
    @Deprecated
    public PVector getLocation() {
        return centroid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#getX()
     */
    @Override
    public float getX() {
        return centroid.x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.shapes.IPoint#getY()
     */
    @Override
    public float getY() {
        return centroid.y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hawilux.mapper.shapes.IPoint#equals(de.hawilux.mapper.shapes.Point)
     */
    public boolean equals(Point other) {
        return (this.parent.equals(other.parent)
                && this.showHelper == other.showHelper && this.id == other.id && this.centroid
                    .equals(other.centroid));
    }

}