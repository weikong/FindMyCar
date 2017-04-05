package com.king.smart.car.mysmartcarapp.ui.fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.manager.SDCardUtil;
import com.king.smart.car.mysmartcarapp.manager.ToolManager;
import com.king.smart.car.mysmartcarapp.ui.activity.RecordAudioActivity;
import com.king.smart.car.mysmartcarapp.ui.activity.TrackShowDemo;
import com.king.smart.car.mysmartcarapp.ui.adapter.ToolAdapter;
import com.king.smart.car.mysmartcarapp.ui.alarm.AlarmActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolFragment extends AbsBaseFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private ToolAdapter adapter;

    private ArrayList<String> dataList = new ArrayList<>();


    public ToolFragment() {
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
    public static ToolFragment newInstance(String param1, String param2) {
        ToolFragment fragment = new ToolFragment();
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
        View view = inflater.inflate(R.layout.fragment_tool, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.List_view);
        adapter = new ToolAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        adapter.setDatas(dataList);
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        String[] tools = getResources().getStringArray(R.array._items_tools);
        for (String tool : tools)
            dataList.add(tool);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            /**
             * 电话簿
             * */
            case 0:
                startContact();
                break;
            case 1:
            case 2:
//                adapter.onItemClick(view, position);
                break;
            /**
             * 相机
             * */
            case 3:
                startCameraPhone();
                break;
            /**
             * 相册
             * */
            case 4:
                startAlbum();
                break;
            /**
             * 录音
             * */
            case 5:
                IntentActivity(getActivity(), RecordAudioActivity.class);
                break;
//            /**
//             * 视频
//             * */
//            case 6:
//                break;
            /**
             * 闹铃
             * */
            case 6:
                IntentActivity(getActivity(), AlarmActivity.class);
                break;
            /**
             * 小车轨迹平滑移动
             * */
            case 7:
                IntentActivity(getActivity(), TrackShowDemo.class);
                break;
        }
    }

    /**
     * 电话簿
     */
    public void startContact() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, ToolManager.REQUEST_CONTACT);
    }

    /**
     * 相册
     */
    public void startAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ToolManager.REQUEST_ALBUM);
    }

    /**
     * 相机
     */
    public void startCameraPhone() {
        //先验证手机是否有sdcard
        if (SDCardUtil.isSDCardEnable()) {
            if (ToolManager.camera != null)
                ToolManager.releaseCamera();
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + ToolManager.localAppDir);
                if (!dir.exists())
                    dir.mkdirs();
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(dir, ToolManager.localTempImgFileName);
                if (f != null && f.exists())
                    f.delete();
                Uri u = Uri.fromFile(f);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, ToolManager.REQUEST_CAMERA_OK);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "没有找到储存目录", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "没有储存卡", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ToolManager.REQUEST_CONTACT) {
                ToolManager.getContactNum(getActivity(), data);
            } else if (requestCode == ToolManager.REQUEST_ALBUM) {

            } else if (requestCode == ToolManager.REQUEST_CAMERA_OK) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File f = new File(Environment.getExternalStorageDirectory()
                                + "/" + ToolManager.localAppDir + "/" + ToolManager.localTempImgFileName);
                        try {
                            Uri u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), f.getAbsolutePath(), null, null));
                            //u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便……
                            Logger.e(" u = " + u.getPath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public void onDetach() {
        if (adapter != null)
            ToolManager.releaseCamera();
        super.onDetach();
    }
}
