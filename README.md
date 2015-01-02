#VOCOBOX

Voice controller for digital instruments

<script src="doc/scripts/mermaid.full.min.js"></script>

## Description

Vocobox intend to provide singers with a software able to turn their voice to a musical controller. Voice features (pitch, volume, ...) are used to control external software or hardware producing music.

###### Vocobox 1.0 (01/01/2015)

At this step we are evaluating pitch detection algorithms using the <a href="https://github.com/vocobox/human-voice-dataset">Human Voice Dataset</a>, a dataset containing notes sung by a singer. We define score such as note onset latency, pitch detection latency and <a href="https://github.com/vocobox/vocobox/blob/master/Benchmark.md">compare samples scores with each other</a>.

We also evaluate pitch detection <i>in live</i> by recording the voice with a microphone, and by generating a feedback sound sounds with a synthetizer controlled according to voice analysis.

To build Vocobox, we gathered :
* a full java additive synthetizer made by chaining oscillators, filters, and other hardware emulation of real synthetizer components based on <a href="http://www.softsynth.com/jsyn/">JSyn</a>. As of Jan. 2015, JSyn has become an <a href="https://github.com/philburk/jsyn">open source project</a>.
* a pitch detection module based on <a href="https://github.com/JorenSix/TarsosDSP">TarsosDSP</a> java library.
* efficient OpenGL charts for monitoring and debugging sound data based on <a href="http://www.jzy3d.org/">Jzy3d</a> charts.




## Applications

Folder vocobox/dev/java/vocobox-apps provides several applications

#### Controlling Synthetizers with CSV files

Our first attempt to analyze voice signal was <a href="https://github.com/vocobox/vocobox/blob/master/dev/r">written in R</a> using <a href="http://rug.mnhn.fr/seewave/">Seewave</a> and <a href="http://aubio.org/">Aubio</a> via an <a href="https://github.com/vocobox/aubio-r/">R binding</a> written for the experiment.

To control JSyn (java) synthetizer, we export frequency and amplitude change commands in two CSV files. Each file contains two columns, the first being elapsed time since song started, the second indicating a value change (frequency changes for pitch.csv, and amplitude changes for envelope.csv). Note that frequency and amplitude can change independently.

Having the original wav file available allows to play audio source in background while executing command events.

To run synthetizer control based on a csv files, see <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/csv2synth/VocoboxControllerCsv.java">VocoboxControllerCsv</a>.

#### Controlling Synthetizers with WAV files

The pitch and amplitude change events of a wav file are sent to a <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-api/src/main/java/org/vocobox/model/synth/VocoSynth.java">synthetizer</a> via its sendFrequency() / sendAmplitude() methods. In these demonstration, we use <a href="https://github.com/vocobox/vocobox/tree/master/dev/java/vocobox-synth-jsyn/src/main/java/org/vocobox/synth/jsyn">JSyn based synthetizers</a>. As the direct control of oscillator's amplitude from input file is sufficiently good to mimic notes, we do not need additional computation to define note on and note off.

Below are few synthetized sounds and their wave file controller.

<table>
  <tr>
    <th>Input</th>
    <td><a href="doc/audio/doremi-piano-in.wav">Do-re-mi piano source</a></td>
    <td><a href="doc/audio/doremi-voice-in.wav">Do-re-mi voice source</a></td>
  </tr>
  <tr>
    <th>Comment</th>
    <td>truc</td>
    <td>pout</td>
  </tr>
  <tr>
    <th>Output</th>
    <td><a href="doc/audio/doremi-piano-out.wav">Do-re-mi synth tracked by piano</a></td>
    <td><a href="doc/audio/doremi-voice-out.wav">Do-re-mi synth tracked by voice</a></td>
  </tr>
  <tr>
    <th>Chart</th>
    <td><img src="doc/audio/doremi-piano.png"/></td>
    <td><img src="doc/audio/doremi-voice.png"/></td>
  </tr>
</table>

See the <a href="doc/audio">doc/audio</a> folder for other input/output/chart results.



To run synthetizer control based on a wav file, see <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/wav2synth/VocoboxControllerFileRead.java">VocoboxControllerFileRead</a>.


#### Controlling Synthetizers with available audio inputs (microphone, etc)

When starting the application, the list of available source are listed by tarsos, and an estimation algorithm is proposed. We found Yin performs best. Running live synthetizer control allows to see pitch detection is pretty efficient.

<img src="doc/images/mic2synth.png"/>

To run synthetizer control based on live voice, see <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/mic2synth/VocoboxControllerMic.java">VocoboxControllerMic</a>

#### Benchmark Pitch Detection algorithm on note datasets

This <a href="Benchmark.md">document</a> explain how we use the <a href="https://github.com/vocobox/human-voice-dataset">Human Voice Dataset</a> (a serie of wav files containing human sung notes) to evaluate pitch detection algorithm on isolated notes.


## Getting and building source code

```
git clone https://github.com/vocobox/human-voice-dataset
git clone https://github.com/vocobox/vocobox
cd vocobox/dev/java
mvn clean install
```

## Contributing
Please join us and share your contributions through <a href="https://help.github.com/articles/using-pull-requests/">pull-requests</a>


## LICENSING
<span style="color:red;">
IF YOU INTEND TO REUSE THIS SOFTWARE, PLEASE VERIFY COMPONENTS LICENCE!
</span>


* <a href="https://github.com/vocobox/vocobox/blob/master/LICENSE">Vocobox</a>
* <a href="http://www.softsynth.com/jsyn/developers/">JSyn</a>
* <a href="https://github.com/JorenSix/TarsosDSP/blob/master/license.txt">Tarsos</a>
* <a href="https://github.com/jzy3d/jzy3d-api/blob/master/jzy3d-api/license.txt">Jzy3d</a>
