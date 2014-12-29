setwd("/Users/martin/Dev/vocobox/dev")
source("/Users/martin/Dev/aubio/aubio-r/aubio.R")
source("./vocoprocess.R")
library(seewave)
library(tuneR)
# try /usr/local/Cellar/aubio/0.4.1/bin/

# -----------------------------
# INSTRUMENTS GOAL
#
# 1 - VOCOBASS
# first vocobass on drum can be cool for a demo
# & for a drummer
#
# 2 - VOCOLEAD
#
#

# IDEA : detect percussive sound in voice and trigger / MIX BEATBOX DETECT
# IRCAM : 
# problematique immersion dans l'instrument
# le ventre a t il des vibrations intéressantes à prendre
# 
# objectif :  faire un hardware? ou juste software + usb controllers OR iphone app
#

folderS = "vocobox-java/data/sound/";
folderA = "vocobox-java/data/analyses/";

fileC3 <- paste(folderS, "C3.wav", sep='');

fileDp <- paste(folderS, "dp2.wav", sep='');
fileSinramp <- paste(folderS, "sin-ramp.wav", sep='');
fileBeatbox <- paste(folderS, "beatbox.wav", sep='');
fileDoremi <- paste(folderS, "doremi.wav", sep='');
filePiano <- paste(folderS, "piano.wav", sep='');
fileVoice1 <- paste(folderS, "voice1.wav", sep='');
fileVoice2 <- paste(folderS, "voice2.wav", sep='');
fileVoice3 <- paste(folderS, "voice3.wav", sep='');


#play(a)
analysis(fileDp, folderA, "dp2", show=TRUE)

analysis(fileC3, folderA, "C3", show=TRUE)

analysis(fileDoremi, folderA, "doremi", show=TRUE)

analysis(filePiano, folderA, "piano", show=TRUE)

analysis(fileBeatbox, folderA, "beatbox", show=TRUE)
analysis(fileSinramp, folderA, "sinramp", show=TRUE) # rentrance
analysis(fileVoice1, folderA, "voice1", show=TRUE) 
analysis(fileVoice2, folderA, "voice2", show=TRUE) 
analysis(fileVoice3, folderA, "voice3", show=TRUE) 


# SANDBOX
pitch = aubiopitch(filePiano, paste("-p", aubio.pitch.p[6]))#, "-s", -30)) # 4 mcomb 6 yinfft


##pitchInFreqRange = ignoreFreq(pitch, note.C0, note.B8, TRUE)

for(i in length(pitch)){
  print(i)
}

pitchApplati = pitch
len = dim(pitch)[1]
window = 50
for (i in window:len ) {
  ids = seq(i-window,i)
  pitchApplati[i,2] = median(pitch[ids,2])
}
plot(pitchApplati, type='l')

plot(pitch, type='l')


ignoreTime(pitch, 0, 1, TRUE)

# Analyse a function filtering input signal
showFilter(sqrt) 

