package com.beepcounter.app;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class BeepDetector {
    private static final String TAG = "BeepDetector";
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    
    // Beep detection parameters
    private static final int MIN_BEEP_FREQ = 1000;  // 1 kHz
    private static final int MAX_BEEP_FREQ = 4000;  // 4 kHz
    private static final double AMPLITUDE_THRESHOLD = 0.15;  // Amplitude threshold for beep detection
    private static final int MIN_BEEP_DURATION_MS = 50;  // Minimum beep duration
    private static final int MAX_BEEP_GAP_MS = 200;  // Maximum gap between beeps to count as one
    
    private AudioRecord audioRecord;
    private Thread recordingThread;
    private boolean isRecording = false;
    private BeepListener listener;
    
    // Beep detection state
    private boolean inBeep = false;
    private long lastBeepEndTime = 0;
    private long beepStartTime = 0;
    
    public interface BeepListener {
        void onBeepDetected();
    }
    
    public BeepDetector(BeepListener listener) {
        this.listener = listener;
    }
    
    public boolean start() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        if (bufferSize == AudioRecord.ERROR_BAD_VALUE || bufferSize == AudioRecord.ERROR) {
            Log.e(TAG, "Invalid buffer size");
            return false;
        }
        
        try {
            audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT,
                    bufferSize * 2
            );
            
            if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                Log.e(TAG, "AudioRecord initialization failed");
                return false;
            }
            
            audioRecord.startRecording();
            isRecording = true;
            
            recordingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    processAudio();
                }
            });
            recordingThread.start();
            
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error starting audio recording", e);
            return false;
        }
    }
    
    public void stop() {
        isRecording = false;
        
        if (audioRecord != null) {
            try {
                if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecord.stop();
                }
                audioRecord.release();
            } catch (Exception e) {
                Log.e(TAG, "Error stopping audio recording", e);
            }
            audioRecord = null;
        }
        
        if (recordingThread != null) {
            try {
                recordingThread.join(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error joining recording thread", e);
            }
            recordingThread = null;
        }
    }
    
    private void processAudio() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        short[] audioBuffer = new short[bufferSize];
        
        while (isRecording && audioRecord != null) {
            int samplesRead = audioRecord.read(audioBuffer, 0, bufferSize);
            
            if (samplesRead > 0) {
                detectBeep(audioBuffer, samplesRead);
            }
        }
    }
    
    private void detectBeep(short[] samples, int length) {
        // Calculate RMS amplitude
        double rms = calculateRMS(samples, length);
        
        // Check if amplitude is above threshold
        boolean aboveThreshold = rms > AMPLITUDE_THRESHOLD;
        
        // Simple frequency analysis - check if dominant frequency is in beep range
        boolean inBeepRange = checkFrequencyRange(samples, length);
        
        long currentTime = System.currentTimeMillis();
        
        if (aboveThreshold && inBeepRange) {
            if (!inBeep) {
                // Start of a potential beep
                inBeep = true;
                beepStartTime = currentTime;
            }
        } else {
            if (inBeep) {
                // End of beep
                long beepDuration = currentTime - beepStartTime;
                
                if (beepDuration >= MIN_BEEP_DURATION_MS) {
                    // Valid beep detected
                    long timeSinceLastBeep = currentTime - lastBeepEndTime;
                    
                    // Only count if enough time has passed since last beep
                    if (timeSinceLastBeep > MAX_BEEP_GAP_MS || lastBeepEndTime == 0) {
                        if (listener != null) {
                            listener.onBeepDetected();
                        }
                    }
                    
                    lastBeepEndTime = currentTime;
                }
                
                inBeep = false;
            }
        }
    }
    
    private double calculateRMS(short[] samples, int length) {
        long sum = 0;
        for (int i = 0; i < length; i++) {
            sum += (long) samples[i] * samples[i];
        }
        double mean = (double) sum / length;
        return Math.sqrt(mean) / Short.MAX_VALUE;  // Normalize to 0-1 range
    }
    
    private boolean checkFrequencyRange(short[] samples, int length) {
        // Simple zero-crossing rate and amplitude check
        // For a more accurate detection, you could implement FFT here
        
        // Count zero crossings as a simple frequency indicator
        int zeroCrossings = 0;
        for (int i = 1; i < length; i++) {
            if ((samples[i] >= 0 && samples[i - 1] < 0) || 
                (samples[i] < 0 && samples[i - 1] >= 0)) {
                zeroCrossings++;
            }
        }
        
        // Estimate frequency from zero crossings
        // Each zero crossing represents half a cycle
        double estimatedFreq = (zeroCrossings / 2.0) * (SAMPLE_RATE / (double) length);
        
        return estimatedFreq >= MIN_BEEP_FREQ && estimatedFreq <= MAX_BEEP_FREQ;
    }
}

