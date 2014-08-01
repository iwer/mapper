package de.hawilux.mapper.tools;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Label;
import controlP5.Slider;
import controlP5.Textlabel;
import processing.core.PApplet;
import processing.core.PImage;
import ddf.minim.AudioInput;
import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.ui.GuiElement;

public class ActivityTracker implements GuiElement {
    PApplet            parent;
    MovementQuantifier mq;
    AudioInput         in;

    float[]            volumeBuf, movementBuf;
    int                bufPos;

    private final int  maxBufLen      = 100;

    int                bufLen         = 10;
    int                volumeFaktor   = 500;
    int                movementFaktor = 5000;
    float              mixValue       = 0.5f;

    Group              grpEffectParams;

    Slider             bufLenSlider;
    Slider             mixValueSlider;
    Textlabel          mixLabel;

    Slider             movementDisplay;
    Slider             volumeDisplay;
    Slider             activityDisplay;

    private float      normMov;
    private float      normVol;
    private float      activity;

    public ActivityTracker(PApplet parent, AudioInput in) {
        this.parent = parent;
        this.in = in;
        this.mq = new MovementQuantifier(parent);

        volumeBuf = new float[maxBufLen];
        movementBuf = new float[maxBufLen];
    }

    @Override
    public void addControllersToGui(Gui gui_) {
        grpEffectParams = gui_.getCp5().addGroup("ActvityTracker")
                .setBackgroundHeight(105).setColor(gui_.getC());
        gui_.getEffectAccordion().addItem(grpEffectParams);

        movementDisplay = gui_.getCp5().addSlider("MovementActivity")
                .setPosition(0, 10).setColor(gui_.getC()).setRange(0, 1)
                .setValue(normMov).moveTo(grpEffectParams);

        volumeDisplay = gui_.getCp5().addSlider("VolumeActivity").setPosition(0, 35)
                .setColor(gui_.getC()).setRange(0, 1).setValue(normVol)
                .moveTo(grpEffectParams);

        activityDisplay = gui_.getCp5().addSlider("MixedActivity").setPosition(0, 60)
                .setColor(gui_.getC()).setRange(0, 1).setValue(activity)
                .moveTo(grpEffectParams);

        mixValueSlider = gui_.getCp5().addSlider("MixValue").setPosition(0, 85)
                .setColor(gui_.getC()).setRange(0, 1).setValue(mixValue)
                .moveTo(grpEffectParams);
        mixValueSlider.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    mixValue = mixValueSlider.getValue();
                }
            }
        });

        mixLabel = gui_.getCp5().addTextlabel("mixlabel")
                .setText("Volume   <->   Movement").setPosition(0, 110)
                .setColor(gui_.getC()).moveTo(grpEffectParams);

        bufLenSlider = gui_.getCp5().addSlider("Slowness").setPosition(0, 135)
                .setColor(gui_.getC()).setRange(1, 100).setValue(bufLen)
                .moveTo(grpEffectParams);
        bufLenSlider.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    bufLen = (int) bufLenSlider.getValue();
                }
            }
        });
        grpEffectParams.setBackgroundHeight(165);

    }

    public void update(PImage newImage) {
        mq.update(newImage);
        movementBuf[bufPos] = mq.getMovement() * movementFaktor;
        volumeBuf[bufPos] = in.mix.level() * volumeFaktor;
        bufPos = (bufPos + 1) % bufLen;

        normMov = normClamp(avg(movementBuf), 0, 100);
        normVol = normClamp(avg(volumeBuf), 0, 100);

        PApplet.println("Mov: " + String.format("%.2f", normMov) + " - Vol: "
                + String.format("%.2f", normVol) + " - Act: " + activity);
        activity = (normMov * mixValue + normVol * (1 - mixValue));

        if (movementDisplay != null) {
            movementDisplay.setValue(normMov);
        }
        if (volumeDisplay != null) {
            volumeDisplay.setValue(normVol);
        }
        if (activityDisplay != null) {
            activityDisplay.setValue(activity);
        }
    }
    
    float avg(float[] buffer) {
        float avg = 0;
        for (int i = 0; i < bufLen; i++) {
            avg += buffer[i];
        }
        avg = avg / bufLen;
        return avg;
    }

    float normClamp(float value, float min, float max) {
        float normVal = PApplet.norm(value, min, max);
        if (normVal < min) {
            normVal = min;
        }

        if (normVal > max) {
            normVal = max;
        }
        return normVal;
    }

    public float getActivityFaktor() {

        return activity;
    }
}
