package edu.sysu.ijkplayerdemo3;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private IjkMediaPlayer player;
    SurfaceView surface;
    SurfaceHolder surfaceHolder;
    String path = "http://p9egy625x.bkt.clouddn.com/class.avi";

    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/MDSVideo/";
    public static String localPath = DOWNLOAD_PATH + "class.avi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏、状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Log.i("test", "onCreate");

        //初始化播放器
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        surface = findViewById(R.id.surface);
        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
    }
    public void openVideo(){
        Log.i("test", "openVideo");
        release();
        try {
            player = new IjkMediaPlayer();
            player.setDataSource(MainActivity.this, Uri.parse(path));
            //player.setDataSource(path);
            player.setDisplay(surfaceHolder);

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setScreenOnWhilePlaying(true);
            player.prepareAsync();
            player.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    Log.i("test", "onPrepared中调用start()");
                    iMediaPlayer.start();
                }
            });
            Log.i("test", "onPrepared外调用start()前");
            player.start();
            Log.i("test", "onPrepared外调用start()后");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void release() {
        Log.i("test", "release");
        if (player != null) {
            player.reset();
            player.release();
            player = null;
            AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("test", "onResume");
        // activity 可见时尝试继续播放
        if (player != null){
            Log.i("test", "onResume--player不为null");
            player.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test", "onStop");
        player.pause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("test", "surfaceCreated");
        openVideo();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }
}

