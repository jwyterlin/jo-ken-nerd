package com.littleredgroup.jokennerd;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.littleredgroup.jokennerd.custom.BaseActionBarActivity;
import com.littleredgroup.jokennerd.utils.Constants;
import com.littleredgroup.jokennerd.utils.Utils;

/**
 * Created by Victor Hugo on 21/03/2015.
 */
public class AboutActivity extends BaseActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_googlecast:
                Utils.openHiperlink(this, Constants.URL_GOOGLE_SDK_CAST);
                break;

            case R.id.action_chromecast:
                Utils.openHiperlink(this, Constants.URL_CHROMECAST);
                break;

            case R.id.action_hackathon:
                Utils.openHiperlink(this, Constants.URL_HACKATHON_GDGJF);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
