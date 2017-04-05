package com.king.smart.car.mysmartcarapp.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ABaseUIActivity extends ABaseDataActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void IntentActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        startActivity(intent);
    }
}
