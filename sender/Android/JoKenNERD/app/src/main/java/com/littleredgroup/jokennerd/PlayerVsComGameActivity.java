package com.littleredgroup.jokennerd;

import android.app.AlertDialog;
import android.os.Bundle;

import com.littleredgroup.jokennerd.custom.GameResultDialog;
import com.littleredgroup.jokennerd.utils.Constants;
import com.littleredgroup.jokennerd.utils.GameLogic;

import java.util.Random;

/**
 * Created by fbvictorhugo on 10/4/14.
 */
public class PlayerVsComGameActivity extends GameActivity {

    private Random rand = new Random();
    private int machineChoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.lbl_single_player);
    }

    @Override
    public void onRockClick() {
        showResult(Constants.CHOICE_ROCK, getMachineChoice());
    }

    @Override
    public void onPaperClick() {
        showResult(Constants.CHOICE_PAPER, getMachineChoice());
    }

    @Override
    public void onScissorClick() {
        showResult(Constants.CHOICE_SCISSOR, getMachineChoice());
    }

    @Override
    public void onLizardClick() {
        showResult(Constants.CHOICE_LIZARD, getMachineChoice());
    }

    @Override
    public void onSpockClick() {
        showResult(Constants.CHOICE_SPOCK, getMachineChoice());
    }

    private int getMachineChoice() {
        return rand.nextInt(5) + 1;
    }

    private void showResult(final int myChoice, final int otherChoice) {
        String result = GameLogic.getResult(this, myChoice, otherChoice);
        GameResultDialog dialog = new GameResultDialog(this);
        dialog.setTitle(R.string.title_result);
        dialog.setResult(result);
        dialog.setImagePlayer(GameLogic.getResImageChoice(myChoice));
        dialog.setImageCom(GameLogic.getResImageChoice(otherChoice));
        dialog.show();
    }

}
