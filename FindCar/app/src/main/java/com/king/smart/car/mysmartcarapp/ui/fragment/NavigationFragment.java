package com.king.smart.car.mysmartcarapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.LockBean;
import com.king.smart.car.mysmartcarapp.manager.LocationUtil;
import com.king.smart.car.mysmartcarapp.manager.SharePreferceTool;
import com.king.smart.car.mysmartcarapp.ui.activity.LocalRouteActivity;
import com.king.smart.car.mysmartcarapp.ui.view.dialog.MyInputDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends AbsBaseFragment implements View.OnClickListener, View.OnLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvDrive;
    private EditText etAddress;
    private LinearLayout layoutHome, layoutCompany;
    private LocationUtil locationUtil;
    private String mStrAddress;
    private MyInputDialog myInputDialog;
    private int TypeDefault = -1;
    private int TypeHome = 1;
    private int TypeCompany = 2;
    private ImageView iv_clear_input;

    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavigationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigationFragment newInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etAddress.addTextChangedListener(watcher);
        tvDrive = (TextView) view.findViewById(R.id.tvDrive);
        layoutHome = (LinearLayout) view.findViewById(R.id.layout_home);
        layoutCompany = (LinearLayout) view.findViewById(R.id.layout_company);
        iv_clear_input = (ImageView)view.findViewById(R.id.iv_clear_input);
        tvDrive.setOnClickListener(this);
        layoutHome.setOnClickListener(this);
        layoutCompany.setOnClickListener(this);
        layoutHome.setOnLongClickListener(this);
        layoutCompany.setOnLongClickListener(this);
        iv_clear_input.setOnClickListener(this);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s))
                iv_clear_input.setVisibility(View.INVISIBLE);
            else
                iv_clear_input.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDrive:
                getDataJump();
                break;
            case R.id.layout_home:
                if (getHomeLocation()){
                    LockBean bean = new LockBean();
                    bean.setmAddress(homeAddress).setmLat(homeLat).setmLon(homeLon);
                    bundleIntent(bean);
                    return;
                }
                TypeDefault = TypeHome;
                popDialog();
                break;
            case R.id.layout_company:
                if (getCompanyLocation()){
                    LockBean bean = new LockBean();
                    bean.setmAddress(companyAddress).setmLat(companyLat).setmLon(companyLon);
                    bundleIntent(bean);
                    return;
                }
                TypeDefault = TypeCompany;
                popDialog();
                break;
            case R.id.iv_clear_input:
                String strInput = etAddress.getText().toString();
                if (!TextUtils.isEmpty(strInput))
                    etAddress.setText("");
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.layout_home:
                TypeDefault = TypeHome;
                popDialog();
                break;
            case R.id.layout_company:
                TypeDefault = TypeCompany;
                popDialog();
                break;
        }
        return true;
    }

    private void getDataJump() {
        mStrAddress = etAddress.getText().toString();
        LockBean bean = null;
        if (!TextUtils.isEmpty(mStrAddress)) {
            bean = new LockBean();
            bean.setmAddress(mStrAddress);
        }
        bundleIntent(bean);
    }

    private void bundleIntent(LockBean bean){
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", bean);
        IntentBundleActivity(getActivity(), bundle, LocalRouteActivity.class);
    }

    private void popDialog() {
        if (myInputDialog == null){
            myInputDialog = new MyInputDialog(getActivity());
            myInputDialog.setOnItemInDlgClickLinster(dlgClickListener);
        }
        myInputDialog.show();
    }

    MyInputDialog.OnItemInDlgClickListener dlgClickListener = new MyInputDialog.OnItemInDlgClickListener() {
        @Override
        public void onItemClick(int type, String input) {
            switch (type){
                case MyInputDialog.TypeConfirm:
                    if (!TextUtils.isEmpty(input)){
                        etAddress.setText(input);
                        if (TypeDefault == TypeHome){
                            homeAddress = input;
                            homeLat = 0;
                            homeLon = 0;
                            saveHomeLocation(homeAddress,homeLat,homeLon);
                        } else if (TypeDefault == TypeCompany){
                            companyAddress = input;
                            companyLat = 0;
                            companyLon = 0;
                            saveCompanyLocation(companyAddress,companyLat,companyLon);
                        }
                    }
                    break;
                case MyInputDialog.TypeCancle:
                    break;
                case MyInputDialog.TypeGps:
                    if (locationUtil == null) {
                        locationUtil = new LocationUtil(false);
                        locationUtil.setReceiveListener(listener);
                    }
                    locationUtil.startLocationClient(getActivity());
                    break;
            }
        }
    };

    LocationUtil.ReceiveListener listener = new LocationUtil.ReceiveListener() {
        @Override
        public void setLocation(BDLocation location) {
            if (location != null){
                if (TextUtils.isEmpty(location.getAddrStr())){
                    Toast.makeText(getActivity(),"定位失败",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String address = location.getAddrStr();
                    myInputDialog.setEtInput(address);
                    if (TypeDefault == TypeHome){
                        homeAddress = location.getAddrStr();
                        homeLat = location.getLatitude();
                        homeLon = location.getLongitude();
                        saveHomeLocation(homeAddress,homeLat,homeLon);
                    } else if (TypeDefault == TypeCompany){
                        companyAddress = location.getAddrStr();
                        companyLat = location.getLatitude();
                        companyLon = location.getLongitude();
                        saveCompanyLocation(companyAddress,companyLat,companyLon);
                    }
                }
            }
        }
    };

    private String homeAddress,companyAddress;
    private double homeLat,homeLon,companyLat,companyLon;

    private void saveHomeLocation(String address,double lat,double lon){
        SharePreferceTool.getInstance().setCache("Home-Address",address);
        SharePreferceTool.getInstance().setCache("Home-Latitude",lat);
        SharePreferceTool.getInstance().setCache("Home-Longitude",lon);
    }

    private void saveCompanyLocation(String address,double lat,double lon){
        SharePreferceTool.getInstance().setCache("Company-Address",address);
        SharePreferceTool.getInstance().setCache("Company-Latitude",lat);
        SharePreferceTool.getInstance().setCache("Company-Longitude",lon);
    }

    private boolean getHomeLocation(){
        boolean haveData = false;
        homeAddress = SharePreferceTool.getInstance().getString("Home-Address");
        homeLat = SharePreferceTool.getInstance().getFloat("Home-Latitude");
        homeLon = SharePreferceTool.getInstance().getFloat("Home-Longitude");
        if (!TextUtils.isEmpty(homeAddress) || (homeLat != 0 && homeLon != 0))
            haveData = true;
        return haveData;
    }

    private boolean getCompanyLocation(){
        boolean haveData = false;
        companyAddress = SharePreferceTool.getInstance().getString("Company-Address");
        companyLat = SharePreferceTool.getInstance().getFloat("Company-Latitude");
        companyLon = SharePreferceTool.getInstance().getFloat("Company-Longitude");
        if (!TextUtils.isEmpty(companyAddress) || (companyLat != 0 && companyLon != 0))
            haveData = true;
        return haveData;
    }
}
