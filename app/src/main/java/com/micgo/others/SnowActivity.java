package com.micgo.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.micgo.R;
import com.micgo.utils.KTVLog;

/**
 * Created by liuhongtian on 18/1/2.
 */

public class SnowActivity extends AppCompatActivity {

    private static final String TAG = "SnowActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow);

        setTitle("Snow");
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, SnowActivity.class);
        return intent;
    }

    public enum Singleton {
        INSTANCE;

        public void test() {
            KTVLog.d("Singleton", "hello single instance");
        }
    }

    public static class Singleton5 {
        private final static class SingletonHolder {
            private static final Singleton5 INSTANCE = new Singleton5();
        }

        private Singleton5() {

        }

        public static Singleton5 getInstance() {
            return SingletonHolder.INSTANCE;
        }
    }

}
