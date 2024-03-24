import javax.sound.midi.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompareMidiFiles {

    public static void main(String[] args) throws Exception {
        String teacherFilePath = "C:\\Users\\josef\\Desktop\\MIDIProject\\teacher_track1.mid";
        String studentFilePath = "C:\\Users\\josef\\Desktop\\MIDIProject\\student_track1.mid";

        Sequence teacherSequence = MidiSystem.getSequence(new File(teacherFilePath));
        Sequence studentSequence = MidiSystem.getSequence(new File(studentFilePath));

        List<Integer> teacherNotes = extractNotes(teacherSequence);
        List<Integer> studentNotes = extractNotes(studentSequence);

        compareNotes(teacherNotes, studentNotes);
    }

    private static List<Integer> extractNotes(Sequence sequence) {
        List<Integer> notes = new ArrayList<>();

        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        notes.add(sm.getData1()); // Adding the note value
                    }
                }
            }
        }

        return notes;
    }

    private static void compareNotes(List<Integer> originalNotes, List<Integer> studentNotes) {
        int correctNotes = 0;
        int missedNotes = 0;
        int extraNotes = 0;

        // Assuming both lists are sorted based on when the note was played.
        // This is a simplified comparison that doesn't take timing or sequence into account.
        for (Integer note : studentNotes) {
            if (originalNotes.contains(note)) {
                correctNotes++;
            } else {
                extraNotes++;
            }
        }

        missedNotes = originalNotes.size() - correctNotes;

        System.out.println("Correct Notes: " + correctNotes);
        System.out.println("Missed Notes: " + missedNotes);
        System.out.println("Extra Notes: " + extraNotes);
    }
}
