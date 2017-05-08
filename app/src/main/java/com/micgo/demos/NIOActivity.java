package com.micgo.demos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.micgo.MicGoApplication;
import com.micgo.R;
import com.micgo.demos.nio.ExceptionWatcher;
import com.micgo.demos.nio.RecordingDetail;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuhongtian on 17/5/8.
 */

public class NIOActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nio);

        setTitle("Nio");
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.write_btn:
                RecordingDetail detail = new RecordingDetail("11221314", "http://yuzong.wei.wu", "http://java.nio.read.write", true, true, true, "2017/12/19 08:45", 150);
                Gson gson = MicGoApplication.getGson();

                JSONObject properties = null;
                try {
                    properties = new JSONObject(gson.toJson(detail));
                    ExceptionWatcher.getInstance().register("record_detail", properties.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.break_btn:

                break;

            case R.id.read_btn:

                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, NIOActivity.class);
        return intent;
    }
}
