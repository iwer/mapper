public class PcRemoteGui {

    /** The parent. */
    PApplet parent;

    /** The cp5. */
    ControlP5 cp5;

    /** The cb. */
    CallbackListener cb;

    /** The c active. */
    int cActive;

    /** The c background. */
    int cBackground;

    /** The c caption label. */
    int cCaptionLabel;

    /** The c foreground. */
    int cForeground;

    /** The c value label. */
    int cValueLabel;

    /** The c. */
    CColor c;

    /** The console. */
    Println console;

    /** The console text area. */
    Textarea consoleTextArea;

    /** The fps label. */
    Textlabel fpsLabel;

    /** The main accordion. */
    Accordion mainAccordion;

    /** The effect accordion. */
    Accordion effectAccordion;

    /** The rdb effects. */
    RadioButton rdbEffects;

    /** The effect group. */
    Group effectGroup;

    /** The file group. */
    Group fileGroup;

    /** The setup group. */
    Group setupGroup;

    /** The setup select mode group. */
    Group setupSelectModeGroup;

    /** The setup edit group. */
    Group setupEditGroup;

    /** The filename. */
    String filename;

    private Textlabel selectedPointsLabel;

    private int nextEffectTogglePos = 10;

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

//        cActive = parent.color(203, 70, 144);
//        cBackground = parent.color(112, 70, 203);
//        cCaptionLabel = parent.color(0, 0, 0);
//        cForeground = parent.color(156, 70, 203);
//        cValueLabel = parent.color(0, 0, 0);

        c = new CColor();// (cForeground, cBackground, cActive,
        // cCaptionLabel, cValueLabel);
        cp5 = cp5_;

        // frameRate at top
        cp5.addFrameRate().setInterval(10).setPosition(10, 10);
        cp5.addTextlabel("fps").setPosition(25, 10).setText("FPS");
        
        selectedPointsLabel = cp5.addTextlabel("selectedPoint").setPosition(150, 10).setColor(c).setText("Nr:");

        // file group
        fileGroup = cp5.addGroup("file").setColor(c);

        // setup group
        setupGroup = cp5.addGroup("setup").setColor(c).setBackgroundHeight(200);

        // setup -> selectmode
        setupSelectModeGroup = cp5.addGroup("selectmodegroup")
                .setLabel("selectmode").setPosition(0, 55).setColor(c)
                .disableCollapse().moveTo(setupGroup);

        // setup -> edit
        setupEditGroup = cp5.addGroup("editgroup").setLabel("edit")
                .setPosition(0, 100).setColor(c).disableCollapse()
                .moveTo(setupGroup);

        // effect group
        effectGroup = cp5.addGroup("effect").setColor(c);

        // accordion
        mainAccordion = cp5.addAccordion("acc").setPosition(10, 40)
                .setWidth(100).addItem(fileGroup).addItem(setupGroup)
                .addItem(effectGroup);
        mainAccordion.setCollapseMode(Accordion.SINGLE);

        effectAccordion = cp5.addAccordion("effAcc")
                .setPosition(parent.width - 210, 40).setWidth(100);
        mainAccordion.setCollapseMode(Accordion.SINGLE);

        // console
        consoleTextArea = cp5.addTextarea("console")
                .setPosition(10, parent.height - 230).setSize(500, 200)
                .setFont(parent.createFont("", 10)).setLineHeight(14)
                .setColor(c);
        ;

        console = cp5.addConsole(consoleTextArea);//
    }

    int effectCnt = 0;
    int yPos = 10;

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
                if (theEvent.getAction() == ControlP5.ACTION_BROADCAST) {
                    oscStack.sendOscMessage("/mapper/effect/"
                            + theEvent.getController().getName(),
                            (int) theEvent.getController().getValue());
                }
            }
        });
        effectCnt++;
    }

    public Group getEffectGroup() {
        return effectGroup;
    }

    /**
     * Gets the cp5.
     * 
     * @return the cp5
     */
    public ControlP5 getCp5() {
        return cp5;
    }

    /**
     * Gets the c.
     * 
     * @return the c
     */
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
        } else {
            cp5.hide();
        }
    }

    /**
     * Sets the console enabled.
     * 
     * @param state
     *            the new console enabled
     */
    public void setConsoleEnabled(boolean state) {
        if (state == true) {
            consoleTextArea.show();
        } else {
            consoleTextArea.hide();
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
