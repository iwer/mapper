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
 * WalkEffect class
 *
 * a line hit effect. 
 *
 */
class FaceHitEffect extends AbstractEffect {
  protected float hitRadius = 50.;

  Slider fadeTimeSlider;
  protected HashMap<Integer, Face> faces;
  private   HashMap<Integer, Integer> hitTime;

  private int currentTime = 1;
  private int fadeTime = 10;

  Group grpEffectParams;

  FaceHitEffect(PApplet parent_, HashMap<Integer, Face> faces_) {
    parent = parent_;
    faces = faces_;
    hitTime = new HashMap<Integer, Integer>();
    for (Face f : faces.values()) {
      hitTime.put(f.getId(), 1);
    }
  }

  public void addEffectControllersToGui(Gui gui) {
    grpEffectParams =  gui.getCp5().addGroup("facehit").setColor(gui.getC());
    gui.getEffectAccordion().addItem(grpEffectParams);
    fadeTimeSlider = gui.getCp5().addSlider("faceHitFadeTime").setCaptionLabel("fadetime").setPosition(10, 10).setColor(gui.getC()).setRange(10, 100).setNumberOfTickMarks(10).moveTo(grpEffectParams);
    fadeTimeSlider.addCallback(new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        if (theEvent.getAction()==ControlP5.ACTION_BROADCAST) {
          fadeTime = int(fadeTimeSlider.getValue());
        }
      }
    }
    );
    //gui.getRdbEffects().addItem("FaceHitEffect", AbstractEffect.FACE_HIT);
  }

  public void setHitRadius(float r_) {
    hitRadius = r_;
  }

  void update() {
    currentTime = parent.frameCount;
  }

  void display() {
    parent.stroke(color(255, 255, 255));
    parent.strokeWeight(2);

    for (Face f : faces.values()) {
      if (mouseOver(f) || hitTime.get(f.getId()) == null) {
        hitTime.put(f.getId(), currentTime);
      }
      int time = hitTime.get(f.getId());
      f.getShape().setStroke(false);
      float tint = 255 - float(currentTime - time) / fadeTime * 255;
      f.getShape().setFill(color(tint, tint, tint));
      //println(currentTime + " - " + time + " < " + fadeTime + " ? " + (currentTime -time) + " Color " + tint);      

      if (currentTime - time < fadeTime) {
        parent.shapeMode(CORNERS);
        parent.shape(f.getShape());
      }
    }
  }

  boolean mouseOver(Face f) {
    ArrayList<PVector> verts = f.getVertices();
    PVector pos = new PVector(parent.mouseX, parent.mouseY);
    int i, j;
    boolean c=false;
    int sides = verts.size();
    for (i = 0,  j = sides - 1; i < sides;j = i++) {
      if (( ((verts.get(i).y <= pos.y) && (pos.y < verts.get(j).y)) || ((verts.get(j).y <= pos.y) && (pos.y < verts.get(i).y))) &&
        (pos.x < (verts.get(j).x - verts.get(i).x) * (pos.y - verts.get(i).y) / (verts.get(j).y - verts.get(i).y) + verts.get(i).x)) {
        c = !c;
      }
    }
    return c;
  }
}
