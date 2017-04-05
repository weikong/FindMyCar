package com.king.smart.car.mysmartcarapp.manager;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

import com.king.smart.car.mysmartcarapp.app.App;

/**
 * Created by xinzhendi-031 on 2016/11/18.
 */
public class DisplayUtil {

    public static int screenWidth = 480;
    public static int screenHeight = 800;

    public static void displayScreen(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                App.getInstance().getResources().getDisplayMetrics());
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}
