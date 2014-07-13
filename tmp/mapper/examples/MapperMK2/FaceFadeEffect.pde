/*
 *   An effect for Mapper2K
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   Belongs to Janina Schlichtes Masterthesis until further notice
 *
 * FaceFadeEffect class
 *
 *  
 *
 */
class FaceFadeEffect extends AbstractEffect {
  HashMap<Integer, Face> faces;
  HashMap<Integer, Integer> fadeInterval;

  int currentTime;
  float fadetime = 1;

  Group grpEffectParams;
  Slider slFadetime;

  FaceFadeEffect(PApplet parent_, HashMap<Integer, Face> faces_) {
    parent = parent_;
    faces = faces_;
    fadeInterval = new HashMap<Integer, Integer>();
    for (Face f : faces.values()) {
      fadeInterval.put(f.getId(), int(random(360, 720)));
    }
  }
  public void addEffectControllersToGui(Gui gui) {
    grpEffectParams =  gui.getCp5().addGroup("facefade").setColor(gui.getC());
    gui.getEffectAccordion().addItem(grpEffectParams);
    slFadetime = gui.getCp5().addSlider("faceFadeFadeTime").setCaptionLabel("fadetime").setPosition(10, 10).setColor(gui.getC()).setRange(.001, 50).setValue(fadetime).moveTo(grpEffectParams);
    slFadetime.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          fadetime = slFadetime.getValue();
        }
      }
    }
    );
    //gui.getRdbEffects().addItem("FaceFadeEffect", AbstractEffect.FACE_FADE);
  }
  public void update() {
    currentTime = parent.frameCount;
  }
  public void display() {
    for (Face f : faces.values()) {
      if (fadeInterval.get(f.getId()) == null) {
        fadeInterval.put(f.getId(), int(random(360, 720)));
      }
      float progress = (currentTime * fadetime) % fadeInterval.get(f.getId()) / fadeInterval.get(f.getId());
      float tint = PApplet.map(sin(radians(progress * 360)), -1, 1, 0, 255);
      f.getShape().setFill(color(tint, tint, tint));
      f.getShape().setStroke(false);
      parent.shapeMode(CORNERS);
      parent.shape(f.getShape());
    }
  }
}
