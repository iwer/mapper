mapper for Processing
=====================

A 2D Video Mapping Tool created from experiences in the [HAWilux](http://hawilux.de) project.

The tool starts fullscreen on the second screen (works best with a projector as second screen :) ), and launches a control window on the first screen. Via the control screen you can draw lines onto the projector-lit scene point by point. New points are set with the middle mouse button. The right button selects points. Lines are draw when a point is selected, and a new one is created. Lines can be connected to existing points. In line-select mode faces can be created by rightclicking lines that define the border of those faces (this could surely be realized much better).

The created shapes are then accessible for creating effects. An abstract effect definition helps integrating those into the user interface. Have a look at the included SimpleMapper example sketch.

Depends on:

* Processing 2.1
* ControlP5
* OscP5
* opencv_processing
* SimpleOpenNI

[Built with Processing](http://processing.org)
