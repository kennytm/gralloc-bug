package com.example.gralloctest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

// This service is used to display a floating window from a background process.
public class TestService extends Service {
    final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    FrameLayout window;

    @Override
    public void onCreate() {
        // When the service was first created, we register a new window in the window service.
        super.onCreate();

        final WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        window = new FrameLayout(this);

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                                               WindowManager.LayoutParams.MATCH_PARENT,
                                               WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                                               WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                       | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                                                       | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                               PixelFormat.TRANSLUCENT);

        wm.addView(window, params);
    }

    private final ITestService.Stub service = new ITestService.Stub() {
        @Override
        public void show(final int value) throws RemoteException {
            // When told to display a text view, we wait for "value" seconds (to avoid everything shown at the same time),
            mainThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // then create the text view on screen,
                    Log.e("lol", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ADD TEXT VIEW #" + value + " >>>>>>>>>");

                    final TextView tv = new TextView(TestService.this);

                    tv.setText(String.valueOf(value));
                    tv.setGravity(Gravity.CENTER);
                    tv.setBackgroundColor(Color.HSVToColor(0x80, new float[] {value * 60, 1, 1}));
                    tv.setTextSize(60);

                    final int i = value - 1;

                    final FrameLayout.LayoutParams myParams = new FrameLayout.LayoutParams(350, 350, 0);
                    myParams.leftMargin = 400 * (i % 3);
                    myParams.topMargin = 400 * (i / 3);
                    tv.setLayoutParams(myParams);

                    window.addView(tv);

                    Log.e("lol", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< FINISH ADD TEXT VIEW #" + value + " <<<<<<<<<");

                    Toast.makeText(TestService.this, "#" + value + " should be shown", Toast.LENGTH_SHORT).show();

                    // and after 0.5 seconds,
                    mainThreadHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("lol", "++++++++++++++++++++++++++++++++++ REMOVE TEXT VIEW #" + value + " +++++++++");
                            // we remove it again.
                            window.removeView(tv);
                            Log.e("lol", "--------------------------- FINISH REMOVE TEXT VIEW #" + value + " ---------");
                        }
                    },
                                                  500);
                }
            },
                                          value * 1000);
        }
    };

    @Override
    public void onDestroy() {
        final WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        wm.removeView(window);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return service;
    }
}
