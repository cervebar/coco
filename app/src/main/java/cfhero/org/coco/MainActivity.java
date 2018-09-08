package cfhero.org.coco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainSctivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // navigation
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        tryReleaseMediaPlayer();
    }


    // navigation -----------------------------
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    // SOUND -------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    // recorder -----------------------
    Recorder rec;
    String file;

    public void startRecord(View view) {
        if(rec == null){
            file = getExternalCacheDir().getAbsolutePath()+"/out.mp4";
            rec = new Recorder(file);
        }
        rec.startRecoring();
    }

    public void stopRecord(View view) {
        if (rec != null){
            rec.stopRecording();
        }
    }


    // play audio -----------------------------------------------------------
    private MediaPlayer mp;

    public void play(View view){
        tryReleaseMediaPlayer();
        mp = new MediaPlayer();
        try {
            Log.w("kvka","file path " +file);
            Uri path= Uri.parse("file://"+ file);
            Log.i(LOG_TAG,path.toString());
            mp.setDataSource(this,path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            Log.e(LOG_TAG, "prepare() of play failed");
            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        tryReleaseMediaPlayer();
    }

    private void tryReleaseMediaPlayer(){
        if(mp!=null) {
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }
    }

}
