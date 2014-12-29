package com.jsyn.examples.record;

import com.jsyn.devices.AudioDeviceFactory;
import com.jsyn.devices.AudioDeviceManager;

public class ListAudioDevices
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		AudioDeviceManager audioManager = AudioDeviceFactory.createAudioDeviceManager();

		int numDevices = audioManager.getDeviceCount();
		for( int i = 0; i < numDevices; i++ )
		{
			String deviceName = audioManager.getDeviceName( i );
			int maxInputs = audioManager.getMaxInputChannels( i );
			int maxOutputs = audioManager.getMaxInputChannels( i );
			boolean isDefaultInput = (i == audioManager.getDefaultInputDeviceID());
			boolean isDefaultOutput = (i == audioManager.getDefaultInputDeviceID());
			System.out.println( "#" + i + " : " + deviceName);
			System.out.println("  max inputs : " + maxInputs + (isDefaultInput ? "   (default)" : ""));
			System.out.println("  max outputs: " + maxOutputs + (isDefaultOutput ? "   (default)" : ""));
		}

	}

}
