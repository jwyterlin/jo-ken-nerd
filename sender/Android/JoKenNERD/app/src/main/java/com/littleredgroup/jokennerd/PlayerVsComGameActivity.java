package com.littleredgroup.jokennerd;

import android.os.Bundle;

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
        machineChoice = getMachineChoice();
        String result = GameLogic.getResult(Constants.CHOICE_ROCK, machineChoice);
        showToastLong(result + "  >>  Maquina: " + GameLogic.getTextChoice(this, machineChoice));
    }

    @Override
    public void onPaperClick() {
        machineChoice = getMachineChoice();
        String result = GameLogic.getResult(Constants.CHOICE_PAPER, machineChoice);
        showToastLong(result + "  >>  Maquina: " + GameLogic.getTextChoice(this, machineChoice));
    }

    @Override
    public void onScissorClick() {
        machineChoice = getMachineChoice();
        String result = GameLogic.getResult(Constants.CHOICE_SCISSOR, machineChoice);
        showToastLong(result + "  >>  Maquina: " + GameLogic.getTextChoice(this, machineChoice));
    }

    @Override
    public void onLizardClick() {
        machineChoice = getMachineChoice();
        String result = GameLogic.getResult(Constants.CHOICE_LIZARD, machineChoice);
        showToastLong(result + "  >>  Maquina: " + GameLogic.getTextChoice(this, machineChoice));
    }

    @Override
    public void onSpockClick() {
        machineChoice = getMachineChoice();
        String result = GameLogic.getResult(Constants.CHOICE_SPOCK, machineChoice);
        showToastLong(result + "  >>  Maquina: " + GameLogic.getTextChoice(this, machineChoice));
    }

    private int getMachineChoice() {
        return rand.nextInt(5) + 1;
    }
}
