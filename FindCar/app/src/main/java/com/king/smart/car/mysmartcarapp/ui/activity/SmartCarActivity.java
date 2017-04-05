package com.king.smart.car.mysmartcarapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.WeatherBean;
import com.king.smart.car.mysmartcarapp.manager.DisplayUtil;
import com.king.smart.car.mysmartcarapp.manager.NetWorkUtil;
import com.king.smart.car.mysmartcarapp.manager.OkHttpClientManager;
import com.king.smart.car.mysmartcarapp.manager.ToastUtil;
import com.king.smart.car.mysmartcarapp.manager.WeatherUtil;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class SmartCarActivity extends ABaseUIActivity implements View.OnClickListener, View.OnTouchListener {
    private final String TAG = SmartCarActivity.class.getSimpleName();

    private ViewPager viewPager;
    private Fragment tipsFragment, navigationFragment, lockFragment, musicFragment, toolFragment;
    private NavigationBarView navigationBarView;
    private TextView tvPartCar;
    private TextView tvCity, tvTemperature, tvWeather, tvAir;
    private ImageView iv_bg_weather;

    private int mPosition = 0;
    private SDKReceiver mReceiver;
    private WeatherBean weatherBean;
    private final int REFRESH_COMPLETE = 0X110;
    private RelativeLayout layout_top_air;
    private ImageView ivProgress;
    private Animation anim1, anim2;
    private float touchDownX, touchDownY, touchUpX, touchUpY;
    private int exitCount = 2;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //保持屏幕
        setContentView(R.layout.activity_smart_car);
        registerMapReceiver();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        callMathAlarmScheduleService();
        MusicFragment fragment = (MusicFragment) musicFragment;
        if (fragment != null && fragment.getUserVisibleHint()) {
            fragment.initMediaPlaying();
        }
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

    private void unRegisterMapReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @SuppressWarnings("ResourceAsColor")
    private void initView() {
        iv_bg_weather = (ImageView) findViewById(R.id.iv_bg_weather);
        layout_top_air = (RelativeLayout) findViewById(R.id.layout_top_air);
        navigationBarView = (NavigationBarView) findViewById(R.id.layout_navigation);
        navigationBarView.setCheckPageListener(checkPageListener);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setOffscreenPageLimit(5);
        tvPartCar = (TextView) findViewById(R.id.tvPartCar);
        tvPartCar.setOnClickListener(this);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvWeather = (TextView) findViewById(R.id.tv_weather);
        tvAir = (TextView) findViewById(R.id.tv_air);
        ivProgress = (ImageView) findViewById(R.id.iv_progress);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.rotate_loading_visiable);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.rotate_loading_invisiable);
        layout_top_air.setOnTouchListener(this);
    }

    private final float SlideHeight = DisplayUtil.screenHeight / 3.0f;
    private boolean isRefresh = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        float minus = 0;
        float displayAlpha = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                minus = y - touchDownY;
                displayAlpha = minus / SlideHeight;
                if (displayAlpha >= 1) {
                    displayAlpha = 1.0f;
                } else if (displayAlpha < 0) {
                    displayAlpha = 0.0f;
                }
                ivProgress.setAlpha(displayAlpha);
                ivProgress.setRotation(minus);
                return true;
            case MotionEvent.ACTION_UP:
                minus = y - touchDownY;
                displayAlpha = minus / SlideHeight;
                if (displayAlpha >= 1) {
                    httpWeatherTask();
                } else {
                    ivProgress.setAlpha(0.0f);
                }
                return false;
        }
        return false;
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
                        if (WeatherUtil.weatherData == null) {
                            httpWeatherTask();
                        } else {
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = WeatherUtil.weatherData;
                            handler.sendMessage(message);
                        }
                    }
                    return tipsFragment;
                case 1:
                    if (lockFragment == null) {
                        lockFragment = new LockFragment().setParentHandler(handler);
                    }
                    return lockFragment;
                case 2:
                    if (navigationFragment == null) {
                        navigationFragment = new NavigationFragment();
                    }
                    return navigationFragment;
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
                if (mPosition != 1)
                    viewPager.setCurrentItem(1, true);
                if (lockFragment != null) {
                    LockFragment fragment = (LockFragment) lockFragment;
                    if (fragment.isLongClick) {
                        Message message = Message.obtain();
                        message.what = 3;
                        message.obj = false;
                        handler.sendMessage(message);
                        fragment.setLongClick(false);
                        fragment.delSelectData();
                    } else {
                        fragment.addData();
                    }
                }
                break;
        }
    }

    private void httpWeatherTask() {
        ivProgress.clearAnimation();
        ivProgress.startAnimation(anim1);
        if (!NetWorkUtil.haveNetWork(this)) {
            ToastUtil.show("请检查网络");
            handler.sendEmptyMessageDelayed(2, 800);
            return;
        }
        OkHttpClientManager.getInstance()._getAsyn(WeatherUtil.getWeatherUrl("四川", "成都"), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.show("获取天气信息失败");
                handler.sendEmptyMessageDelayed(2, 800);
            }

            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    ToastUtil.show("获取天气信息失败");
                    handler.sendEmptyMessageDelayed(2, 800);
                    return;
                }
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
                } finally {
                    handler.sendEmptyMessageDelayed(2, 800);
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
                    weatherBean = WeatherUtil.parsonWeatherData(jsonData);
                    if (weatherBean == null)
                        return;
                    checkDayOrNight(weatherBean);
                    ((TipsFragment) tipsFragment).loadData(weatherBean);
                    tvCity.setText(weatherBean.getCity());
                    tvTemperature.setText(weatherBean.getTemperature());
                    tvWeather.setText(weatherBean.getWeather());
                    tvAir.setText(weatherBean.getAirCondition());
                    break;
                case 2:
                    ivProgress.clearAnimation();
                    ivProgress.startAnimation(anim2);
                    handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 300);
                    break;

                case 3:
                    boolean isLongClick = (boolean) msg.obj;
                    if (!isLongClick) {
                        tvPartCar.setText("P");
                        tvPartCar.setCompoundDrawables(null, null, null, null);
                    } else {
                        Drawable drawable = getResources()
                                .getDrawable(R.drawable.action_delete_normal);
                        /// 这一步必须要做,否则不会显示.
//                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
//                                drawable.getMinimumHeight());
                        drawable.setBounds(0, 0, DisplayUtil.dp2px(40),
                                DisplayUtil.dp2px(40));
                        tvPartCar.setText(null);
                        tvPartCar.setCompoundDrawables(null, drawable, null, null);
                    }
                    break;

                case REFRESH_COMPLETE:
                    ivProgress.clearAnimation();
                    ivProgress.setAlpha(0.0f);
                    break;
            }
        }
    };

    private void checkDayOrNight(WeatherBean bean) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String strRiseTime = bean.getSunRise();
        String strSetTime = bean.getSunSet();
        if (TextUtils.isEmpty(strRiseTime) || TextUtils.isEmpty(strSetTime)
                || !strRiseTime.contains(":") || !strSetTime.contains(":"))
            return;
        String[] nowTime = str.split(":");
        String[] riseTime = strRiseTime.split(":");
        String[] setTime = strSetTime.split(":");
        int nowTimeH = Integer.parseInt(nowTime[0]);
        int nowTimeM = Integer.parseInt(nowTime[1]);
        int riseTimeH = Integer.parseInt(riseTime[0]);
        int riseTimeM = Integer.parseInt(riseTime[1]);
        int setTimeH = Integer.parseInt(setTime[0]);
        int setTimeM = Integer.parseInt(setTime[1]);
        if (nowTimeH > riseTimeH && nowTimeH < setTimeH) {
            //day
            iv_bg_weather.setImageResource(R.drawable.bg_sunny_day);
        } else if ((nowTimeH == riseTimeH && nowTimeM > riseTimeM)
                || (nowTimeH == setTimeM && nowTimeM < setTimeM)) {
            //day
            iv_bg_weather.setImageResource(R.drawable.bg_sunny_day);
        } else {
            //night
            iv_bg_weather.setImageResource(R.drawable.bg_sunny_night);
        }
    }

    @Override
    public void onBackPressed() {
        if (lockFragment != null) {
            LockFragment fragment = (LockFragment) lockFragment;
            if (fragment.isLongClick) {
                Message message = Message.obtain();
                message.what = 3;
                message.obj = false;
                handler.sendMessage(message);
                fragment.setLongClick(false);
                fragment.notifyDataChanged();
                return;
            }
        }
        if (--exitCount > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitCount = 2;
                }
            }, 2000);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        unRegisterMapReceiver();
        Log.e(TAG, "Destroy");
        super.onDestroy();
    }
}
