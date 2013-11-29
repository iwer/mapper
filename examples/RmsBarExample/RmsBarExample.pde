import de.hawilux.mapper.*;
import de.hawilux.mapper.effects.*;
import de.hawilux.mapper.shapes.*;
import de.hawilux.mapper.ui.*;
import de.hawilux.mapper.file.*;

import controlP5.*;
import ddf.minim.*;
Minim minim;
AudioInput in;

ControlP5 cp5;
VolumeBar volume;

void setup() {
  size(150, 50, P2D);

  cp5 = new ControlP5(this);

  minim = new Minim(this);
  in = minim.getLineIn();
  
  volume = new VolumeBar(cp5, in, 10,10,100,10);
}

void draw() {
    background(0);
    volume.update();
}
