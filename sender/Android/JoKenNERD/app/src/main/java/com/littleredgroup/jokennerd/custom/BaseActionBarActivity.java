package com.littleredgroup.jokennerd.custom;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.littleredgroup.jokennerd.R;

/**
 * Created by fbvictorhugo on 10/4/14.
 */
public class BaseActionBarActivity extends ActionBarActivity {


    public void showToastLong(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showToastLong(int message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
