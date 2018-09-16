package cfhero.org.coco.audio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import cfhero.org.coco.audio.receiver.CompositeReceiver;
import cfhero.org.coco.audio.receiver.EchoReceiver;
import cfhero.org.coco.audio.receiver.RestReceiver;

import static java.util.Arrays.asList;

public class RecordingService extends Service {
    private static final String TAG = RecordingService.class.getName();
    AudioReceiver reciever;

    public RecordingService() {
        reciever = new CompositeReceiver(asList(new EchoReceiver(), new RestReceiver()));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "in onCreate");
    }

    // bind --------------------------------------
    private IBinder binder = new RecoringBinder();

    public class RecoringBinder extends Binder {

        public RecordingService getService() {
            return RecordingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "in onBind");
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "in onDestroy");
    }


    private boolean isRunning = false;
    private AudioThread audioThread;

    public boolean isRecording() {
        return isRunning;
    }

    public void startRecording(String file) {
        isRunning = true;
        audioThread = new AudioThread(reciever);
        audioThread.start();
    }

    public void stopRecording() {
        isRunning = false;
        audioThread.stopRecording();
        audioThread = null;
    }


}
