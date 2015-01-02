package org.vocobox.model.synth;

public class SynthMonitorDefault implements SynthMonitor {

	@Override
	public void start() {
	}

	@Override
	public void pitchConfidenceChangedAt(double time, float confidence,
			float pitch) {
	}

	@Override
	public void pitchConfidenceChanged(float value) {
	}

	@Override
	public void pitchChanged(float value, Object info) {
	}

	@Override
	public void pitchChanged(float value) {
	}

	@Override
	public void pitchChangeAt(double time, float value, Object info) {
	}

	@Override
	public void pitchChangeAt(double time, float value) {
	}

	@Override
	public void onsetOccuredAt(double time, float salience) {
	}

	@Override
	public void onsetOccured(float salience) {
	}

	@Override
	public void offsetOccured() {
	}

	@Override
	public void midiVolume(int nChannel, int volume) {
	}

	@Override
	public void midiPitchBend(int nChannel, int value) {
	}

	@Override
	public void midiNoteOn(int nChannel, int nKey, int nVelocity) {
	}

	@Override
	public void midiNoteOff(int nChannel, int nKey) {
	}

	@Override
	public void amplitudeChanged(float value) {
	}

	@Override
	public void amplitudeChangeAt(double time, float value) {
	}
}
