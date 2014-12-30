#VOCOBOX

Voice controller for digital instruments

<script src="doc/scripts/mermaid.full.min.js"></script>

### INTRO

Vocobox intend to provide singers with a software able to convert and send voice events to control external software or hardware producing music.

####### SCHEMA
SINGER note, amplitude, formant -> SYNTHETISER CONTROLER -> SYNTHETIZER: midi, emulated

###### Vocobox 1.0 (01/01/2015)

At this step we are evaluating pitch detection algorithms using the <a href="https://github.com/vocobox/human-voice-dataset">Human Voice Dataset</a>, a dataset containing notes sung by singer.

We define score such as note onset latency, pitch detection latency and compare samples scores with each other.

We also evaluate pitch detection in live using microphone.

To build Vocobox, we gathered :
* a full java additive synthetizer made by chaining oscillators, filters, and other hardware emulation of real synthetizer components. It is build <a href="http://www.softsynth.com/jsyn/">JSyn</a>.
* a pitch detection module based on the excellent <a href="https://github.com/JorenSix/TarsosDSP">TarsosDSP</a> java library.
* efficient OpenGL charts for monitoring and debugging the sound data workflow built with <a href="http://www.jzy3d.org/">Jzy3d</a> 2d charts.




### Running Vocobox

#### Getting and building source code

```
git clone https://github.com/vocobox/human-voice-dataset
git clone https://github.com/vocobox/vocobox
cd vocobox/dev/java
mvn clean install
```

#### Running applications

Folder vocobox/dev/java/vocobox-apps provides several applications

##### Controlling Synthetizers with CSV files

Our first attempt to analyze voice signal was <a href="https://github.com/vocobox/vocobox/blob/master/dev/r">written in R</a> using <a href="http://rug.mnhn.fr/seewave/">Seewave</a> and <a href="http://aubio.org/">Aubio</a> via an <a href="https://github.com/vocobox/aubio-r/">R binding</a> written for the experiment.

To control JSyn (java) synthetizer, we export frequency and amplitude change commands in two CSV files. Each file contains two columns, the first being elapsed time since song start, the second indicating a value change (frequency changes for pitch.csv, and amplitude changes for envelope.csv). Note that frequency and amplitude can change independently.

Having the original wav file available allows to play in background while executing command events.

##### Controlling Synthetizers with WAV files

To run synthetizer control based on a wav file, see <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/wav2synth/VocoboxControllerFilePlay.java">VocoboxControllerFilePlay</a> and <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/wav2synth/VocoboxControllerFileRead.java">VocoboxControllerFileRead</a>


##### Controlling Synthetizers with Microphone

To run synthetizer control based on live voice, see <a href="https://github.com/vocobox/vocobox/blob/master/dev/java/vocobox-apps/src/main/java/org/vocobox/apps/mic2synth/VocoboxControllerMic.java">VocoboxControllerMic</a>

##### Benchmark Pitch Detection algorithm on Human Voice Dataset

Read this <a href="Benchmark.md">document</a>.


### Contributing
Please join us and share your contributions through <a href="https://help.github.com/articles/using-pull-requests/">pull-requests</a>


### LICENSING
<span style="color:red;">
IF YOU INTEND TO REUSE THIS SOFTWARE WITH VOCOBOX, PLEASE READ THIS!
</span>


* Vocobox API : MIT License
* Vocobox JSyn : Jsyn <a href="http://www.softsynth.com/jsyn/developers/">License</href>
* Vocobox Tarsos : Tarsos is licensed under <a href="https://github.com/JorenSix/TarsosDSP/blob/master/license.txt">GPL</a>
