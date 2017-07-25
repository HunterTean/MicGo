package com.micgo.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.micgo.MicGoApplication;
import com.micgo.R;
import com.micgo.others.nio.ExceptionWatcher;
import com.micgo.others.nio.RecordingDetail;
import com.micgo.utils.KTVLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuhongtian on 17/5/8.
 */

public class NIOActivity extends AppCompatActivity {

    private static final String TAG = "NIOActivity";

    private TextView tipsText;

    private int writeIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nio);

        setTitle("Nio");
        KTVLog.d(TAG , "onCreate");

        tipsText = (TextView) findViewById(R.id.tips_tx);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.write_btn:
                RecordingDetail detail = new RecordingDetail("11221314", "http://yuzong.wei.wu", "http://java.nio.read.write", true, true, true, "2017/12/19 08:45", 150);
                Gson gson = MicGoApplication.getGson();

                try {
                    JSONObject properties = new JSONObject(gson.toJson(detail));
                    ExceptionWatcher.getInstance().register("record_detail", writeIndex++ + " | " + properties.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                tipsText.setText("write");
                break;

            case R.id.break_btn:

                tipsText.setText("break;");
                break;

            case R.id.read_btn:
                String content = ExceptionWatcher.getInstance().check("record_detail");
                RecordingDetail recordingDetail = MicGoApplication.getGson().fromJson(content, RecordingDetail.class);
                tipsText.setText("read '\n'" + recordingDetail);
                break;

            case R.id.delete_btn:
                ExceptionWatcher.getInstance().unregister("record_detail");
                break;

            case R.id.crash_btn:
                int i = 0/0;
                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, NIOActivity.class);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        KTVLog.d(TAG , "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        KTVLog.d(TAG , "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        KTVLog.d(TAG , "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        KTVLog.d(TAG , "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        KTVLog.d(TAG , "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KTVLog.d(TAG , "onDestroy");
        ExceptionWatcher.getInstance().unregister("record_detail");
    }
}
