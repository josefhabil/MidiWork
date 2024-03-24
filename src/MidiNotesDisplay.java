import javax.sound.midi.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MidiNotesDisplay {

    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;

    public static void displayRecordedNotes(Sequence sequence) {
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ":");

            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;

                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = noteNames[note];
                        int velocity = sm.getData2();


                        System.out.println("Note On, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = noteNames[note];
                        // You can add logic here to calculate the duration if needed
                        System.out.println("Note Off, " + noteName + octave + " key=" + key);
                    }
                }
            }

        }
    }
    public static void extractAndSaveMidiData(Sequence sequence, String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath, false)) {
            int trackNumber = 0;
            for (Track track : sequence.getTracks()) {
                trackNumber++;
                writer.write("Track " + trackNumber + ":\n");

                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int noteIndex = key % 12;
                        String noteName = noteNames[noteIndex];

                        if (sm.getCommand() == ShortMessage.NOTE_ON) {
                            int velocity = sm.getData2();
                            writer.write(String.format("Note On, %s%d, Key=%d, Velocity=%d\n", noteName, octave, key, velocity));
                        } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                            writer.write(String.format("Note Off, %s%d, Key=%d\n", noteName, octave, key));
                        }
                    }
                }
                writer.write("\n"); // Add a new line for readability between tracks
            }
            System.out.println("MIDI data has been successfully written to " + outputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }


    private static final String[] noteNames = {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
    };

    public static void main(String[] args) throws InvalidMidiDataException, IOException {
        String midiFilePath = "C:\\Users\\josef\\Desktop\\MIDIProject\\student_track1.mid";
        String outputFilePath = "C:\\Users\\josef\\Desktop\\MIDIProject\\midi_notes_data2.txt"; // The path where you want to save the extracted data

        Sequence sequence = MidiSystem.getSequence(new File(midiFilePath));
        displayRecordedNotes(sequence);
        extractAndSaveMidiData(sequence,outputFilePath);
    }
}
