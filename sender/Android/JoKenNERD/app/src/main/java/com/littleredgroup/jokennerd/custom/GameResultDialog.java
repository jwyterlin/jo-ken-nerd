package com.littleredgroup.jokennerd.custom;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.littleredgroup.jokennerd.R;

/**
 * Created by Victor Hugo on 22/03/2015.
 */
public class GameResultDialog extends Dialog {

    Context context;

    ImageView ivResultPlayer;
    ImageView ivResultCom;
    TextView tvResult;

    public GameResultDialog(Context context, boolean cancelable,
                            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        initialize();
    }

    public GameResultDialog(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public GameResultDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initialize();
    }


    private void initialize() {
        setContentView(R.layout.dialog_result);

        ivResultPlayer = (ImageView) findViewById(R.id.dialog_img_you);
        ivResultCom = (ImageView) findViewById(R.id.dialog_img_com);
        tvResult = (TextView) findViewById(R.id.dialog_tv_result);
    }


    public void setImagePlayer(int resId) {
        ivResultPlayer.setImageResource(resId);
    }

    public void setImageCom(int resId) {
        ivResultCom.setImageResource(resId);
    }

    public void setResult(String result) {
        tvResult.setText(result);
    }

}
