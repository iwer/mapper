import de.hawilux.mapper.ui.Gui;
import de.hawilux.mapper.shapes.Edge;
import de.hawilux.mapper.effects.AbstractEffect;

import controlP5.*;
import de.hawilux.mapper.*;

ControlP5 cp5;
Mapper mapper;
AbstractEffect currentEffect;

void setup() {
  size(800, 800, P2D);
  
  cp5 = new ControlP5(this);
  mapper = Mapper.getInstance(this, cp5); //<>//
  currentEffect = new ExampleEffect(this, mapper.getEdges());
  mapper.addEffectControllers(currentEffect);
}

void draw() {
  background(0);
  currentEffect.update();
  currentEffect.display();
}

class ExampleEffect extends AbstractEffect {
  HashMap<Integer, Edge> edges;
  float tint;
  
  ExampleEffect(PApplet parent_, HashMap<Integer, Edge> edges_) {
    super(parent_, "example");
    edges = edges_;
  }
  
  void addEffectControllersToGui(Gui gui){
  }
  void update(){
    tint = random(255);
  }
  void display(){
    for (Edge f : edges.values()) {
            f.getShape().setStroke(parent.color(tint, tint, tint));
            parent.shapeMode(CORNERS);
            parent.shape(f.getShape());
        }
  }
}
