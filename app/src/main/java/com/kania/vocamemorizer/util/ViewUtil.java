package com.kania.vocamemorizer.util;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Seonghan Kim on 2016-11-06.
 */

public class ViewUtil {

    public static void setButtonColor(Button btn, int color) {
        Drawable d = btn.getBackground();
        d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        btn.setBackgroundDrawable(d);
    }

    public static void setImageButtonColor(ImageButton btn, int color) {
        Drawable d = btn.getDrawable();
        d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        btn.setImageDrawable(d);
    }

    public static void setEditColor(EditText edit, int color) {
        Drawable d = edit.getBackground();
        d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
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
}
