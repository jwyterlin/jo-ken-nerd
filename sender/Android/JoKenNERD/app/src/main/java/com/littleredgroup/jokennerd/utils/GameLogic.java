package com.littleredgroup.jokennerd.utils;

import android.content.Context;

import com.littleredgroup.jokennerd.R;

/**
 * Created by fbvictorhugo on 10/4/14.
 */
public class GameLogic {

    public static String getResult(final int myChoice, final int otherChoice) {

        //Draw Game
        if (myChoice == otherChoice) {
            return Constants.RESULT_DRAW;
        }

        switch (myChoice) {
            case Constants.CHOICE_ROCK:
                if (otherChoice == Constants.CHOICE_SCISSOR || otherChoice == Constants.CHOICE_LIZARD) {
                    return Constants.RESULT_WIN;
                } else if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_SPOCK) {
                    return Constants.RESULT_LOSE;
                }
                break;

            case Constants.CHOICE_PAPER:
                if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SPOCK) {
                    return Constants.RESULT_WIN;
                } else if (otherChoice == Constants.CHOICE_SCISSOR || otherChoice == Constants.CHOICE_LIZARD) {
                    return Constants.RESULT_LOSE;
                }
                break;


            case Constants.CHOICE_SCISSOR:
                if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_LIZARD) {
                    return Constants.RESULT_WIN;
                } else if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SPOCK) {
                    return Constants.RESULT_LOSE;
                }
                break;

            case Constants.CHOICE_LIZARD:
                if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_SPOCK) {
                    return Constants.RESULT_WIN;
                } else if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SCISSOR) {
                    return Constants.RESULT_LOSE;
                }
                break;


            case Constants.CHOICE_SPOCK:
                if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SCISSOR) {
                    return Constants.RESULT_WIN;
                } else if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_LIZARD) {
                    return Constants.RESULT_LOSE;
                }
                break;
        }

        return Constants.RESULT_DRAW;
    }

    public static String getTextChoice(Context context, int choice) {
        switch (choice) {
            case Constants.CHOICE_ROCK:
                return context.getString(R.string.choice_rock);
            case Constants.CHOICE_PAPER:
                return context.getString(R.string.choice_paper);
            case Constants.CHOICE_SCISSOR:
                return context.getString(R.string.choice_scissor);
            case Constants.CHOICE_LIZARD:
                return context.getString(R.string.choice_lizard);
            case Constants.CHOICE_SPOCK:
                return context.getString(R.string.choice_spock);
        }
        return "";
    }


}
