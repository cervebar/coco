package cfhero.org.coco.archive;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class RecordingServiceArchive extends Service {
    private static final String LOG_TAG = "RecordingService";
    Recorder rec;

    public RecordingServiceArchive() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");
    }

    // bind --------------------------------------
    private IBinder binder = new RecoringBinder();

    public class RecoringBinder extends Binder {
        RecordingServiceArchive getService() {
            return RecordingServiceArchive.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
//        rec.stopRecording();
    }


    public boolean isRecording() {
        return rec!=null && rec.isRecoring();
    }

    public void startRecording(String file){
        rec = new Recorder(file);
        rec.startRecoring();
    }

    public void stopRecording(){
        rec.stopRecording();
    }
}
