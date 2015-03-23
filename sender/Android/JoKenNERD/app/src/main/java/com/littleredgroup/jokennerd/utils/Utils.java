package com.littleredgroup.jokennerd.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Victor Hugo on 22/03/2015.
 */
public class Utils {

    public static void openHiperlink(Activity activity, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }
}
