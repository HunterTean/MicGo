package com.micgo.opengl.cube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.studio.gl.GLESController;

/**
 * Created by liuhongtian on 18/1/10.
 */

public class GLCubeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_trans);

        setTitle("Trans");

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(GLESController.getInstance().test());
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, GLCubeActivity.class);
        return intent;
    }

}
