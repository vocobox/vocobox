# ----------------
## Seewave : Dominant frequency  
spectro(waveDoremi, ovlp=75, zp=8, palette=rev.gray.colors.1, scale=FALSE, flim=freqRangeKHz)
par(new=TRUE)
#dfreq(waveDoremi, ovlp=50, threshold=6, type="l", col="red", lwd=2)

## Seewave : Instantaneous frequency
ifreq(waveDoremi, threshold=6, col="darkviolet", main="Instantaneous frequency with Hilbert transform")

## Seewave : Spectrogram
spectro(waveDoremi, ovlp=50, zp=16, collevels=seq(-40,0,0.5), flim=freqRangeKHZ)
