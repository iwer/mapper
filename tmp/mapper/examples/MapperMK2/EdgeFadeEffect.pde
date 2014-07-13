/*
 *   An effect for Mapper2K
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   Belongs to Janina Schlichtes Masterthesis until further notice
 *
 * EdgeFadeEffect class
 *
 * a edge hit effect. 
 *
 */
class EdgeFadeEffect extends AbstractEffect {
  HashMap<Integer, Edge> edges;
  HashMap<Integer, Integer> fadeInterval;

  int currentTime;
  float fadetime = 1;

  Group grpEffectParams;
  Slider slFadetime;

  EdgeFadeEffect(PApplet parent_, HashMap<Integer, Edge> edges_) {
    parent = parent_;
    edges = edges_;
    fadeInterval = new HashMap<Integer, Integer>();
    for (Edge f : edges.values()) {
      fadeInterval.put(f.getId(), int(random(360, 720)));
    }
  }
  public void addEffectControllersToGui(Gui gui) {
    grpEffectParams =  gui.getCp5().addGroup("edgefade").setColor(gui.getC());
    gui.getEffectAccordion().addItem(grpEffectParams);
    slFadetime = gui.getCp5().addSlider("edgeFadeFadeTime").setCaptionLabel("fadetime").setPosition(10, 10).setColor(gui.getC()).setRange(.001, 50).setValue(fadetime).moveTo(grpEffectParams);
    slFadetime.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          fadetime = slFadetime.getValue();
        }
      }
    }
    );
    //gui.getRdbEffects().addItem("EdgeFadeEffect", AbstractEffect.EDGE_FADE);
  }
  public void update() {
    currentTime = parent.frameCount;
  }
  public void display() {
    for (Edge f : edges.values()) {
      if (fadeInterval.get(f.getId()) == null) {
        fadeInterval.put(f.getId(), int(random(360, 720)));
      }
      float progress = (currentTime * fadetime) % fadeInterval.get(f.getId()) / fadeInterval.get(f.getId());
      float tint = PApplet.map(sin(radians(progress * 360)), -1, 1, 0, 255);
      f.getShape().setStroke(color(tint, tint, tint));
      parent.shapeMode(CORNERS);
      parent.shape(f.getShape());
    }
  }
}
