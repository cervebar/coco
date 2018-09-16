package cfhero.org.coco.audio.receiver;

import android.util.Log;

import cfhero.org.coco.audio.AudioReceiver;

public class RestReceiver implements AudioReceiver {
    private static final String TAG = RestReceiver.class.getName();

    @Override
    public void write(byte[] audioBuffer) {
        Log.d(TAG,"recieving bytes "+audioBuffer.length + " , bytes" + audioBuffer);
    }

    @Override
    public void stopRecieving() {
    }

}
