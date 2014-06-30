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