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
  mapper = new Mapper(this, cp5); //<>//
  currentEffect = new EdgeWalkEffect(this, mapper.getEdges());
  mapper.addEffectControllers(currentEffect);
}

void draw() {
  background(0);
  currentEffect.update();
  currentEffect.display();
}
