
import controlP5.*;
import de.hawilux.mapper.*;

ControlP5 cp5;
Mapper mapper;

void setup() {
  size(800, 800, P2D);
  
  cp5 = new ControlP5(this);
  mapper = new Mapper(this, cp5); //<>//
}

void draw() {
  background(0 );
}
