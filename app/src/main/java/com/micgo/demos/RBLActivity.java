package com.micgo.demos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.demos.rbl.UUIDs;

import java.util.UUID;

/**
 * Created by liuhongtian on 17/2/25.
 */

public class RBLActivity extends AppCompatActivity {

    private TextView tipsText;

    private String aString = "";
    private String bString = "";
    private String cString = "";
    private String dString = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbl);

        setTitle("RBL");

        tipsText = (TextView) findViewById(R.id.tips_tx);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.a_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        final UUID auuid = UUIDs.random();
                        tipsText.post(new Runnable() {
                            @Override
                            public void run() {
//                                aString = auuid.toString();
                                tipsText.setText(aString + "\n" + bString);
                            }
                        });
                    }
                }).start();
                break;

            case R.id.b_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        final UUID buuid = UUIDs.timeBased();
                        tipsText.post(new Runnable() {
                            @Override
                            public void run() {
//                                bString = buuid.toString();
                                tipsText.setText(aString + "\n" + bString);
                            }
                        });
                    }
                }).start();
                break;

            case R.id.c_btn:
                UUID cuuid = UUID.randomUUID();
                cString = cuuid.toString();
                tipsText.setText(aString + "\n" + bString + "\n" + cString);
                break;

            case R.id.d_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        final String duuid = ChangbaUUIDs.get();
                        tipsText.post(new Runnable() {
                            @Override
                            public void run() {
//                                dString = duuid;
                                tipsText.setText(aString + "\n" + bString + "\n" + cString +"\n" + dString);
                            }
                        });
                    }
                }).start();
                break;

            case R.id.e_btn:

                break;

            case R.id.f_btn:

                break;

        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, RBLActivity.class);
        return intent;
    }

}
