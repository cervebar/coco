package cfhero.org.coco.archive;

import android.app.IntentService;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

import static android.media.MediaRecorder.AudioSource.MIC;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class Recorder  {
    private static final String LOG_TAG = "Recorder";

    private MediaRecorder mr;
    private String file;

    public Recorder(String file) {
        this.file = file;
    }

    public void startRecoring() {
        initMediaRecorder();
        mr.start();
    }

    public void stopRecording() {
        tryReleaseMediaRecorder("stop request");
    }

    private void tryReleaseMediaRecorder(String caller){
        Log.i(LOG_TAG,caller + "releasing recorder " + mr);
        if(mr !=null) {
            try{
                mr.stop();
                mr.reset();
                mr.release();
            }catch(RuntimeException stopException){
               Log.i(LOG_TAG,"media recorder runtime error", stopException);
            }
            mr = null;
        }
    }

    private void initMediaRecorder(){
        tryReleaseMediaRecorder("by init");
        this.mr = new MediaRecorder();
        mr.setAudioSource(MIC);

        // TODO
        // To record raw audio select UNPROCESSED.
        // Some devices do not support unprocessed input.
        // Call AudioManager.getProperty(AudioManager.PROPERTY_SUPPORT_AUDIO_SOURCE_UNPROCESSED) first to verify it's available.
        // If it is not, try using VOICE_RECOGNITION instead, which does not employ AGC or noise suppression.
        // You can use UNPROCESSED as an audio source even when the property is not supported,
        // but there is no guarantee whether the signal will be unprocessed or not in that case.

        mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // TODO
        // MediaRecorder supports the MPEG2_TS format, which is useful for streaming:

        Log.i(LOG_TAG,"path: " + file);
        mr.setOutputFile(file);

        //TODO
        // Starting with Android 8.0 (API level 26) you can use a MediaMuxer to record multiple simultaneous audio and video streams. In earlier versions of Android you can only record one audio track and/or one video track at a time.
        // Use the addTrack() method to mix multipe tracks together.
        // You can also add one or more metadata tracks with custom information for each frame, but only to MP4 containers. Your app defines the format and content of the metadata.
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mr.prepare();
        } catch (IOException e) {
            Log.e("ERROR","couldn't be initialized");
            e.printStackTrace();
        }
    }

    public boolean isRecoring() {
        return this.mr !=null;
    }
}
