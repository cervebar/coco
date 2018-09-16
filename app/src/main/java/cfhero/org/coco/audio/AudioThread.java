package cfhero.org.coco.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.Arrays;

public class AudioThread extends Thread {
    private final String TAG = AudioThread.class.getName();
    private final AudioReceiver audioReceiver;
    private boolean running = false;

    private AudioRecord recorder = null;

    int bufferSize = 320;
    byte buffer[] = new byte[bufferSize];

    /**
     * Give the thread high priority so that it's not canceled unexpectedly
     * @param audioReceiver
     */
    public AudioThread(AudioReceiver audioReceiver) {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        this.audioReceiver = audioReceiver;
    }

    @Override
    public void start(){
        running = true;
        super.start();
    }

    @Override
    public void run() {
        Log.i("Audio", "Running Audio Thread");
        if(running == false){
            return;
        }
        /** ======= Initialize AudioRecord ======== **/
        initRecorder();
        /** ------------------------------------------------------ **/
        while (running == true) {
            /* Read & Write to the Device */
            recorder.read(buffer, 0, bufferSize);
            byte[] bytes = Arrays.copyOf(buffer, bufferSize);
            audioReceiver.write(bytes);
        }
        Log.i(TAG, "Loopback exit");
        return;
    }

    private void initRecorder() {
        recorder = findAudioRecord(recorder);
        if (recorder == null) {
            Log.e(TAG, "======== findAudioRecord : Returned Error! =========== ");
            return;
        }
        if (AudioRecord.STATE_INITIALIZED == recorder.getState()) {
            recorder.startRecording();
            Log.d(TAG, "========= Recorder Started... =========");
        } else {
            Log.d(TAG, "==== Initilazation failed for AudioRecord or AudioTrack =====");
            return;
        }
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */
    public void stopRecording() {
        running = false;
        Log.d(TAG, "=====  Stop Button is pressed ===== ");
        if (AudioRecord.STATE_INITIALIZED == recorder.getState()) {
            Log.d(TAG, "unitialized recording");
            recorder.stop();
            recorder.release();
        }
       audioReceiver.stopRecieving();
    }

    public AudioRecord findAudioRecord(AudioRecord recorder) {
        int m_bufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if (m_bufferSize != AudioRecord.ERROR_BAD_VALUE) {
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, m_bufferSize);

            if (recorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
                throw new RuntimeException("not initialized rec");
            }
        }
        return recorder;
    }

}