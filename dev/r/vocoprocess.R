analysis <-function(file, folder, song, show=FALSE){
  wave <- readWave(file)
  
  folder = paste(folder, song, '/', sep='');
  dir.create(folder)
  name = paste(folder, song, sep='');
  
  compPitch = TRUE
  compAmp = FALSE
  compEnv = TRUE
  
  # PITCH
  if(compPitch){
    pitch = aubiopitch(file, paste("-p", aubio.pitch.p[6]))#, "-s", -30)) # 4 mcomb 6 yinfft
    write.csv(pitch, file = paste(name, "-pitch.csv", sep=''), quote = FALSE, row.names=FALSE)
  }
  # TODO : LIMIT CONFIDENCE WITH -l 0.5
  
  # AMPLITUDE
  if(compAmp){
    #nsamples = 10000
    #ampli = amplitudeSimplify(wave, nsamples)
    ampli = amplitude(wave)
    write.csv(ampli, file = paste(name, "-amplitude.csv", sep=''), quote = FALSE, row.names=FALSE)
  }
  
  # ENVELOPPE
  if(compEnv){
    ## ATTENTION, ON N A PAS AUTANT DE SAMPLE QUE LE SON BRUT
    # CF PARAM DE SMOOTH
    smoothFactor = 20;
    envelopeRaw = env(wave, msmooth=c(smoothFactor,0), plot=FALSE)
    
    # normalize envelope
    me = max(envelopeRaw)
    envelope = sqrt(2*envelopeRaw/me)
    #print(me)
    
    
    #timedEnvelope = formatTime(addTime(computeTime(wave, nsamples=length(envelope)), envelope))
    
    ## SUBSAMPLE BUGGE!!??
    nsamples = length(pitch[,1])/1; # wish approx same number of samples than pitch
    timedEnvelope = subsample(envelope, 44100, nsamples);
    timedEnvelope[,1] = timedEnvelope[,1] * smoothFactor;
    timedEnvelope = formatTime(timedEnvelope);
    #head(timedEnvelope)
    
    write.csv(timedEnvelope, file = paste(name, "-envelope.csv", sep=''), quote = FALSE, row.names=FALSE)    
  }
  
  #SHOW
  if(show){
#    plot(ampli, type='l')
#    oscillo(wavePiano)
    png(filename = paste(name, ".png", sep=''), width = 800, height = 600, bg = "white")
    par(mfcol=c(3,1))
    plot(pitch, type='p', col='blue')
    plot(timedEnvelope, type='l', col='red')
    plot(envelopeRaw, type='l', col='red')
    dev.off()
    
    #   plot(pitch[250:400,], type='p')
    #  plot(amplitudeSummary, type='l')    
  }
  
}

showFilter <- function(formula, x=seq(0,1,0.01)){
  data <- cbind(x,formula(x))
  plot(data)  
}

# ----------------------
# FREQUENCY FILTER

ignoreFreq <- function(pitch, fromFrq, toFrq, show=FALSE){
  # ignore freq out of range
  freqRangeHz = c(note.C0, note.C4)
  freqRangeKHz = freqRangeHz/1000
  
  pitchFilter = filterFreqRange(pitch, freqRangeHz)
  if(show){
    plot(pitch, type='l', col='red')#'p' 
    lines(pitchFilter, col='blue')#'p' 
  }
  return(pitchFilter)
}

ignoreTime <- function(pitch, fromSecond, toSecond){
  # ignore freq out of range
  timeRangeS = c(0,1)
  
  pitchFilter = filterTimeRange(pitchFilter, timeRangeS)
  if(show)
    plot(pitchFilter, type='l')#'p' 
  return(pitchFilter)
}

# ----------------------
# AMPLITUDE

amplitude <- function(wave, nsamples){
  input<-inputw(wave=wave,f=f) ; wave<-input$w ; f<-input$f ; rm(input)
  
  from = NULL
  to = NULL
  if(is.null(from) && is.null(to)) {a<-0; b<-length(wave); from<-0; to<-length(wave)/f}
  if(is.null(from) && !is.null(to)) {a<-1; b<-round(to*f); from<-0}
  if(!is.null(from) && is.null(to)) {a<-round(from*f); b<-length(wave); to<-length(wave)/f}
  if(!is.null(from) && !is.null(to))
  {
    if(from>to) stop("'from' cannot be superior to 'to'")
    if(from==0) {a<-1} else {a<-round(from*f)}
    b<-round(to*f)
  }
  amplitude<-as.matrix(wave[a:b,])
  n<-nrow(amplitude)
  
  # subsample ampltude to ensure no more events than nsamples
  
  amplitudeOut = addTime(computeTime(amplitude, f), amplitude);  
  return(formatTime(amplitudeOut))
}


# Abs, subsample
amplitudeSimplify <- function(wave, nsamples){
  input<-inputw(wave=wave,f=f) ; wave<-input$w ; f<-input$f ; rm(input)
  
  from = NULL
  to = NULL
  if(is.null(from) && is.null(to)) {a<-0; b<-length(wave); from<-0; to<-length(wave)/f}
  if(is.null(from) && !is.null(to)) {a<-1; b<-round(to*f); from<-0}
  if(!is.null(from) && is.null(to)) {a<-round(from*f); b<-length(wave); to<-length(wave)/f}
  if(!is.null(from) && !is.null(to))
  {
    if(from>to) stop("'from' cannot be superior to 'to'")
    if(from==0) {a<-1} else {a<-round(from*f)}
    b<-round(to*f)
  }
  wave<-as.matrix(wave[a:b,])
  n<-nrow(wave)
  amplitude <- abs(wave)
  
  # subsample ampltude to ensure no more events than nsamples
  amplitudeOut <- subsample(amplitude, f, nsamples);
  return(formatTime(amplitudeOut))
}

# ----------------
# 

subsample <- function(waveMatrix, f, nsamples){
  selection = seq(from=1, to=length(waveMatrix), by=length(waveMatrix)/nsamples)
  amplitudeSummary = waveMatrix[selection]
  elapsed = selection / f
  
#print(selection)
#  print(elapsed)
  return(addTime(elapsed, amplitudeSummary))
}

# ----------------------
# ELAPSED TIME HELPERS

computeTime <- function(waveMatrix, f, nsamples=-1){
  input<-inputw(wave=waveMatrix,f=f) ; 
  wave<-input$w ; 
  f<-input$f ; 
  rm(input);

  if(nsamples<=0){
    selection = seq(from=1, to=length(amplitude))
    elapsed = selection / f
    return( elapsed );    
  }
  else{
    #f = inputw(wave=waveMatrix, f=f)$f
    selection = seq(from=1, to=length(waveMatrix), by=length(waveMatrix)/nsamples)
    elapsed = selection / f
    return(elapsed);
  }
}

addTime <- function (elapsed, amplitudeSummary) {
  amplitudeOut = cbind(elapsed, amplitudeSummary)
}

formatTime <- function(amplitudeOut){
  ao = data.frame(amplitudeOut)
  ao[,1] <- format(ao[,1], scientific = FALSE)
  return(ao)
}


