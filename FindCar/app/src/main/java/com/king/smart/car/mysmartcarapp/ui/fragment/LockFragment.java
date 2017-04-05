package com.king.smart.car.mysmartcarapp.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.baidu.location.BDLocation;
import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.LockBean;
import com.king.smart.car.mysmartcarapp.db.helper.LockDBHelper;
import com.king.smart.car.mysmartcarapp.manager.LocationUtil;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.ui.activity.LocalRouteActivity;
import com.king.smart.car.mysmartcarapp.ui.adapter.LockAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LockFragment extends AbsBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ArrayList<LockBean> dataList = new ArrayList<>();
    private LockAdapter mAdapter;
    private Handler mHandler = new Handler();

    private final String mName = "天府软件园 ";
    private LocationUtil locationUtil;

    public boolean isLongClick = false;

    private Handler parentHandler;

    public boolean isLongClick() {
        return isLongClick;
    }

    public LockFragment setLongClick(boolean longClick) {
        isLongClick = longClick;
        if (mAdapter != null)
            mAdapter.setLongClick(longClick);
        return this;
    }

    public LockFragment() {
    }

    public LockFragment setParentHandler(Handler parentHandler) {
        this.parentHandler = parentHandler;
        return this;
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
    public static LockFragment newInstance(String param1, String param2) {
        LockFragment fragment = new LockFragment();
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
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lock, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new LockAdapter(getActivity(), handler);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new LockAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int pos) {
                LockBean bean = mAdapter.getDataList().get(pos);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", bean);
                IntentBundleActivity(getActivity(), bundle, LocalRouteActivity.class);
            }

            @Override
            public void onItemLongClick(View view, int pos) {
//                removeAddressData(pos);

            }
        });
        mAdapter.setLongClick(isLongClick);
        mAdapter.setDatas(dataList);
        mAdapter.notifyDataSetChanged();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isLongClick = (boolean) msg.obj;
                    if (parentHandler != null) {
                        Message message = Message.obtain();
                        message.what = 3;
                        message.obj = isLongClick;
                        parentHandler.sendMessage(message);
                    }
                    break;
            }
        }
    };

    /**
     * 加载数据
     */
    private void loadData() {
        ArrayList<LockBean> list = LockDBHelper.query();
        dataList.clear();
        dataList.addAll(list);
    }

    public void addData() {
        showProgreessDialog();
        if (locationUtil == null) {
            locationUtil = new LocationUtil(false);
            locationUtil.setReceiveListener(listener);
        }
        locationUtil.startLocationClient(getActivity());
    }

    public void insertDB(BDLocation location) {
        dismissProgressDialog();
        if (location == null)
            return;
        LockBean bean = new LockBean();
        bean.setmTime(location.getTime());
        bean.setmAddress(location.getAddrStr());
        bean.setmNearBy(location.getLocationDescribe());
        bean.setmLat(location.getLatitude());
        bean.setmLon(location.getLongitude());
        boolean isInsert = LockDBHelper.insert(bean);
        if (isInsert) {
            loadData();
            mAdapter.setDatas(dataList);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public void removeAddressData(LockBean bean, int position) {
        if (LockDBHelper.delete(bean)) {
            Logger.e("position = " + position + "  getmID = " + bean.getmID());
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemChanged(position);
        }
    }

    public void delSelectData() {
        showProgreessDialog();
        int size = mAdapter.getDataList().size();
        boolean isDelData = false;
        for (int i = 0; i < size; i++) {
            LockBean bean = mAdapter.getDataList().get(i);
            if (bean.isCheck()) {
                isDelData = true;
                removeAddressData(bean, i);
            }
        }
        if (isDelData) {
            loadData();
            mAdapter.setDatas(dataList);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        }, 200);
        dismissProgressDialog();
    }

    public void notifyDataChanged() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    LocationUtil.ReceiveListener listener = new LocationUtil.ReceiveListener() {
        @Override
        public void setLocation(BDLocation location) {
            insertDB(location);
        }
    };

    @Override
    public void onDestroyView() {
        Logger.e("onDestroyView", "LockFragment");
        if (mAdapter != null)
            dataList = mAdapter.getDataList();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (locationUtil != null) {
            locationUtil.destoryLocationClient();
            locationUtil = null;
        }
        super.onDestroy();
    }
}
