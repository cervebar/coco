package cfhero.org.coco.audio.receiver;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import cfhero.org.coco.audio.AudioReceiver;

public class EchoReceiver implements AudioReceiver {
    private static final String TAG = EchoReceiver.class.getName();
    int bufferSize = 320;
    byte buffer[] = new byte[bufferSize];
    boolean isRunning;

    private AudioTrack track = null;

    public void stopRecieving() {
       releaseTrack();
    }

    @Override
    public void write(byte[] audioBuffer) {
        if(track == null){
            initTracker();
        }
        track.write(audioBuffer, 0, bufferSize);
    }

    private void initTracker() {
        if(track != null){
           releaseTrack();
        }
        track = findAudioTrack(track);
        if (track == null) {
            Log.e(TAG, "======== findAudioTrack : Returned Error! ========== ");
            return;
        }
        if (AudioTrack.STATE_INITIALIZED == track.getState()) {
            track.play();
            Log.d(TAG, "========= Track Started... =========");
        } else {
            Log.d(TAG, "==== Initilazation failed for AudioRecord or AudioTrack =====");
            return;
        }
    }

    private void releaseTrack() {
        if (AudioTrack.STATE_INITIALIZED == track.getState()) {
            track.stop();
            track.release();
        }
        track = null;
    }

    public AudioTrack findAudioTrack(AudioTrack track) {
        int m_bufferSize = AudioTrack.getMinBufferSize(8000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (m_bufferSize != AudioTrack.ERROR_BAD_VALUE) {
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, m_bufferSize,
                    AudioTrack.MODE_STREAM);

            if (track.getState() == AudioTrack.STATE_UNINITIALIZED) {
                throw new RuntimeException("not initialized track");
            }
        }
        return track;
    }



}
