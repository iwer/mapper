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
 * EdgeWalkEffect class
 *
 * a edge Walker effect. Simply lines that walk along all Edges 
 *
 */
class EdgeWalkEffect extends AbstractEffect {
  float start;
  float end; 
  protected float time = 1.;
  HashMap<Integer, Edge> edges;

  Slider timeSlider;
  Group grpEffectParams;

  EdgeWalkEffect(PApplet parent_, HashMap<Integer, Edge> edges_) {
    parent = parent_;
    edges = edges_;
  }

  public void addEffectControllersToGui(Gui gui) {
    grpEffectParams =  gui.getCp5().addGroup("edgewalk").setColor(gui.getC());
    gui.getEffectAccordion().addItem(grpEffectParams);
    timeSlider = gui.getCp5().addSlider("effectTime").setPosition(10, 10).setColor(gui.getC()).setRange(1, 10).setNumberOfTickMarks(10).moveTo(grpEffectParams);
    timeSlider.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          time = timeSlider.getValue();
        }
      }
    }
    );
    //gui.getRdbEffects().addItem("edgeWalkEffect", AbstractEffect.EDGE_WALK);
  }
  
  public void setTime(float time_){
    time = time_;
  }
  
  void update() {
    float timepos = (parent.frameCount % (100*time)) * .02 * 1./time;
    start = min(1., timepos);
    end = max(0., timepos - 1);
  }
  
  void display() {
    parent.stroke(color(255, 255, 255));
    parent.strokeWeight(2);

    for (Edge e : edges.values()) {
      float x1 = lerp(e.getA().getX(), e.getB().getX(), start);
      float y1 = lerp(e.getA().getY(), e.getB().getY(), start);
      float x2 = lerp(e.getA().getX(), e.getB().getX(), end);
      float y2 = lerp(e.getA().getY(), e.getB().getY(), end);
      parent.line(x1, y1, x2, y2);
    }
  }
}
