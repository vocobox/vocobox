package org.vocobox.model.synth;

import org.junit.Assert;
import org.junit.Test;

public class TestMonitoredSynth {
	public static float PRECISION = 0.1f;
	
	@Test
	public void amplitudeNeverExceedOne(){
		final int expectedAmplitude = 1;
		
		SynthMonitor monitor = newAssertAmplitudeMonitor(expectedAmplitude);
		MonitoredSynth ms = new MonitoredSynth(monitor);
		ms.sendAmplitude(1.1f);
		ms.sendAmplitude(1.0f);
	}

	@Test
	public void amplitudeGain(){
		final int expectedAmplitude = 1;
		
		SynthMonitor monitor = newAssertAmplitudeMonitor(expectedAmplitude);
		MonitoredSynth ms = new MonitoredSynth(monitor);
		ms.settings.amplitudeGain = 2;
		ms.sendAmplitude(0.5f);
	}

	private SynthMonitor newAssertAmplitudeMonitor(final int amplitude) {
		SynthMonitorDefault monitor = new SynthMonitorDefault() {
			@Override
			public void amplitudeChanged(float value) {
				Assert.assertEquals(amplitude, value, PRECISION);
			}
			@Override
			public void amplitudeChangeAt(double time, float value) {
				Assert.assertEquals(amplitude, value, PRECISION);
			}
		};
		return monitor;
	}
}
