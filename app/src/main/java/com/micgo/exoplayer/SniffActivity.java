package com.micgo.exoplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.exoplayer.sniff.MetadataChecker;
import com.micgo.utils.KTVLog;
import com.micgo.utils.KTVUtility;

import java.io.File;

/**
 * Created by liuhongtian on 17/5/17.
 */

public class SniffActivity extends AppCompatActivity {

    private static final String TAG = "SniffActivity";

    private TextView tipsText;

    private HandlerThread handlerThread = new HandlerThread("sniff");
    private ThreadHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sniff);

        setTitle("Sniff");

        tipsText = (TextView) findViewById(R.id.tips_tx);

        handlerThread.start();
        handler = new ThreadHandler(handlerThread.getLooper());
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.sniff_btn:
                handler.sendEmptyMessage(0);
                tipsText.setText("sniff;");
                break;

            case R.id.clear_btn:
                tipsText.setText("");
                break;
        }
    }

    class ThreadHandler extends Handler {

        public ThreadHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                File sdFile = KTVUtility.getSDPath();
                File songFile = new File(sdFile, ".ktv/song");
                String[] dirName = songFile.list();
                long currentTime = System.currentTimeMillis();
                if (dirName != null && dirName.length > 0) {
//                    for (int i = 0; i < dirName.length; i++) {
//                        File subSongName = new File(songFile, dirName[i]);
                    File subSongName = new File(songFile, "_music");
                        String[] subDirName = subSongName.list();
                        for (int j = 0; j < subDirName.length; j++) {
                            long cTime = System.currentTimeMillis();

                            File file = new File(subSongName, subDirName[j]);
                            final Uri uri = Uri.fromFile(file);
                            MetadataChecker metadataChecker = new MetadataChecker(uri, new MetadataChecker.OnCheckListener() {
                                @Override
                                public void onComplete(boolean result) {
                                    if (result) {
                                        if (!uri.toString().endsWith(".mp3")) {
                                            KTVLog.d(TAG, "MetadataChecker onComplete true other | uri = " + uri);
                                        }
                                    } else {
                                        if (uri.toString().endsWith(".mp3")) {
                                            KTVLog.d(TAG, "MetadataChecker onComplete false mp3 | uri = " + uri);
                                        }
                                    }
                                }
                            });
                            metadataChecker.start();

                            KTVLog.d(TAG, "MetadataChecker check index = " + j + " | " + uri + " | time = " + (System.currentTimeMillis() - cTime));
                        }
                    }
//                }
                KTVLog.d(TAG, "MetadataChecker done | totalTime = " + (System.currentTimeMillis() - currentTime));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, SniffActivity.class);
        return intent;
    }
}
