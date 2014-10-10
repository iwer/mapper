package de.hawilux.mapper.tools;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.HashMap;

public class DisplayConfiguration {

    private static HashMap<String, Display> displays;
    private static String primaryDisplayID;
    private static String secondaryDisplayID;

    public DisplayConfiguration() {
        displays = new HashMap<String, Display>();

        GraphicsEnvironment environment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice devices[] = environment.getScreenDevices();
        primaryDisplayID = environment.getDefaultScreenDevice().getIDstring();
        secondaryDisplayID = new String();

        for (int i = 0; i < devices.length; i++) {
            Rectangle disp = devices[i].getDefaultConfiguration().getBounds();
            displays.put(devices[i].getIDstring(), new Display(i, disp));
            if (!devices[i].getIDstring().equals(primaryDisplayID)
                    && secondaryDisplayID.equals("")) {
                secondaryDisplayID = devices[i].getIDstring();
            }
        }

        System.out.println("Found displays: " + displays);
        System.out.println("Primary: " + primaryDisplayID);
        System.out.println("Secondary: " + secondaryDisplayID);
    }

    public static Display getPrimaryDisplay() {
        return displays.get(primaryDisplayID);
    }

    public static Display getSecondaryDisplay() {
        return displays.get(secondaryDisplayID);
    }

    public class Display {
        int id;

        Rectangle disp;

        @Override
        public String toString() {
            return "Display [id=" + id + ", x=" + disp.x + ", y=" + disp.y
                    + ", width=" + disp.width + ", height=" + disp.height + "]";
        }

        public Display(int id, Rectangle disp) {
            this.id = id;
            this.disp = disp;
        }

        public int getId() {
            return id;
        }

        public int getX() {
            return disp.x;
        }

        public int getY() {
            return disp.y;
        }

        public int getWidth() {
            return disp.width;
        }

        public int getHeight() {
            return disp.height;
        }

        public double getCenterX() {
            return disp.getCenterX();
        }

        public double getCenterY() {
            return disp.getCenterY();
        }

    }

}
