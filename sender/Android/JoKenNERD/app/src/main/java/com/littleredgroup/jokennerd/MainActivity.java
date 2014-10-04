package com.littleredgroup.jokennerd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.MediaRouteButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.littleredgroup.jokennerd.custom.BaseActionBarActivity;


public class MainActivity extends BaseActionBarActivity implements View.OnClickListener {

    Button btnSingle;
    EditText etName;
    MediaRouteButton btnMediaRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instanceComponents();
        setListeners();
    }

    private void instanceComponents() {
        btnSingle = (Button) findViewById(R.id.main_btn_single_player_vs_com);
        etName = (EditText) findViewById(R.id.main_et_name);
        btnMediaRoute = (MediaRouteButton) findViewById(R.id.main_btn_multi_player_chromecast);
    }

    private void setListeners() {
        btnSingle.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;
        switch (view.getId()) {

            case R.id.main_btn_single_player_vs_com:
                intent = new Intent(this, PlayerVsComGameActivity.class);
                startActivity(intent);
                break;
        }

    }
}
