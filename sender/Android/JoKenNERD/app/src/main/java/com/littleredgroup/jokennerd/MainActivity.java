package com.littleredgroup.jokennerd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.littleredgroup.jokennerd.custom.BaseActionBarActivity;
import com.littleredgroup.jokennerd.utils.Constants;


public class MainActivity extends BaseActionBarActivity implements View.OnClickListener {
    Button btnSingle;
    EditText etName;
    Button btnMultiPlayerCast;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instanceComponents();
        setListeners();
    }

    private void instanceComponents() {

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        btnSingle = (Button) findViewById(R.id.main_btn_single_player_vs_com);
        etName = (EditText) findViewById(R.id.main_et_name);
        btnMultiPlayerCast = (Button) findViewById(R.id.main_btn_multi_player_cast);
    }

    private void setListeners() {
        btnSingle.setOnClickListener(this);
        btnMultiPlayerCast.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;
        switch (view.getId()) {

            case R.id.main_btn_single_player_vs_com:
                intent = new Intent(this, PlayerVsComGameActivity.class);
                intent.putExtra(Constants.TAG_NAME, etName.getText().toString());
                startActivity(intent);
                break;

            case R.id.main_btn_multi_player_cast:
                intent = new Intent(this, ChromecastGameActivity.class);
                intent.putExtra(Constants.TAG_NAME, etName.getText().toString());
                startActivity(intent);
                break;

        }
    }
}