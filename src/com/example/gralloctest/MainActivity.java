package com.example.gralloctest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.VideoView;

// This activity is used to play a video and start the services.
public class MainActivity extends Activity {
    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Just play any video.
        final VideoView videoView = new VideoView(this);
        videoView.setVideoPath("/data/user/jobs.mp4");
        videoView.start();
        setContentView(videoView);

        // Then start 5 services.
        for (int i = 1; i <= 5; ++i) {
            final Intent intent = new Intent("com.example.gralloctest.TestService" + i);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            // When the service is ready, we tell them to show a TextView.

            final String id = name.getClassName().substring("com.example.gralloctest.TestService".length());
            final int value = Integer.parseInt(id);

            try {
                ITestService.Stub.asInterface(service).show(value);
            } catch (final RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {}
    };
}
