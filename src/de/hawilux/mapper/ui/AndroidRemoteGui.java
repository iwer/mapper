package de.hawilux.mapper.ui;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.Accordion;
import controlP5.CColor;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.Group;
import controlP5.Textlabel;
import controlP5.Toggle;
import de.hawilux.mapper.net.OscStack;
import de.hawilux.mapper.ui.cp5view.ArrowButton;

public class AndroidRemoteGui {
    /** The parent. */
    PApplet           parent;

    /** The cp5. */
    ControlP5         cp5;

    Accordion         mainAccordion;
    Group             effectGroup;
    Group             setupGroup;
    private Textlabel selectedPointsLabel;
    CColor            colorScheme;

    public AndroidRemoteGui(PApplet parent) {
        this(parent, new ControlP5(parent));
    }

    public AndroidRemoteGui(PApplet parent, ControlP5 cp5) {
        this.parent = parent;
        this.cp5 = cp5;

        PFont pfont = parent.createFont("Mono", 20, true); // use true/false for
                                                           // smooth/no-smooth
        ControlFont font = new ControlFont(pfont, 30);
        ControlFont smallFont = cp5.getFont();

        colorScheme = ControlP5.getColor();

        cp5.setFont(font);

        cp5.addFrameRate().setInterval(10).setPosition(10, 10)
                .setFont(smallFont);
        cp5.addTextlabel("fps").setPosition(25, 10).setFont(smallFont)
                .setText("FPS");

        setupGroup = cp5.addGroup("setup").setBarHeight(50)
                .setBackgroundHeight(600);
        // setupGroup.getCaptionLabel().getStyle().setPadding(5,5,4,5);

        cp5.addButton("previous").setSize(parent.width / 2 - 20, 50)
                .setPosition(0, 10).moveTo(setupGroup);
        cp5.addButton("next").setSize(parent.width / 2 - 20, 50)
                .setPosition(parent.width / 2, 10).moveTo(setupGroup);

        setSelectedPointsLabel(cp5.addTextlabel("selected").setPosition(0, 80)
                .setText("Nr: ").moveTo(setupGroup));

        cp5.addButton("up").setCaptionLabel("")
                .setPosition(parent.width / 2 - 60, 80).setSize(100, 100)
                .setView(new ArrowButton(parent, ArrowButton.UP))
                .moveTo(setupGroup);
        cp5.addButton("left").setCaptionLabel("")
                .setPosition(parent.width / 2 - 160, 180).setSize(100, 100)
                .setView(new ArrowButton(parent, ArrowButton.LEFT))
                .moveTo(setupGroup);
        cp5.addButton("right").setCaptionLabel("")
                .setPosition(parent.width / 2 + 40, 180).setSize(100, 100)
                .setView(new ArrowButton(parent, ArrowButton.RIGHT))
                .moveTo(setupGroup);
        cp5.addButton("down").setCaptionLabel("")
                .setPosition(parent.width / 2 - 60, 280).setSize(100, 100)
                .setView(new ArrowButton(parent, ArrowButton.DOWN))
                .moveTo(setupGroup);

        cp5.addToggle("helper").setSize(parent.width / 2 - 20, 50)
                .setPosition(0, 400).moveTo(setupGroup);

        effectGroup = cp5.addGroup("effect").setBarHeight(50);

        mainAccordion = cp5.addAccordion("acc").setPosition(10, 30)
                .setWidth(parent.width - 20).addItem(setupGroup)
                .addItem(effectGroup);
        mainAccordion.setCollapseMode(ControlP5Constants.SINGLE);

    }

    int effectCnt = 0;
    int yPos      = 10;

    public void addEffectToggle(String effectname, final OscStack oscStack) {
        Toggle t;
        if (effectCnt % 2 == 0) {
            t = cp5.addToggle(effectname.toLowerCase())
                    .setSize(parent.width / 2 - 20, 50).setPosition(0, yPos)
                    .moveTo(effectGroup);
        } else {
            t = cp5.addToggle(effectname.toLowerCase())
                    .setSize(parent.width / 2 - 20, 50)
                    .setPosition(parent.width / 2, yPos).moveTo(effectGroup);
            yPos += 100;
        }

        t.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5Constants.ACTION_BROADCAST) {
                    oscStack.sendOscMessage("/mapper/effect/"
                            + theEvent.getController().getName(),
                            (int) theEvent.getController().getValue());
                }
            }
        });
        effectCnt++;
    }

    public Textlabel getSelectedPointsLabel() {
        return selectedPointsLabel;
    }

    public void setSelectedPointsLabel(Textlabel selectedPointsLabel) {
        this.selectedPointsLabel = selectedPointsLabel;
    }

    public void setSelectedPointsLabelText(String text) {
        this.selectedPointsLabel.setText(text);
    }
}
