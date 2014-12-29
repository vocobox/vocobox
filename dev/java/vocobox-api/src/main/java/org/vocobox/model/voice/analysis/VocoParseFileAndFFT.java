package org.vocobox.model.voice.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.bluecow.spectro.Clip;
import net.bluecow.spectro.Frame;

import org.jzy3d.maths.Coord3d;
import org.vocobox.model.song.Song;

public class VocoParseFileAndFFT implements VocoParse {
    Clip clip;

    public VocoParseFileAndFFT(File file) throws UnsupportedAudioFileException, IOException {
        clip = Clip.newInstance(file);
    }

    @Override
    public Clip getAnalysis() {
        return clip;
    }
    
    public List<Coord3d> toCoord3d() {
        int frameCount = clip.getFrameCount();
        int frameDimensions = clip.getFrame(0).getLength();
        List<Coord3d> coords = new ArrayList<Coord3d>(frameCount*frameDimensions);
        
        for (int frameId = 0; frameId < frameCount; frameId++) {
            Frame frame = clip.getFrame(frameId);
            
            for (int frameDimKey = 0; frameDimKey < frameDimensions; frameDimKey++) {
                double frameDimVal = frame.getReal(frameDimKey);
                //System.out.println(frameDimVal + " ");
                coords.add(new Coord3d(frameId,frameDimKey, frameDimVal));
            }
        }
        return coords;
    }

    public static void toCoord3d(int frameId, Frame frame) {
        int frameDimensions = frame.getLength();

        System.out.print(frameId + "] [" + frameDimensions + "]  ");

        for (int frameDimKey = 0; frameDimKey < frameDimensions; frameDimKey++) {
            double frameDimVal = frame.getReal(frameDimKey);
            System.out.print(frameDimVal + " ");
        }
        System.out.println(frame);
    }

    public void toConsole() {
        toConsole(clip);
    }
    
    public static void toConsole(Clip clip) {
        int frameCount = clip.getFrameCount();
        for (int frameId = 0; frameId < frameCount; frameId++) {
            Frame frame = clip.getFrame(frameId);
            toConsole(frameId, frame);
        }
    }

    public static void toConsole(int frameId, Frame frame) {
        int frameDimensions = frame.getLength();

        System.out.print(frameId + "] [" + frameDimensions + "]  ");

        for (int frameDimKey = 0; frameDimKey < frameDimensions; frameDimKey++) {
            double frameDimVal = frame.getReal(frameDimKey);
            System.out.print(frameDimVal + " ");
        }
        System.out.println(frame);
    }

    @Override
    public Song computeSong() {
        // TODO Auto-generated method stub
        return null;
    }
}
