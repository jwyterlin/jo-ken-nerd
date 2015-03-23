package com.littleredgroup.jokennerd.utils;

import android.content.Context;

import com.littleredgroup.jokennerd.R;

/**
 * Created by fbvictorhugo on 10/4/14.
 */
public class GameLogic {

    public static String getResult(Context context, final int myChoice, final int otherChoice) {

        //Draw Game
        if (myChoice == otherChoice) {
            return context.getString(R.string.msg_game_draw);
        }

        switch (myChoice) {
            case Constants.CHOICE_ROCK:
                if (otherChoice == Constants.CHOICE_SCISSOR || otherChoice == Constants.CHOICE_LIZARD) {
                    return context.getString(R.string.msg_game_win);
                } else if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_SPOCK) {
                    return context.getString(R.string.msg_game_loses);
                }
                break;

            case Constants.CHOICE_PAPER:
                if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SPOCK) {
                    return context.getString(R.string.msg_game_win);
                } else if (otherChoice == Constants.CHOICE_SCISSOR || otherChoice == Constants.CHOICE_LIZARD) {
                    return context.getString(R.string.msg_game_loses);
                }
                break;


            case Constants.CHOICE_SCISSOR:
                if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_LIZARD) {
                    return context.getString(R.string.msg_game_win);
                } else if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SPOCK) {
                    return context.getString(R.string.msg_game_loses);
                }
                break;

            case Constants.CHOICE_LIZARD:
                if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_SPOCK) {
                    return context.getString(R.string.msg_game_win);
                } else if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SCISSOR) {
                    return context.getString(R.string.msg_game_loses);
                }
                break;


            case Constants.CHOICE_SPOCK:
                if (otherChoice == Constants.CHOICE_ROCK || otherChoice == Constants.CHOICE_SCISSOR) {
                    return context.getString(R.string.msg_game_win);
                } else if (otherChoice == Constants.CHOICE_PAPER || otherChoice == Constants.CHOICE_LIZARD) {
                    return context.getString(R.string.msg_game_loses);
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

    public static int getResImageChoice(int choice) {
        switch (choice) {
            case Constants.CHOICE_ROCK:
                return R.drawable.rock;
            case Constants.CHOICE_PAPER:
                return R.drawable.paper;
            case Constants.CHOICE_SCISSOR:
                return R.drawable.scissor;
            case Constants.CHOICE_LIZARD:
                return R.drawable.lizard;
            case Constants.CHOICE_SPOCK:
                return R.drawable.spock;
        }
        return 0;
    }


}
