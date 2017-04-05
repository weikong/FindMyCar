package com.king.smart.car.mysmartcarapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.WeatherBean;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.manager.OkHttpClientManager;
import com.king.smart.car.mysmartcarapp.manager.WeatherUtil;
import com.king.smart.car.mysmartcarapp.ui.adapter.LockAdapter;
import com.king.smart.car.mysmartcarapp.ui.base.ABaseUIActivity;
import com.king.smart.car.mysmartcarapp.ui.fragment.LockFragment;
import com.king.smart.car.mysmartcarapp.ui.fragment.MusicFragment;
import com.king.smart.car.mysmartcarapp.ui.fragment.NavigationFragment;
import com.king.smart.car.mysmartcarapp.ui.fragment.TipsFragment;
import com.king.smart.car.mysmartcarapp.ui.fragment.ToolFragment;
import com.king.smart.car.mysmartcarapp.ui.view.NavigationBarView;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class ChoiceCityActivity extends ABaseUIActivity implements View.OnClickListener {
    private final String TAG = ChoiceCityActivity.class.getSimpleName();

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager viewPager;
    private Fragment tipsFragment, navigationFragment, lockFragment, musicFragment, toolFragment;
    private NavigationBarView navigationBarView;
    private TextView tvPartCar;
    private TextView tvCity, tvTemperature, tvWeather, tvAir;

    private ArrayList<String> dataList = new ArrayList<>();
    private LockAdapter mAdapter;
    private int mPosition = 0;
    private SDKReceiver mReceiver;
    private WeatherBean tipsBean;

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(TAG, "action: " + s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        registerMapReceiver();
        initView();
        getWeatherTask();
    }

    private void registerMapReceiver() {
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
    }

    private void initView() {
//        ImageView bannner = (ImageView) findViewById(R.id.bannner);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("多云 23°");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        navigationBarView = (NavigationBarView) findViewById(R.id.layout_navigation);
        navigationBarView.setCheckPageListener(checkPageListener);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        tvPartCar = (TextView) findViewById(R.id.tvPartCar);
        tvPartCar.setOnClickListener(this);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvWeather = (TextView) findViewById(R.id.tv_weather);
        tvAir = (TextView) findViewById(R.id.tv_air);
    }

    /**
     * 初始化FragmentPagerAdapter
     */
    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (tipsFragment == null) {
                        tipsFragment = new TipsFragment();
                    }
                    return tipsFragment;
                case 1:
                    if (navigationFragment == null) {
                        navigationFragment = new NavigationFragment();
                    }
                    return navigationFragment;
                case 2:
                    if (lockFragment == null) {
                        lockFragment = new LockFragment();
                    }
                    return lockFragment;
                case 3:
                    if (musicFragment == null) {
                        musicFragment = new MusicFragment();
                    }
                    return musicFragment;
                case 4:
                    if (toolFragment == null) {
                        toolFragment = new ToolFragment();
                    }
                    return toolFragment;

                default:
                    return new TipsFragment();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            navigationBarView.checkItemView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    NavigationBarView.CheckPageListener checkPageListener = new NavigationBarView.CheckPageListener() {
        @Override
        public void junpIndex(int index) {
            viewPager.setCurrentItem(index, true);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPartCar:
                if (mPosition != 2)
                    viewPager.setCurrentItem(2, true);
                if (lockFragment != null)
                    ((LockFragment) lockFragment).addData();
                break;
        }
    }

    private void getWeatherTask() {
        OkHttpClientManager.getInstance()._getAsyn(WeatherUtil.httpWeaher, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(String response) {
                Logger.e("res = " + response);
                if (TextUtils.isEmpty(response))
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.optString("msg");
                    String retCode = jsonObject.optString("retCode");
                    if (!TextUtils.isEmpty(retCode) && retCode.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("result");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            JSONObject jsonData = jsonArray.optJSONObject(0);
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = jsonData;
                            handler.sendMessage(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject jsonData = (JSONObject) msg.obj;
                    initWeatherData(jsonData);
                    tipsBean = initWeatherData(jsonData);
                    if (tipsBean == null)
                        return;
                    ((TipsFragment) tipsFragment).loadData(tipsBean);
                    tvCity.setText(tipsBean.getCity());
                    tvTemperature.setText(tipsBean.getTemperature());
                    tvWeather.setText(tipsBean.getWeather());
                    tvAir.setText(tipsBean.getAirCondition());
                    collapsingToolbarLayout.setTitle(tipsBean.getWeather() + " " + tipsBean.getTemperature());
                    break;
            }
        }
    };

    private WeatherBean initWeatherData(JSONObject jsonData) {
        if (jsonData == null)
            return null;
        String mStrAir = jsonData.optString("airCondition");
        String mStrCity = jsonData.optString("city");
        String mStrColdIndex = jsonData.optString("coldIndex");
        String mStrDressingIndex = jsonData.optString("dressingIndex");
        String mStrExerciseIndex = jsonData.optString("exerciseIndex");
        String mStrPollutionIndex = jsonData.optString("pollutionIndex");
        String mStrWashIndex = jsonData.optString("washIndex");
        String mStrWeek = jsonData.optString("week");
        String mStrWind = jsonData.optString("wind");
        String mStrUpdateTime = jsonData.optString("updateTime");
        String mStrSunRise = jsonData.optString("sunrise");
        String mStrSunSet = jsonData.optString("sunset");
        String mStrTemperature = jsonData.optString("temperature");
        String mStrWeather = jsonData.optString("weather");
        tipsBean = new WeatherBean();
        tipsBean.setCity(mStrCity).setTemperature(mStrTemperature).setWeather(mStrWeather)
                .setAirCondition(mStrAir).setColdIndex(mStrColdIndex).setDressingIndex(mStrDressingIndex)
                .setExerciseIndex(mStrExerciseIndex).setPollutionIndex(mStrPollutionIndex).setWashIndex(mStrWashIndex)
                .setWeek(mStrWeek).setWind(mStrWind).setUpdateTime(mStrUpdateTime).setSunRise(mStrSunRise)
                .setSunSet(mStrSunSet);
        return tipsBean;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Destroy");
    }
}
