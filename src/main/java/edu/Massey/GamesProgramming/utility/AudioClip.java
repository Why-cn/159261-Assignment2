package edu.Massey.GamesProgramming.utility;

import javax.sound.sampled.*;
import java.io.File;

/*
Sound class
 */

public class AudioClip {
    // Format
    AudioFormat mFormat;
    // Audio Data
    byte[] mData;
    // Buffer Length
    long mLength;
    // Loop Clip
    Clip mLoopClip;

    public Clip getLoopClip() {
        // return mLoopClip
        return mLoopClip;
    }

    public void setLoopClip(Clip clip) {
        // Set mLoopClip to clip
        mLoopClip = clip;
    }

    public AudioFormat getAudioFormat() {
        // Return mFormat
        return mFormat;
    }

    public byte[] getData() {
        // Return mData
        return mData;
    }

    public long getBufferSize() {
        // Return mLength
        return mLength;
    }

    public AudioClip(AudioInputStream stream) {
        // Get Format
        mFormat = stream.getFormat();
        // Get length (in Frames)
        mLength = stream.getFrameLength() * mFormat.getFrameSize();
        // Allocate Buffer Data
        mData = new byte[(int) mLength];
        try {
            // Read data
            stream.read(mData);
        } catch (Exception exception) {
            // Print Error
            System.out.println("Error reading Audio File\n");
            // Exit
            System.exit(1);
        }
        // Set LoopClip to null
        mLoopClip = null;
    }

    // Loads the AudioClip stored in the file specified by filename
    public static AudioClip loadAudio(String filename) {
        try {
            // Open File
            File file = new File(filename);
            // Open Audio Input Stream
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            // Create Audio Clip
            AudioClip clip = new AudioClip(audio);
            // Return Audio Clip
            return clip;
        } catch (Exception e) {
            // Catch Exception
            System.out.println("Error: cannot open Audio File " + filename + "\n");
        }
        // Return Null
        return null;
    }

    // Plays an AudioClip
    public static void playAudio(AudioClip audioClip) {
        // Check audioClip for null
        if (audioClip == null) {
            // Print error message
            System.out.println("Error: audioClip is null\n");
            // Return
            return;
        }
        try {
            // Create a Clip
            Clip clip = AudioSystem.getClip();
            // Load data
            clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int) audioClip.getBufferSize());
            // Play Clip
            clip.start();
        } catch (Exception exception) {
            // Display Error Message
            System.out.println("Error playing Audio Clip\n");
        }
    }

    // Plays an AudioClip with a volume in decibels
    public static void playAudio(AudioClip audioClip, float volume) {
        // Check audioClip for null
        if (audioClip == null) {
            // Print error message
            System.out.println("Error: audioClip is null\n");
            // Return
            return;
        }
        try {
            // Create a Clip
            Clip clip = AudioSystem.getClip();
            // Load data
            clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int) audioClip.getBufferSize());
            // Create Controls
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Set Volume
            control.setValue(volume);
            // Play Clip
            clip.start();
        } catch (Exception exception) {
            // Display Error Message
            System.out.println("Error: could not play Audio Clip\n");
        }
    }

    // Starts playing an AudioClip on loop
    public static void startAudioLoop(AudioClip audioClip) {
        // Check audioClip for null
        if (audioClip == null) {
            // Print error message
            System.out.println("Error: audioClip is null\n");
            // Return
            return;
        }
        // Get Loop Clip
        Clip clip = audioClip.getLoopClip();
        // Create Loop Clip if necessary
        if (clip == null) {
            try {
                // Create a Clip
                clip = AudioSystem.getClip();
                // Load data
                clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int) audioClip.getBufferSize());
                // Set Clip to Loop
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                // Set Loop Clip
                audioClip.setLoopClip(clip);
            } catch (Exception exception) {
                // Display Error Message
                System.out.println("Error: could not play Audio Clip\n");
            }
        }
        // Set Frame Position to 0
        clip.setFramePosition(0);
        // Start Audio Clip playing
        clip.start();
    }

    // Starts playing an AudioClip on loop with a volume in decibels
    public static void startAudioLoop(AudioClip audioClip, float volume) {
        // Check audioClip for null
        if (audioClip == null) {
            // Print error message
            System.out.println("Error: audioClip is null\n");
            // Return
            return;
        }
        // Get Loop Clip
        Clip clip = audioClip.getLoopClip();
        // Create Loop Clip if necessary
        if (clip == null) {
            try {
                // Create a Clip
                clip = AudioSystem.getClip();
                // Load data
                clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int) audioClip.getBufferSize());
                // Create Controls
                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Set Volume
                control.setValue(volume);
                // Set Clip to Loop
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                // Set Loop Clip
                audioClip.setLoopClip(clip);
            } catch (Exception exception) {
                // Display Error Message
                System.out.println("Error: could not play Audio Clip\n");
            }
        }
        // Set Frame Position to 0
        clip.setFramePosition(0);
        // Start Audio Clip playing
        clip.start();
    }

    // Stops an AudioClip playing
    public static void stopAudioLoop(AudioClip audioClip) {
        // Get Loop Clip
        Clip clip = audioClip.getLoopClip();
        // Check clip is not null
        if (clip != null) {
            // Stop Clip playing
            clip.stop();
        }
    }
}





