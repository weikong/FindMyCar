package com.king.smart.car.mysmartcarapp.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;

/**
 * Created by xinzhendi-031 on 2016/11/11.
 */
public class MyInputDialog extends Dialog {

    private EditText etInput;
    private TextView tvConfirm, tvCancle;
    private ImageView ivGps;
    private OnItemInDlgClickListener onItemInDlgClickListener;
    public static final int TypeConfirm = 1; //点击确定
    public static final int TypeCancle = 2; //点击取消
    public static final int TypeGps = 3; //点击定位

    public MyInputDialog(Context context) {
//        super(context);
        this(context, R.style.Dialog_input);
    }

    public MyInputDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_my_input);
        etInput = (EditText)findViewById(R.id.etInputAddress);
        tvConfirm = (TextView) findViewById(R.id.tvConfirm);
        tvCancle = (TextView) findViewById(R.id.tvCancle);
        ivGps = (ImageView) findViewById(R.id.ivGps);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemInDlgClickListener != null){
                    String input = etInput.getText().toString();
                    onItemInDlgClickListener.onItemClick(TypeConfirm,input);
                }
                dismiss();
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemInDlgClickListener != null)
                    onItemInDlgClickListener.onItemClick(TypeCancle,"");
                dismiss();
            }
        });
        ivGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemInDlgClickListener != null)
                    onItemInDlgClickListener.onItemClick(TypeGps,"");
            }
        });
    }

    @Override
    public void dismiss() {
        if (etInput != null)
            etInput.setText("");
        super.dismiss();
    }

    public void setIconVisiable(int visiable){
        if (ivGps != null)
            ivGps.setVisibility(visiable);
    }

    public void setEtHintInput(String s) {
        if (etInput != null)
            etInput.setHint(s);
    }

    public void setEtInput(String s) {
        if (etInput != null)
            etInput.setText(s);
    }

    // 响应DLg中的List item 点击
    public interface OnItemInDlgClickListener {
        public void onItemClick(int type, String input);
    }

    public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
        onItemInDlgClickListener = itemListener;
    }
}
