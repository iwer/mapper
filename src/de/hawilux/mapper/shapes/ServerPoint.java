package de.hawilux.mapper.shapes;

import processing.core.PApplet;
import processing.core.PVector;
import de.hawilux.mapper.MapperServer;

public class ServerPoint extends Point implements IPoint {
	MapperServer server;
	public ServerPoint(PApplet parent_, MapperServer server_, int id_, int x_,
			int y_, boolean helper_) {
		super(parent_, id_, x_, y_, helper_);
		server = server_;
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

    /**
     * Move.
     */
    public void move() {
        centroid.x = server.mouseX;
        centroid.y = server.mouseY;
        updated = true;
    }

}