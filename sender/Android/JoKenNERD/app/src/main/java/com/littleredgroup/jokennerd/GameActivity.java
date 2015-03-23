package com.littleredgroup.jokennerd;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.littleredgroup.jokennerd.custom.BaseActionBarActivity;
import com.littleredgroup.jokennerd.custom.onChoiceClick;
import com.littleredgroup.jokennerd.utils.Constants;

/**
 * Created by fbvictorhugo on 10/4/14.
 */
public class GameActivity extends BaseActionBarActivity implements View.OnClickListener, onChoiceClick {

    private final String CLASS_TAG = GameActivity.class.getSimpleName();
    private ImageView ivRock;
    private ImageView ivPaper;
    private ImageView ivScissor;
    private ImageView ivLizard;
    private ImageView ivSpock;

    private TextView tvResult;
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    protected RelativeLayout rlGameContent;
    protected TextView tvMessageLayout;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setComponentsView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setListeners();
        playerName = getIntent().getStringExtra(Constants.TAG_NAME);
    }

    private void setComponentsView() {
        mToolbar = (Toolbar) findViewById(R.id.game_toolbar);

        ivRock = (ImageView) findViewById(R.id.game_iv_rock);
        ivPaper = (ImageView) findViewById(R.id.game_iv_paper);
        ivScissor = (ImageView) findViewById(R.id.game_iv_scissor);
        ivLizard = (ImageView) findViewById(R.id.game_iv_lizard);
        ivSpock = (ImageView) findViewById(R.id.game_iv_spock);
        tvResult = (TextView) findViewById(R.id.game_tv_result);
        rlGameContent = (RelativeLayout) findViewById(R.id.game_rl_content);
        tvMessageLayout = (TextView) findViewById(R.id.game_tv_message_layout);
    }

    private void setListeners() {
        ivRock.setOnClickListener(this);
        ivPaper.setOnClickListener(this);
        ivScissor.setOnClickListener(this);
        ivLizard.setOnClickListener(this);
        ivSpock.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.game_iv_rock:
                onRockClick();
                break;

            case R.id.game_iv_paper:
                onPaperClick();
                break;

            case R.id.game_iv_scissor:
                onScissorClick();
                break;

            case R.id.game_iv_lizard:
                onLizardClick();
                break;

            case R.id.game_iv_spock:
                onSpockClick();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onRockClick() {
        //
    }

    @Override
    public void onPaperClick() {
        //
    }

    @Override
    public void onScissorClick() {
        //
    }

    @Override
    public void onLizardClick() {
        //
    }

    @Override
    public void onSpockClick() {
        //
    }

    public void setMessaegStatus(String msg) {
        tvResult.setText(msg);
    }
}