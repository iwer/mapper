package de.hawilux.mapper.tools;

import processing.core.PApplet;
import controlP5.Button;
import controlP5.CColor;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ColorPicker;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Slider;
import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.ui.GuiElement;

public class ColorManager implements GuiElement {

    PApplet     parent;
    int[]       colors;
    float       variationFactor;

    ColorPicker colorPicker;
    Button      color1, color2, color3, color4;
    Slider      variationFactorSlider;

    Group       grpEffectParams;

    CColor      white;

    public ColorManager(PApplet parent_/* , int colorCount */) {
        this.parent = parent_;
        int w = parent.color(255);
        white = new CColor(w, w, w, colorComplement(w), w);
        colors = new int[4];
    }

    @Override
    public void addControllersToGui(Gui gui_) {
        grpEffectParams = gui_.getCp5().addGroup("ColorManager")
                .setBackgroundHeight(105).setColor(gui_.getC());
        gui_.getEffectAccordion().addItem(grpEffectParams);
        colorPicker = gui_.getCp5()
                .addColorPicker("ColorPicker", 10, 10, 100, 10)
                .setColorValue(parent.color(127, 127, 127, 255))
                .moveTo(grpEffectParams);
        color1 = gui_.getCp5().addButton("Color1").setPosition(10, 75)
                .setSize(40, 10).setColor(white).moveTo(grpEffectParams)
                .addCallback(new CallbackListener() {

                    @Override
                    public void controlEvent(CallbackEvent theEvent) {
                        if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                            int c = colorPicker.getColorValue();
                            colors[0] = c;
                            color1.setColor(new CColor(c, c, c,
                                    colorComplement(c), c));
                        }
                    }
                });
        color2 = gui_.getCp5().addButton("Color2").setPosition(70, 75)
                .setSize(40, 10).setColor(white).moveTo(grpEffectParams)
                .addCallback(new CallbackListener() {

                    @Override
                    public void controlEvent(CallbackEvent theEvent) {
                        if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                            int c = colorPicker.getColorValue();
                            colors[1] = c;
                            color2.setColor(new CColor(c, c, c,
                                    colorComplement(c), c));
                        }
                    }
                });
        color3 = gui_.getCp5().addButton("Color3").setPosition(10, 90)
                .setSize(40, 10).setColor(white).moveTo(grpEffectParams)
                .addCallback(new CallbackListener() {

                    @Override
                    public void controlEvent(CallbackEvent theEvent) {
                        if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                            int c = colorPicker.getColorValue();
                            colors[2] = c;
                            color3.setColor(new CColor(c, c, c,
                                    colorComplement(c), c));
                        }
                    }
                });
        color4 = gui_.getCp5().addButton("Color4").setPosition(70, 90)
                .setSize(40, 10).setColor(white).moveTo(grpEffectParams)
                .addCallback(new CallbackListener() {

                    @Override
                    public void controlEvent(CallbackEvent theEvent) {
                        if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                            int c = colorPicker.getColorValue();
                            colors[3] = c;
                            color4.setColor(new CColor(c, c, c,
                                    colorComplement(c), c));
                        }
                    }
                });
    }

//    private int getColor(int colorIndex) {
//        if (colorIndex >= 0 && colorIndex < 4) {
//            return colors[colorIndex];
//        } else {
//            return parent.color(255);
//        }
//    }
    
    public int getColor(float activity) {
        int ret;
        if (activity < .2) {
            ret = parent.lerpColor(colors[0], colors[1], activity * 5);
        } else if (activity < .4) {
            ret = parent.lerpColor(colors[1], colors[2], (activity - .2f) * 5);
        } else if (activity < .7) {
            ret = parent.lerpColor(colors[2], colors[3], (activity - .4f) * 10 / 3);
        } else {
            ret = parent.lerpColor(colors[3], parent.color(255),
                    (activity - .7f) * 10 / 3);
        }
        return ret;
    }

    private int colorComplement(int color) {
        float R = parent.red(color);
        float G = parent.green(color);
        float B = parent.blue(color);
        float minRGB = PApplet.min(R, PApplet.min(G, B));
        float maxRGB = PApplet.max(R, PApplet.max(G, B));
        float minPlusMax = minRGB + maxRGB;
        int complement = parent.color(minPlusMax - R, minPlusMax - G,
                minPlusMax - B);
        return complement;
    }
}
