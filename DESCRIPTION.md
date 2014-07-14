Mapper
============

Die Software hilft beim Setzen von Punkten, Linien und Flächen über einen
Projektor. Dafür werden die Formen intern verwaltet, und über ein Plugin-
Interface an Effektmodule weitergegeben. Die Effektmodule können dann Effekte
auf Basis der eingemessenen Formen zu produzieren, die dann passgenau 
projiziert werden können. Das Programm ist in Java geschrieben und nutzt im 
Kern die Processing Bibliothek (http://processing.org) und einige Zusatzmodule. 
Für das User-Interface kommt die Processing Erweiterung controlP5 
(http://www.sojamo.de/libraries/controlP5/) zum Einsatz. Die Audioverarbeitung 
erfolgt mit Hilfe der Minim Bibliothek (http://code.compartmental.net/tools/minim/), 
welche als Teil von Processing ausgeliefert wird. Für Effektemodule die mit 
Daten einer Kinectkamera arbeiten, wird die SimpleOpenNI Bibliothek 
(https://code.google.com/p/simple-openni/) verwendet.

Effekte
===========

Die einfachen Effekte zeichnen die Linien oder Flächen in variierenden 
zeitlichen Verläufen. Die audioreaktiven Effekte erzeugen nicht dagestellte 
Textur mit sich ausdehnenden Kreisen, die wie Wellen vom Geräuschpegel 
ausgelöst werden. Linien oder Flächen, welche von diesen Kreisen getroffen 
werden gezeichnet. Aus Geräuschpegel und Bewegung vor der Kamera wird ein 
Aktivitätsfaktor berechnet, welche die Farbgebung der Projektion beeinflusst.

PolyScape/42
================

Für die Installation Polyscape/42 wurden die Objekte aus 3D Modellen gefertigt. Die gleichen 3D Modelle wurden verwendet um texturiert in einer 3D Umgebung gerendert zu werden. Mit Hilfe eines vvvv-Patches wurde eine Blickrichtung auf die virtuelle Szene berechnet, welche der "Blick"-Richtung des Projektor auf die realen Objekte entspricht. Durch genaue Feinkalibrierung fällt das Licht des virtuellen 3D Modells exakt auf das reale Objekt. Mit einer Kinect Kamera wurde der durchschnittliche Abstand von Betrachtern zu dem Objekt gemessen, welcher die Farbtemperatur der Texturen steuert.

Emorph
========
Die Projektion auf der Emorph Installation besteht im wesentlichen aus einer Fluidsimulation. Scheinbar in Flüssigkeit schwimmende Partikel und Farbe folgen einer unsichtbaren Strömung. Mit einer Kinect Kamera wurde die Händer eines Betrachters getrackt, um mit diesen Information die Strömung zu verändern.