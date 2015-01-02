#VOCOBOX

Voice controller for digital instruments

<script src="doc/scripts/mermaid.full.min.js"></script>

## Description

Vocobox intend to provide singers with a software able to turn their voice to a musical controller. Voice features (pitch, volume, ...) are used to control external software or hardware producing music.

###### VOCOBOX 1.0 (01/01/2015)

At this step we are mainly evaluating pitch detection algorithms using the <a href="https://github.com/vocobox/human-voice-dataset">Human Voice Dataset</a>, a dataset containing notes sung by a singer. We define score such as note onset latency, pitch detection latency and <a href="https://github.com/vocobox/vocobox/blob/master/Benchmark.md">compare samples scores with each other</a>.

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
    <th>Output</th>
    <td><a href="doc/audio/doremi-piano-out.wav">Do-re-mi synth controlled by piano</a></td>
    <td><a href="doc/audio/doremi-voice-out.wav">Do-re-mi synth controlled by voice</a></td>
  </tr>
  <tr>
    <th>Chart</th>
    <td><img src="doc/audio/doremi-piano.png"/></td>
    <td><img src="doc/audio/doremi-voice.png"/></td>
  </tr>
</table>

See this <a href="http://doc.jzy3d.org/vocobox/examples/">examples</a> folder for more input/output/chart results.

To run synthetizer control based on a wav file, see <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/wav2synth/VocoboxControllerFileRead.java">VocoboxControllerFileRead</a>.


#### Controlling Synthetizers in live with available audio inputs (microphone,  lines)

When starting the application, the list of available source are listed by tarsos, and an estimation algorithm is proposed. We found Yin performs best. Running live synthetizer control allows to see pitch detection is pretty efficient.

<img src="doc/images/mic2synth.png"/>

To run synthetizer control based on live voice, see <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/mic2synth/VocoboxControllerMic.java">VocoboxControllerMic</a>

#### Benchmark Pitch Detection algorithm on note datasets

This <a href="Benchmark.md">document</a> explain how we use the <a href="https://github.com/vocobox/human-voice-dataset">Human Voice Dataset</a> (a serie of wav files containing human sung notes) to evaluate pitch detection algorithm on isolated notes.

## Components

### Synthetizers

Synthetizer are powered by <a href="https://github.com/philburk/jsyn">JSyn</a>. Our synthetizer implementations are really dead simple. All the work is actually made by JSyn.

<table>
<tr>
<th>Synthetizer</th>
<th>Comment</th>
</tr>

<tr>
<td>JsynMonoscilloSynth</td>
<td>A single oscillator.</td>
</tr>

<tr>
<td>JsynMonoscilloRampSynth</td>
<td>A single oscillator having a LinearRamp on frequency and amplitude change commands, handling numerous pitch / amplitude change events without audio artifact.</td>
</tr>

<tr>
<td>JsynOcclusiveNoiseSynth</td>
<td>A synthetizer using non frequency-defined sounds (here : white noise) when confidence parameter is below a certain threshold. It allows a kind of audio debugging of pitch detection. Brutal tone change make the synthetizer not usable musically but smooth changes in tone balance could make interesting effects.</td>
</tr>

<tr>
<td>JsynCircuitSynth</td>
<td>A synthetizer based on JSyn Circuit, allowing to more easily abstraction and composition of synthetizer elements. Here, we use circuit SynthCircuitBlaster that is derived from JSyn examples. Note the circuit provides its control panel to Vocobox UI.</td>
</tr>


<tr>
<td>JsynOscilloSpectroHarpSynth</td>
<td>An experimental synthetizer based on FFT analysis of a file. A file is played, its FFT is processed, and all frequency band energies defines amplitude of one the 93 oscillators covering 0-4kHz.</td>
</tr>

</table>

### Audio analysis

Audio signal analysis is powered by <a href="https://github.com/JorenSix/TarsosDSP">TarsosDSP</a>. Yin implementation outperforms any other algorithm for pitch detection.

Vocobox delivers pitch detection through following analyzers

<table>
  <tr>
    <th>Analyzer</th>
    <th>Comment</th>
    </tr>
  <tr>
    <td>VoiceMicListen</td>
    <td>Analyse audio signal from available inputs (microphones, but also lines, etc). <font color="orange">When running a Jack server, audio sources made available by Jack appear in source list!</font>
    </td>
  </tr>
  <tr>
  <td>VoiceFileRead</td>
  <td>Analyse audio signal from (mono) wav files. After reading, a collection of audio analysis events are collected an can be send to a synthetizer.</td>
  </tr>
</table>

### Charts

Charts are powered by <a href="https://github.com/jzy3d/jzy3d-api">Jzy3d</a>. Vocobox has drived Jzy3d improvements on 2D charts (primitives, time charts, etc).

Charts are used as synthetizer command logs : parameter changes of the synthetizer are tracked and mapped to multiple charts.


<table>
  <tr>
    <th>Chart</th>
    <th>Comment</th>
    </tr>
  <tr>
    <td>Frequency chart</td>
    <td>Shows the synthetizer frequency changes with a pink scatter plot. Confidence is used to define alpha, so there is nothing displayed if pitch detection has confidence 0.</td>
  </tr>
  <tr>
    <td>Amplitude chart</td>
    <td>Shows the synthetizer amplitude changes with a cyan scatter plot. Amplitude events below the note relevance threshold (default 0.1) are drawn in gray.</td>
  </tr>
  </table>

Few features interesting with Jzy3d
* easy charting
* performance and liveness
* <font color="orange">coming soon : log chart</font> will help to let frequency charts look like note charts without having to do the conversion by ourself.
* underlying JOGL make it 4*4 (any Java Windowing toolkit including Android)


## Getting and building source code

```
git clone https://github.com/vocobox/human-voice-dataset
git clone https://github.com/vocobox/vocobox
cd vocobox/dev/java
mvn clean install
```

## Contributing
Please join us and share your contributions through <a href="https://help.github.com/articles/using-pull-requests/">pull-requests</a>


## Licensing
<span style="color:red;">
IF YOU INTEND TO REUSE THIS SOFTWARE, PLEASE VERIFY COMPONENTS LICENCE!
</span>


* <a href="https://github.com/vocobox/vocobox/blob/master/LICENSE">Vocobox</a>
* <a href="http://www.softsynth.com/jsyn/developers/">JSyn</a>
* <a href="https://github.com/JorenSix/TarsosDSP/blob/master/license.txt">Tarsos</a>
* <a href="https://github.com/jzy3d/jzy3d-api/blob/master/jzy3d-api/license.txt">Jzy3d</a>
