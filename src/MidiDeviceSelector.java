import javax.sound.midi.*;

public class MidiDeviceSelector {

    public static MidiDevice getKeyboardDevice() throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);
            // Check if the device is a keyboard and has transmitters
            if (device.getMaxTransmitters() != 0 && info.getName().contains("RD")) {
                return device;
            }
        }
        return null; // Keyboard not found
    }

    public static void main(String[] args) {
        try {
            MidiDevice keyboardDevice = getKeyboardDevice();
            if (keyboardDevice != null) {
                System.out.println("Keyboard Device Found: " + keyboardDevice.getDeviceInfo().getName());
                // Proceed to recording (next steps)
            } else {
                System.out.println("Keyboard Device Not Found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
