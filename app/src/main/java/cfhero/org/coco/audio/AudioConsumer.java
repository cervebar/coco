package cfhero.org.coco.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public interface AudioConsumer {

    public void consume(AudioTrack audioTrack);

}