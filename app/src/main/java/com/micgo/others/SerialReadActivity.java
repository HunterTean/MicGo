package com.micgo.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.utils.KTVLog;
import com.micgo.utils.KTVUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liuhongtian on 17/10/25.
 */

public class SerialReadActivity extends AppCompatActivity {

    private TextView testAText;
    private TextView testBText;

    private String srcFilePath = KTVUtility.getMGFileDir() + File.separator + "src.mp3";
    private String desFilePath = KTVUtility.getMGFileDir() + File.separator + "dst.mp3";
    private String readFilePath = KTVUtility.getMGFileDir() + File.separator + "test_read.mp3";

    private Thread threadA;
    private Thread threadB;

    private boolean isStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_read);

        setTitle("Serial Read");

        testAText = (TextView) findViewById(R.id.test_a_tv);
        testBText = (TextView) findViewById(R.id.test_b_tv);

    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.a_btn:
                isStop = false;
                startA();
                startB();
                break;
            case R.id.b_btn:

                break;
            case R.id.c_btn:
                isStop = true;
                break;
            default:

                break;
        }
    }

    private void startA() {
        threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileInputStream srcFis = new FileInputStream(new File(srcFilePath));
                    FileOutputStream srcFos = new FileOutputStream(new File(desFilePath));
                    byte[] srcBuffer = new byte[1024 * 4];
                    int ch = -1;
                    int totalSize = (int) new File(srcFilePath).length();
                    int currSize = 0;
                    while (!isStop) {
                        if ((ch = srcFis.read(srcBuffer)) <= 0) {
                            break;
                        }
                        srcFos.write(srcBuffer, 0, ch);
                        currSize += ch;
                        srcFos.flush();
                        KTVLog.d("Tian", "a = " + currSize);
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.arg1 = currSize/totalSize;
                        msg.arg2 = totalSize;
                        Thread.sleep(100);
                    }
                    srcFis.close();
                    srcFos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadA.start();
    }

    private void startB() {
        threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileInputStream srcFis = new FileInputStream(new File(desFilePath));
                    FileOutputStream srcFos = new FileOutputStream(new File(readFilePath));
                    byte[] srcBuffer = new byte[1024 * 4];
                    int ch = -1;
                    int totalSize = 0;
                    while (!isStop) {
                        if ((ch = srcFis.read(srcBuffer)) < 1) {
                            Thread.sleep(500);
                        } else {
                            srcFos.write(srcBuffer, 0, ch);
                            totalSize += ch;
                            srcFos.flush();
                            KTVLog.d("Tian", "b = " + totalSize);
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.arg1 = totalSize;
                            handler.sendMessage(msg);
                        }
                    }
                    srcFis.close();
                    srcFos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadB.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int progress = msg.arg1;
                int totalSize = msg.arg2;
                testAText.setText("progress = " + progress + " | totalSize = " + totalSize);
            } else if (msg.what == 1) {
                int currentSize = msg.arg1;
                testBText.setText("currentSize = " + currentSize);
            }
        }
    };

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, SerialReadActivity.class);
        return intent;
    }

}