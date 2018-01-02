package com.micgo.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.micgo.R;

/**
 * Created by liuhongtian on 17/2/25.
 */

public class OthersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);

        setTitle("Others");
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.check_metadata:
                Intent checkIntent = CheckMetadataActivity.buildIntent(this);
                startActivity(checkIntent);
                break;
            case R.id.nio:
                Intent nioIntent = NIOActivity.buildIntent(this);
                startActivity(nioIntent);
                break;
            case R.id.rbl:
                Intent rblIntent = RBLActivity.buildIntent(this);
                startActivity(rblIntent);
                break;
            case R.id.serial_read:
                Intent srIntent = SerialReadActivity.buildIntent(this);
                startActivity(srIntent);
                break;
            case R.id.texture_view:
                Intent tvIntent = TextureViewActivity.buildIntent(this);
                startActivity(tvIntent);
                break;
            case R.id.texture_snow:
                Intent snowIntent = SnowActivity.buildIntent(this);
                startActivity(snowIntent);
                break;
            default:

                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, OthersActivity.class);
        return intent;
    }

}
