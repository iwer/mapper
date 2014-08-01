package de.hawilux.mapper.ui;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscStatus;
import processing.core.PApplet;
import processing.core.PImage;
import controlP5.ControlP5;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import de.hawilux.mapper.Mapper;
import de.hawilux.mapper.effects.AbstractEffect;
import de.hawilux.mapper.effects.jma.EdgeFadeEffect;
import de.hawilux.mapper.effects.jma.EdgeHitEffect;
import de.hawilux.mapper.effects.jma.EdgeTextureActivationEffect;
import de.hawilux.mapper.effects.jma.EdgeWalkEffect;
import de.hawilux.mapper.effects.jma.FaceFadeEffect;
import de.hawilux.mapper.effects.jma.FaceHitEffect;
import de.hawilux.mapper.effects.jma.VolumeCircleActivationTexture;
import de.hawilux.mapper.net.OscStack;

public class MapperControlFrame extends PApplet {
    PApplet           parent;
    PImage            background;
    public int        w;
    public int        h;

    Mapper            mapper;
    public Mapper getMapper() {
        return mapper;
    }

    private ControlP5 cp5;
    Minim             minim;
    AudioInput        in;

    AbstractEffect    effect;

    boolean           init = false;
    int               tgtWidth, tgtHeight;
    float             scaleRatio;

    public static int mainMouseX;
    public static int mainMouseY;
    public static int projectorMouseX;
    public static int projectorMouseY;
    private int       projektorScreenWidth;
    private int       projektorScreenHeight;
    private int       controlScreenWidth;
    private int       controlScreenHeight;

    private MapperControlFrame() {
    }

    public MapperControlFrame(PApplet parent, PImage remoteImage, int theWidth,
            int theHeight) {
        this.parent = parent;
        this.background = remoteImage;
        this.w = theWidth;
        this.h = theHeight;
    }

    public void setup() {
        size(w, h, P2D);
        //frameRate(25);

        projektorScreenWidth = background.width;
        projektorScreenHeight = background.height;
        controlScreenWidth = width;
        controlScreenHeight = height;

        float widthRatio = (float) projektorScreenWidth / controlScreenWidth;
        float heightRatio = (float) projektorScreenHeight / controlScreenHeight;

        PApplet.println("WidthRatio: " + widthRatio + " HeightRatio: "
                + heightRatio);

        if (widthRatio >= heightRatio) {
            tgtWidth = (int) (projektorScreenWidth / widthRatio);
            tgtHeight = (int) (projektorScreenHeight / widthRatio);
            scaleRatio = widthRatio;
        } else {
            tgtWidth = (int) (projektorScreenWidth / heightRatio);
            tgtHeight = (int) (projektorScreenHeight / heightRatio);
            scaleRatio = heightRatio;
        }

        PApplet.println("tgtWidth: " + tgtWidth + " tgtHeigth: " + tgtHeight);

        minim = new Minim(this);
        in = minim.getLineIn();

        OscStack.getInstance(this, new OscListener(), "239.0.0.1", 7777);

        cp5 = new ControlP5(this);
        mapper = Mapper.getInstance(this, cp5, background);

//         VolumeCircleActivationTexture.getInstance(this, in);
//        
//         mapper.registerEffect(new EdgeWalkEffect(this, mapper.getEdges()));
//         mapper.registerEffect(new EdgeFadeEffect(this, mapper.getEdges()));
//         mapper.registerEffect(new EdgeHitEffect(this, mapper.getEdges()));
//         mapper.registerEffect(new FaceFadeEffect(this, mapper.getFaces()));
//         mapper.registerEffect(new FaceHitEffect(this, mapper.getFaces()));
//         mapper.registerEffect(new EdgeTextureActivationEffect(this, mapper
//         .getEdges()));
//         mapper.registerEffect(new ColorTestEffect(this, mapper.getEdges(),
//         mapper.getColorManager()));
//         mapper.registerEffect(new FaceAudioEffect(this, mapper.getFaces()));
        init = true;

        registerMethod("draw", mapper);
        // registerMethod("dispose", mapper);
        registerMethod("mouseEvent", mapper);
        registerMethod("keyEvent", mapper);
        registerMethod("post", mapper);
    }

    public boolean isInitialized() {
        return init;
    }

    public void draw() {
        background(10, 10, 50);
        image(mapper.getOffscreenBuffer(), leftRightMarginWidth(),
                (height - tgtHeight) / 2, tgtWidth, tgtHeight);
        text("Points: " + mapper.getPoints().size(), 400, 10);
        text("Edges:  " + mapper.getEdges().size(), 400, 20);
        text("Faces:  " + mapper.getFaces().size(), 400, 30);
    }

    public void mouseMoved() {
        updateVirtualMouses();
    }

    public void mouseDragged() {
        updateVirtualMouses();
    }

    private void updateVirtualMouses() {
        mainMouseX = mouseX;
        mainMouseY = mouseY;

        projectorMouseX = (int) ((((float) (mouseX - leftRightMarginWidth()) / tgtWidth) * projektorScreenWidth));
        projectorMouseY = (int) ((((float) (mouseY - topBottomMarginWidth()) / tgtHeight) * projektorScreenHeight));

        if (mouseX >= leftRightMarginWidth() + tgtWidth) {
            projectorMouseX = projektorScreenWidth - 1;
        }
        if (mouseX < leftRightMarginWidth()) {
            projectorMouseX = 0;
        }

        if (mouseY >= topBottomMarginWidth() + tgtHeight) {
            projectorMouseY = projektorScreenWidth - 1;
        }
        if (mouseY < topBottomMarginWidth()) {
            projectorMouseY = 0;
        }
    }

    private int leftRightMarginWidth() {
        return (width - tgtWidth) / 2;
    }

    private int topBottomMarginWidth() {
        return (height - tgtHeight) / 2;
    }

    public PImage getImage() {
        if (init) {
            return mapper.getRemoteImage();
        } else {
            return null;
        }
    }

    class OscListener implements OscEventListener {
        @Override
        public void oscEvent(OscMessage theMessage) {
        }

        @Override
        public void oscStatus(OscStatus theStatus) {
        }
    }
}
