package org.vocobox.events;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class SoundEventReader {
    public static List<SoundEvent> read(String file, SoundEvent.Type type) throws IOException{
        List<SoundEvent> events = new ArrayList<SoundEvent>();

        CSVReader reader = new CSVReader(new FileReader(file));
        String [] nextLine;
        
        while ((nextLine = reader.readNext()) != null) {
            if(!isString(nextLine)){
                SoundEvent e = new SoundEvent(nextLine[0], nextLine[1]);
                e.type = type;
                events.add(e);
            }
        }
        reader.close();
        
        return events;
    }
    
    public static boolean isString(String[] line){
        try{
            Float.parseFloat(line[0]);
            return false;
        }
        catch(Exception e){
            return true;
        }
    }

}
