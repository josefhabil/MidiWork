import javax.sound.midi.*;

public class MidiRecorder {

    public static void main(String[] args) {
        try {
            MidiDevice keyboardDevice = MidiDeviceSelector.getKeyboardDevice();
            if (keyboardDevice == null) {
                System.out.println("MIDI keyboard not found.");
                return;
            }
            keyboardDevice.open(); // Open the device to start listening for MIDI data

            Sequencer sequencer = MidiSystem.getSequencer(false); // Get a sequencer without a default device
            sequencer.open();
            sequencer.setSequence(new Sequence(Sequence.PPQ, 24));

            Transmitter transmitter = keyboardDevice.getTransmitter();
            Receiver receiver = sequencer.getReceiver();
            transmitter.setReceiver(receiver); // Connect the keyboard to the sequencer


            sequencer.recordEnable(sequencer.getSequence().createTrack(), -1);

            sequencer.startRecording();
            System.out.println("Recording... Press ENTER to stop.");


            System.in.read();


            sequencer.stopRecording();
            // Save the recorded sequence to a MIDI file
            int[] fileTypes = MidiSystem.getMidiFileTypes(sequencer.getSequence());

            if (fileTypes.length > 0) {
                MidiSystem.write(sequencer.getSequence(), fileTypes[0], new java.io.File("sample_track1.mid"));
                System.out.println("MIDI file saved as recorded_track.mid");
            } else {
                System.out.println("No valid MIDI file types available for saving.");
            }

            // Cleanup

            transmitter.close();
            receiver.close();
            sequencer.close();
            keyboardDevice.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
