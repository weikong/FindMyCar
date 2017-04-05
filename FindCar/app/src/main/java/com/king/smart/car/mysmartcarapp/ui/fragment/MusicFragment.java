package com.king.smart.car.mysmartcarapp.ui.fragment;


import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.MediaEntity;
import com.king.smart.car.mysmartcarapp.bean.MediaStateEntity;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.manager.MediaPlayerControll;
import com.king.smart.car.mysmartcarapp.manager.MediaUtil;
import com.king.smart.car.mysmartcarapp.manager.SharePreferceTool;
import com.king.smart.car.mysmartcarapp.ui.activity.MusicPlayActivity;
import com.king.smart.car.mysmartcarapp.ui.adapter.MusicAdapter;
import com.king.smart.car.mysmartcarapp.ui.view.MusicBottomControllView;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends AbsBaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //    private List<MediaEntity> datas;
    private ListView listView;
    private MusicAdapter adapter;
    private int selectIndex = -1;
    private MusicBottomControllView musicBottomControllView;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = 0;
    private MediaPlayerControll mediaPlayerControll;
    private String mediaPath = "";

    private final int TYPE_LOOP_LIST = 1; //列表循环
    private final int TYPE_LOOP_SINGLE = 2; //单曲循环
    private final int TYPE_LOOP_RANDOM = 3; //随机
    private int mTypeLoop = TYPE_LOOP_LIST;

    public MusicFragment() {
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
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
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
//        mediaPlayerControll = new MediaPlayerControll(getActivity());
        mediaPlayerControll = MediaPlayerControll.getInstance();
        mediaPlayerControll.setPlayerListener(playerListener);
        mediaPlayerControll.initMediaPlayer();
        if (MediaUtil.mediaEntityList == null) {
            QueryMusicTask task = new QueryMusicTask();
            task.execute("query");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        initView(view);
        return view;
    }

    public void initMediaPlaying() {
        if (mediaPlayerControll != null && mediaPlayerControll.getPlayerListener() == null)
            mediaPlayerControll.setPlayerListener(playerListener);
        if (MediaUtil.mediaEntityList != null) {
            MediaStateEntity entity = MediaUtil.getMediaState();
            mPlay = entity.getStatePlay();
            selectIndex = entity.getSelectIndex();
            mediaPath = selectIndex == -1 ? "" : MediaUtil.mediaEntityList.get(selectIndex).path;
            mTypeLoop = SharePreferceTool.getInstance().getInt("TYPE_LOOP", TYPE_LOOP_LIST);
            musicBottomControllView.setMenuIcon(mTypeLoop);
            if (!checkSelect()) {
                adapter.notifyDataSetChanged();
                changeBottomView();
            } else {
                if (mPlay > 0)
                    musicBottomControllView.playMusic(mPlay);
                changeBottomView();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            savePlayingState(mPlay);
        }
    }

    @Override
    public void onDestroyView() {
        savePlayingState(mPlay);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        savePlayingState(0);
        mediaPlayerControll.releaseMediaPlayer();
        super.onDetach();
    }

    public boolean queryMusics() {
        if (MediaUtil.mediaEntityList == null)
            MediaUtil.mediaEntityList = MediaUtil.getAllMediaList(getActivity(), null);
        return true;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listview);
        musicBottomControllView = (MusicBottomControllView) view.findViewById(R.id.viewControll);
        musicBottomControllView.setClickListener(clickListener);
        musicBottomControllView.setOnClickListener(this);
        adapter = new MusicAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if (MediaUtil.mediaEntityList != null) {
            adapter.setDatas(MediaUtil.mediaEntityList);
            adapter.notifyDataSetChanged();
        }
        initMediaPlaying();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == selectIndex) {
            if (mPlay != TYPE_PLAY)
                musicBottomControllView.playMusic(TYPE_PLAY);
            return;
        }
        selectIndex = position;
        checkSelect();
        changeBottomView();
        musicBottomControllView.playMusic(TYPE_PLAY);
    }

    private boolean checkSelect() {
        if (selectIndex == -1)
            return false;
        adapter.setSelectIndex(selectIndex);
        adapter.setmPlay(mPlay);
        adapter.notifyDataSetChanged();
        return true;
    }

    private void changeBottomView() {
        try {
            if (MediaUtil.mediaEntityList == null)
                return;
            MediaEntity entity = null;
            if (selectIndex >= (MediaUtil.mediaEntityList.size() - 1) || selectIndex < 0)
                entity = MediaUtil.mediaEntityList.get(0);
            else
                entity = MediaUtil.mediaEntityList.get(selectIndex);
            musicBottomControllView.setTvActionTitleText(entity.title);
            musicBottomControllView.setTvActionNameText(entity.singer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MusicBottomControllView.ClickListener clickListener = new MusicBottomControllView.ClickListener() {

        @Override
        public void actionMenu(int type) {
            mTypeLoop = type;
            SharePreferceTool.getInstance().setCache("TYPE_LOOP", mTypeLoop);
        }

        @Override
        public void actionPlay(int play) {
            if (play == TYPE_PLAY) {
                if (selectIndex >= (MediaUtil.mediaEntityList.size() - 1) || selectIndex < 0)
                    selectIndex = 0;
                String path = MediaUtil.mediaEntityList.get(selectIndex).path;
                if (!TextUtils.isEmpty(path)) {
                    if (mPlay == 0 || !path.equals(mediaPath))
                        mediaPlayerControll.setMediaSource(path);
                    else
                        mediaPlayerControll.playMedia();
                    mediaPath = path;
                }
            } else {
                mediaPlayerControll.pauseMedia();
            }
            mPlay = play;
            checkSelect();
            changeBottomView();
        }

        @Override
        public void actionNext() {
            playNextOne();
        }
    };

    private void playNextOne() {
        if (MediaUtil.mediaEntityList == null || MediaUtil.mediaEntityList.size() == 0)
            return;
        if (mTypeLoop == TYPE_LOOP_LIST) {
            if (selectIndex >= (MediaUtil.mediaEntityList.size() - 1))
                selectIndex = 0;
            else
                selectIndex++;
        } else if (mTypeLoop == TYPE_LOOP_RANDOM) {
            try {
                Random random = new Random();
                selectIndex = random.nextInt(MediaUtil.mediaEntityList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        checkSelect();
        changeBottomView();
        musicBottomControllView.playMusic(TYPE_PLAY);
    }


    MediaPlayerControll.PlayerListener playerListener = new MediaPlayerControll.PlayerListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            playNextOne();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }
    };

    private void savePlayingState(int state_play) {
        if (mPlay > 0) {
            MediaStateEntity entity = new MediaStateEntity();
            entity.setId(MediaUtil.mediaEntityList.get(selectIndex).id)
                    .setSelectIndex(selectIndex)
                    .setStatePlay(state_play)
                    .setLoopType(mTypeLoop)
                    .setCurrentTime(mediaPlayerControll.initMediaPlayer().getCurrentPosition())
                    .setDuration(mediaPlayerControll.initMediaPlayer().getDuration());
            MediaUtil.setMediaState(entity);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewControll:
                savePlayingState(mPlay);
                Bundle bundle = new Bundle();
                bundle.putInt("SelectIndex", selectIndex);
                bundle.putInt("PlayState", mPlay);
                IntentBundleActivity(getActivity(), bundle, MusicPlayActivity.class);
                break;
        }
    }

    private class QueryMusicTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            Logger.e("QueryMusicTask", "start");
            showProgreessDialog();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Logger.e("QueryMusicTask", "doInBackground");
            return queryMusics();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Logger.e("QueryMusicTask", "result = " + aBoolean);
            dismissProgressDialog();
            adapter.setDatas(MediaUtil.mediaEntityList);
            adapter.notifyDataSetChanged();
            checkSelect();
            changeBottomView();
        }
    }
}
