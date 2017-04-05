package com.king.smart.car.mysmartcarapp.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.king.smart.car.mysmartcarapp.ui.view.ProgressDialog;


/**
 * @author shun
 * @version V1.0
 * @Title: AbsBaseFragment.java
 * @Package com.setNone.intl.fragment
 * @Description: 具备fragment都可以拥有功能<br/>
 * 1.显示全局加载对话框，activity层次<br/>
 * @date 2015-3-25 下午5:03:17
 */
public class AbsBaseFragment extends Fragment {

    private ProgressDialog pDialog;

    /**
     * 显示等待对话框 当点击返回键取消对话框并停留在该界面
     */
    public void showProgreessDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCanceledOnTouchOutside(false);
        }
        if (pDialog.isShowing()) {
            return;
        }
        pDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    try {
                        dismissProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        pDialog.show();
    }

    /**
     * 销毁对话框
     */
    public void dismissProgressDialog() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void IntentActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        startActivity(intent);
    }

    protected void IntentBundleActivity(Context context, Bundle bundle, Class c) {
        Intent intent = new Intent(context, c);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
