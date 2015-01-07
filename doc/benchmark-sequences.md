In below charts, expected notes (midi file) are drawn as rectangle.

Pitch detected value is show has a translucent scatter :
* gray when it stand in the frequency range of an expected note, red otherwise.
* alpha according to amplitude.

Amplitude is shown in background. It is scaled to show source enveloppe amplified according synthetizer input gain setting of the simulation.

#### Pitch detection on sequence with a piano tone generated from midi file

<img src="images/benchmark-sequence-piano.png"/>

We observe errors at note start and end but pitch detection is pretty stable in the middle of each note.

Error at note end is discutable : alpha of the point is defined by amplitude, and there is no clear understanding of WHEN the sound actually stops, so such error should be investigated with audio.


#### Pitch detection on sequence with a Fender Rhode tone generated from midi file

<img src="images/benchmark-sequence-rhode.png"/>

Contrary to previous chart :
* there is latency on note onset, but no wrong pitch detection
* there is real pitch detection error at note offset, whereas pitch detection was still succeeding on piano note release.

#### Pitch detection on sequence with a voice tone singed according to piano tone

<img src="images/benchmark-sequence-voice.png"/>

#### On overdrived voice
<img src="images/benchmark-sequence-voice-overdrive.png"/>
