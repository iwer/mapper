import ddf.minim.spi.*; //<>//
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

import controlP5.*;
import oscP5.*;
import de.hawilux.mapper.*;
import de.hawilux.mapper.effects.*;
import de.hawilux.mapper.shapes.*;
import de.hawilux.mapper.ui.*;
import de.hawilux.mapper.tools.*;
import java.awt.Insets;
import java.awt.Frame;
import SimpleOpenNI.SimpleOpenNI;


DisplayConfiguration dc;

MapperControlFrame          mcf;

ColorManager                cm;
ActivityTracker             at;

PImage                      background;
PImage                      kinectImage;

boolean                     initialized = false;

public void setup() {
  size(displayWidth, displayHeight, P2D);
  background = createImage(width, height, RGB);

  dc = new DisplayConfiguration();

  mcf = addControlFrame("Mapper MKII - ControlWindow", background, 
  DisplayConfiguration.getPrimaryDisplay().getWidth(), 
  DisplayConfiguration.getPrimaryDisplay().getHeight());
}

public void draw() {
  if (!initialized) {
    initialize();
  }

  background(0);
  blendMode(ADD);
  background = mcf.getImage();
  if (background != null) {
    image(background, 0, 0, width, height);
  }
}

private void initialize() {
  if (mcf.isInitialized()) {
    cm = mcf.getMapper().addColorManager();

    mcf.getMapper().registerEffect(
    new ExampleEffect(this, mcf.getMapper().getEdges()));

    initialized = true;
  }
}

MapperControlFrame addControlFrame(String theName, PImage theImage, 
int theWidth, int theHeight) {
  Frame f = new Frame(theName);
  MapperControlFrame p = new MapperControlFrame(this, theImage, 
  theWidth - 20, theHeight - 80);
  f.add(p);
  p.init();
  f.setTitle(theName);
  f.setLocation(DisplayConfiguration.getPrimaryDisplay().getX() + 10, 
  DisplayConfiguration.getPrimaryDisplay().getY() + 10);
  f.setResizable(false);
  f.setVisible(true);
  Insets borders = f.getInsets();
  f.setSize(p.w + borders.left + borders.right, p.h + borders.top
    + borders.bottom);
  return p;
}


class ExampleEffect extends AbstractEffect {
  HashMap<Integer, IEdge> edges;
  float tint;

  ExampleEffect(PApplet parent_, HashMap<Integer, IEdge> edges_) {
    super(parent_, "example");
    edges = edges_;
  }

  void addEffectControllersToGui(Gui gui) {
  }
  void update() {
    tint = random(255);
  }
  void display() {
    for (IEdge f : edges.values ()) {
      f.getShape().setStroke(color(tint, tint, tint));
      parent.shapeMode(CORNERS);
      parent.shape(f.getShape());
    }
  }
}
