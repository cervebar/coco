package cfhero.org.coco.audio.receiver;

import java.util.List;

import cfhero.org.coco.audio.AudioReceiver;

public class CompositeReceiver implements AudioReceiver {
    private final List<AudioReceiver> audioReceivers;

    public CompositeReceiver(List<AudioReceiver> audioReceivers) {
        this.audioReceivers = audioReceivers;
    }

    @Override
    public void write(final byte[] audioBuffer) {
        for (AudioReceiver rec : audioReceivers) {
            rec.write(audioBuffer);
        }
    }

    @Override
    public void stopRecieving() {
        for (AudioReceiver rec : audioReceivers) {
            rec.stopRecieving();
        }
    }
}
