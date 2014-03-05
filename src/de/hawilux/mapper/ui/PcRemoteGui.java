package de.hawilux.mapper.ui;

import processing.core.PApplet;
import controlP5.Accordion;
import controlP5.Button;
import controlP5.CColor;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.RadioButton;
import controlP5.Textlabel;
import controlP5.Toggle;
import de.hawilux.mapper.net.OscStack;

public class PcRemoteGui implements IGui {
    /** The select points. */
    public final int SELECT_POINTS       = 0;

    /** The select edges. */
    public final int SELECT_EDGES        = 1;

    /** The select faces. */
    public final int SELECT_FACES        = 2;

    /** The parent. */
    PApplet          parent;

    /** The cp5. */
    ControlP5        cp5;

    /** The cb. */
    CallbackListener cb;

    /** The c active. */
    int              cActive;

    /** The c background. */
    int              cBackground;

    /** The c caption label. */
    int              cCaptionLabel;

    /** The c foreground. */
    int              cForeground;

    /** The c value label. */
    int              cValueLabel;

    /** The c. */
    CColor           c;

    /** The fps label. */
    Textlabel        fpsLabel;

    /** The main accordion. */
    Accordion        mainAccordion;

    /** The effect accordion. */
    Accordion        effectAccordion;

    /** The rdb effects. */
    RadioButton      rdbEffects;

    /** The effect group. */
    Group            effectGroup;

    /** The file group. */
    Group            fileGroup;

    /** The setup group. */
    Group            setupGroup;

    /** The setup select mode group. */
    Group            setupSelectModeGroup;

    /** The setup edit group. */
    Group            setupEditGroup;

    /** The btn new config. */
    Button           btnNewConfig;

    /** The btn load config. */
    Button           btnLoadConfig;

    /** The btn save config. */
    Button           btnSaveConfig;

    /** The tgl helper. */
    Toggle           tglHelper;

    /** The rdb select mode. */
    RadioButton      rdbSelectMode;

    /** The btn delete selected. */
    Button           btnDeleteSelected;

    /** The btn switch dir. */
    Button           btnSwitchDir;

    /** The btn subdivide. */
    Button           btnSubdivide;

    /** The filename. */
    String           filename            = new String();

    Textlabel        selectedPointsLabel;

    private int      nextEffectTogglePos = 10;

    /**
     * Instantiates a new gui.
     * 
     * @param parent_
     *            the parent_
     */
    public PcRemoteGui(PApplet parent_) {
        this(parent_, new ControlP5(parent_));
    }

    /**
     * Instantiates a new gui.
     * 
     * @param parent_
     *            the parent_
     * @param cp5_
     *            the cp5_
     */
    public PcRemoteGui(PApplet parent_, ControlP5 cp5_) {
        this.parent = parent_;

        // cActive = parent.color(203, 70, 144);
        // cBackground = parent.color(112, 70, 203);
        // cCaptionLabel = parent.color(0, 0, 0);
        // cForeground = parent.color(156, 70, 203);
        // cValueLabel = parent.color(0, 0, 0);

        c = new CColor();// (cForeground, cBackground, cActive,
        // cCaptionLabel, cValueLabel);
        cp5 = cp5_;

        // frameRate at top
        cp5.addFrameRate().setInterval(10).setPosition(10, 10);
        fpsLabel = cp5.addTextlabel("fps").setPosition(25, 10).setText("FPS");

        selectedPointsLabel = cp5.addTextlabel("selectedPoint").setPosition(150, 10).setColor(c)
                .setText("Nr:");

        // file group
        fileGroup = cp5.addGroup("file").setColor(c);

        btnNewConfig = cp5.addButton("newConfig").setPosition(10, 10).setColor(c).moveTo(fileGroup);

        btnLoadConfig = cp5.addButton("loadConfig").setPosition(10, 35).setColor(c)
                .moveTo(fileGroup);
        btnSaveConfig = cp5.addButton("saveConfig").setPosition(10, 60).setColor(c)
                .moveTo(fileGroup);

        // setup group
        setupGroup = cp5.addGroup("setup").setColor(c).setBackgroundHeight(200);

        // setup -> selectmode
        setupSelectModeGroup = cp5.addGroup("selectmodegroup").setLabel("selectmode")
                .setPosition(0, 55).setColor(c).disableCollapse().moveTo(setupGroup);

        // setup -> edit
        setupEditGroup = cp5.addGroup("editgroup").setLabel("edit").setPosition(0, 100).setColor(c)
                .disableCollapse().moveTo(setupGroup);

        tglHelper = cp5.addToggle("showHelper").setPosition(10, 10).setColor(c)
                .setMode(ControlP5.SWITCH).setValue(true).moveTo(setupGroup);

        rdbSelectMode = cp5.addRadioButton("selectmode").setPosition(10, 2).setColor(c)
                .setNoneSelectedAllowed(false).addItem("points", 1).addItem("edges", 2)
                .addItem("faces", 3).moveTo(setupSelectModeGroup).activate("points");
        btnDeleteSelected = cp5.addButton("deleteSelected").setPosition(10, 2).setColor(c)
                .moveTo(setupEditGroup);
        btnSubdivide = cp5.addButton("subdivide").setPosition(10, 27).setColor(c).hide()
                .moveTo(setupEditGroup);
        btnSwitchDir = cp5.addButton("switchdir").setPosition(10, 52).setColor(c).hide()
                .moveTo(setupEditGroup);

        // effect group
        effectGroup = cp5.addGroup("effect").setColor(c);

        // accordion
        mainAccordion = cp5.addAccordion("acc").setPosition(10, 40).setWidth(100)
                .addItem(fileGroup).addItem(setupGroup).addItem(effectGroup);
        mainAccordion.setCollapseMode(Accordion.SINGLE);

        effectAccordion = cp5.addAccordion("effAcc").setPosition(parent.width - 210, 40)
                .setWidth(100);
        mainAccordion.setCollapseMode(Accordion.SINGLE);
    }

    public RadioButton getRdbSelectMode() {
        return rdbSelectMode;
    }

    int effectCnt = 0;
    int yPos      = 10;

    public void addEffectToggle(String effectname, final OscStack oscStack) {
        Toggle t;
        if (effectCnt % 2 == 0) {
            t = cp5.addToggle(effectname.toLowerCase()).setSize(parent.width / 2 - 20, 50)
                    .setPosition(0, yPos).moveTo(effectGroup);
        }
        else {
            t = cp5.addToggle(effectname.toLowerCase()).setSize(parent.width / 2 - 20, 50)
                    .setPosition(parent.width / 2, yPos).moveTo(effectGroup);
            yPos += 100;
        }

        t.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    oscStack.sendOscMessage("/mapper/effect/" + theEvent.getController().getName(),
                            (int) theEvent.getController().getValue());
                }
            }
        });
        effectCnt++;
    }

    public Group getEffectGroup() {
        return effectGroup;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.ui.IGui#getCp5()
     */
    @Override
    public ControlP5 getCp5() {
        return cp5;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hawilux.mapper.ui.IGui#getC()
     */
    @Override
    public CColor getC() {
        return c;
    }

    /**
     * Gets the setup select mode group.
     * 
     * @return the setup select mode group
     */
    public Group getSetupSelectModeGroup() {
        return setupSelectModeGroup;
    }

    /**
     * Gets the setup edit group.
     * 
     * @return the setup edit group
     */
    public Group getSetupEditGroup() {
        return setupEditGroup;
    }

    /**
     * Gets the file group.
     * 
     * @return the file group
     */
    public Group getFileGroup() {
        return fileGroup;
    }

    /**
     * Gets the setup group.
     * 
     * @return the setup group
     */
    public Group getSetupGroup() {
        return setupGroup;
    }

    /**
     * Gets the effect accordion.
     * 
     * @return the effect accordion
     */
    public Accordion getEffectAccordion() {
        return effectAccordion;
    }

    /**
     * Gets the rdb effects.
     * 
     * @return the rdb effects
     */
    public RadioButton getRdbEffects() {
        return rdbEffects;
    }

    // change gui functions
    /**
     * Show.
     * 
     * @param state
     *            the state
     */
    public void show(boolean state) {
        if (state == true) {
            cp5.show();
        }
        else {
            cp5.hide();
        }
    }

    /**
     * Sets the line editor enabled.
     * 
     * @param state
     *            the new line editor enabled
     */
    public void setLineEditorEnabled(boolean state) {
        if (state == true) {
            btnSubdivide.show();
            btnSwitchDir.show();
        }
        else {
            btnSubdivide.hide();
            btnSwitchDir.hide();
        }
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