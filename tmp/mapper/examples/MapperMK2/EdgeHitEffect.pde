/*
 *   A 2D Video Mapping Tool created from experiences in the HAWilux project
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this program; if not, write to the Free Software Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *
 * EdgeHitEffect class
 *
 * a edge hit effect. 
 *
 */
class EdgeHitEffect extends AbstractEffect {
  //  float start;
  //  float end; 
  protected float hitRadius = 50.;

  HashMap<Integer, Edge> edges;

  private   HashMap<Integer, Integer> hitTime;

  private int currentTime = 1;
  private int fadeTime = 10;


  Slider hitRadiusSlider;
  Slider fadeTimeSlider;
  Group grpEffectParams;



  EdgeHitEffect(PApplet parent_, HashMap<Integer, Edge> edges_) {
    parent = parent_;
    edges = edges_;
    hitTime = new HashMap<Integer, Integer>();
    for (Edge e : edges.values()){
      hitTime.put(e.getId(), 1);
    }
  }

  public void addEffectControllersToGui(Gui gui) {
    grpEffectParams =  gui.getCp5().addGroup("edgehit").setColor(gui.getC());
    gui.getEffectAccordion().addItem(grpEffectParams);
    hitRadiusSlider = gui.getCp5().addSlider("edgeHitRadius").setCaptionLabel("hitRadius").setPosition(10, 10).setColor(gui.getC()).setRange(10, 100).setNumberOfTickMarks(10).moveTo(grpEffectParams);
    hitRadiusSlider.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          hitRadius = hitRadiusSlider.getValue();
        }
      }
    }
    );
    fadeTimeSlider = gui.getCp5().addSlider("edgeHitFadeTime").setCaptionLabel("fadeTime").setPosition(10, 35).setColor(gui.getC()).setRange(10, 100).setNumberOfTickMarks(10).moveTo(grpEffectParams);
    fadeTimeSlider.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          fadeTime = int(fadeTimeSlider.getValue());
        }
      }
    }
    );
    //gui.getRdbEffects().addItem("edgeHitEffect", AbstractEffect.EDGE_HIT);
  }

  public void setHitRadius(float r_) {
    hitRadius = r_;
  }

  void update() {
    currentTime = parent.frameCount;
  }

  void display() {
    parent.strokeWeight(2);

    for (Edge e : edges.values()) {
       if(mouseOver(e) || hitTime.get(e.getId()) == null) {
        hitTime.put(e.getId(), currentTime);
      }
      int time = hitTime.get(e.getId());
      float tint = 255 - float(currentTime - time) / fadeTime * 255;
      parent.stroke(color(tint, tint, tint));

      if (currentTime - time < fadeTime) {
        parent.line(e.getA().getX(), e.getA().getY(), e.getB().getX(), e.getB().getY());
      }
    }
  }

  // from http://www.openprocessing.org/sketch/8009
  boolean mouseOver(Edge e_) {

    float x1 = e_.getA().getX();
    float y1 = e_.getA().getY();
    float x2 = e_.getB().getX();
    float y2 = e_.getB().getY();

    float cx = parent.mouseX;
    float cy = parent.mouseY;
    float cr = hitRadius;

    float dx = x2 - x1;
    float dy = y2 - y1;
    float a = dx * dx + dy * dy;
    float b = 2 * (dx * (x1 - cx) + dy * (y1 - cy));
    float c = cx * cx + cy * cy;
    c += x1 * x1 + y1 * y1;
    c -= 2 * (cx * x1 + cy * y1);
    c -= cr * cr;
    float bb4ac = b * b - 4 * a * c;

    //println(bb4ac);

    if (bb4ac < 0) {  // Not intersecting
      return false;
    }
    else {

      float mu = (-b + sqrt( b*b - 4*a*c )) / (2*a);
      float ix1 = x1 + mu*(dx);
      float iy1 = y1 + mu*(dy);
      mu = (-b - sqrt(b*b - 4*a*c )) / (2*a);
      float ix2 = x1 + mu*(dx);
      float iy2 = y1 + mu*(dy);

      // The intersection points
      //ellipse(ix1, iy1, 10, 10);
      //ellipse(ix2, iy2, 10, 10);

      float testX;
      float testY;
      // Figure out which point is closer to the circle
      if (dist(x1, y1, cx, cy) < dist(x2, y2, cx, cy)) {
        testX = x2;
        testY = y2;
      } 
      else {
        testX = x1;
        testY = y1;
      }

      if (dist(testX, testY, ix1, iy1) < dist(x1, y1, x2, y2) || dist(testX, testY, ix2, iy2) < dist(x1, y1, x2, y2)) {
        return true;
      } 
      else {
        return false;
      }
    }
  }
}
