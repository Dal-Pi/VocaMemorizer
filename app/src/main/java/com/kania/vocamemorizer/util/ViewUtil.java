package com.kania.vocamemorizer.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Seonghan Kim on 2016-11-06.
 */

public class ViewUtil {

    public static void changeStatusbarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            int flag = decorView.getSystemUiVisibility();
            flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(flag);
        }
    }

    public static void setMenuItemColor(MenuItem item, int color) {
        Drawable d = item.getIcon();
        setDrawableColor(d, color);
        item.setIcon(d);
    }

    public static void setButtonColor(Button btn, int color) {
        Drawable d = btn.getBackground();
        setDrawableColor(d, color);
        btn.setBackgroundDrawable(d);
    }

    public static void setImageButtonColor(ImageButton btn, int color) {
        Drawable d = btn.getDrawable();
        setDrawableColor(d, color);
        btn.setImageDrawable(d);
    }

    public static void setEditColor(EditText edit, int color) {
        Drawable d = edit.getBackground();
        setDrawableColor(d, color);
        edit.setBackgroundDrawable(d);
    }

    public static void setDialogButtonColor(final AlertDialog dialog, final int positiveColor,
                                            final int neutralColor, final int negativeColor) {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextColor(positiveColor);
                Button btnNeutral = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                btnNeutral.setTextColor(neutralColor);
                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setTextColor(negativeColor);
            }
        });
    }

    public static void setDrawableColor(Drawable d, int color) {
        d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
    }
}
