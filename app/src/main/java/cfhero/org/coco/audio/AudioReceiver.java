package cfhero.org.coco.audio;

public interface AudioReceiver {

    void write(byte[] audioBuffer);

    void stopRecieving();
}
