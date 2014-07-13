/*
 *   An effect for Mapper2K
 *   Copyright (C) 2013  Iwer Petersen (iwer.petersen@gmail.com)
 *
 *   Belongs to Janina Schlichtes Masterthesis until further notice
 *
 * NDWEffectComposition class
 */
import ddf.minim.*;

class NDWEffectComposition extends AbstractEffect {
  public AbstractEffect currentEffect;

  FaceFadeEffect faceFadeEffect;
  EdgeAudioEffect edgeAudioEffect;
  EdgeWalkEffect lineWalkEffect;
  
  AudioInput audioin;
  
  float audioThreshold = .1;
  int silenceTime = 100;
  int lastTimeAudio;


  NDWEffectComposition(PApplet parent_, HashMap<Integer, Edge> edges_, HashMap<Integer, Face> faces_, AudioInput audioin_) {
    parent = parent_;
    audioin = audioin_;
    faceFadeEffect = new FaceFadeEffect(parent, faces_);
    edgeAudioEffect = new EdgeAudioEffect(parent, edges_, audioin);
    lineWalkEffect = new EdgeWalkEffect(parent, edges_);
    currentEffect = lineWalkEffect;
  }

  public void addEffectControllersToGui(Gui gui) {
    edgeAudioEffect.addEffectControllersToGui(gui);
    faceFadeEffect.addEffectControllersToGui(gui);
    lineWalkEffect.addEffectControllersToGui(gui);
  }
  public void update() {
    if(audioin.mix.get(0) > audioThreshold) {
      lastTimeAudio = parent.frameCount;
    }
//    if(parent.frameCount % 1000 < 500) {
//      currentEffect = lineWalkEffect;
//    } else {
//      currentEffect = faceFadeEffect;
//    }
    edgeAudioEffect.update();
    faceFadeEffect.update();
    lineWalkEffect.update();
  }
  public void display() {
    faceFadeEffect.display();
    lineWalkEffect.display();
    if(parent.frameCount - lastTimeAudio < silenceTime) {
      edgeAudioEffect.display();
    }
  }
}
