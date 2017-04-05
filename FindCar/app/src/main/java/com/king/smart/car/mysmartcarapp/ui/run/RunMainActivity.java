package com.king.smart.car.mysmartcarapp.ui.run;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.manager.DisplayUtil;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.ui.base.ABaseUIActivity;
import com.king.smart.car.mysmartcarapp.ui.run.view.RunMainView;
import com.king.smart.car.mysmartcarapp.ui.view.viewpager.CardFragmentPagerAdapter;
import com.king.smart.car.mysmartcarapp.ui.view.viewpager.CardPagerAdapter;
import com.king.smart.car.mysmartcarapp.ui.view.viewpager.ShadowTransformer;


public class RunMainActivity extends ABaseUIActivity {

    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    private boolean mShowingFragments = false;

    private ImageView ivRunMe, ivRunHistory;
    private RunMainView runMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_main);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                DisplayUtil.dpToPixels(2, this));
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
        mCardShadowTransformer.enableScaling(true);
        mFragmentCardShadowTransformer.enableScaling(true);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(true, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        ivRunMe = (ImageView) findViewById(R.id.iv_run_me);
        ivRunHistory = (ImageView) findViewById(R.id.iv_run_history);
        ivRunHistory.setColorFilter(getResources().getColor(R.color.color_219AE6));
        runMainView = (RunMainView) findViewById(R.id.view_run_main);
        runMainView.setHandler(handler);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    IntentActivity(RunMainActivity.this,RunTrainActivity.class);
                    int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                    if(version > 5 ){
                        overridePendingTransition(R.anim.anim_activity_in, R.anim.anim_activity_out);
                        Logger.e("RunMainActivity","version = "+version);
                    }
                    break;
            }
        }
    };
}
