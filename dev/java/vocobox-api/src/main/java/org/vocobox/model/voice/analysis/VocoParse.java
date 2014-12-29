package org.vocobox.model.voice.analysis;

import net.bluecow.spectro.Clip;

import org.vocobox.model.song.Song;

public interface VocoParse {
    public Clip getAnalysis();
    public Song computeSong();
}
