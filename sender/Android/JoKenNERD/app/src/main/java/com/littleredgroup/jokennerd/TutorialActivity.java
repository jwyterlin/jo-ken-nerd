package com.littleredgroup.jokennerd;


import android.os.Bundle;

import com.littleredgroup.jokennerd.custom.BaseActionBarActivity;

public class TutorialActivity extends BaseActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
