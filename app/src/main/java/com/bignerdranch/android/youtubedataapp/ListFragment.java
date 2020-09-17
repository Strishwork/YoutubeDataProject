package com.bignerdranch.android.youtubedataapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.youtubedataapp.model.DataLab;
import com.bignerdranch.android.youtubedataapp.video.VideoDialog;
import com.bignerdranch.android.youtubedataapp.video.VideoItem;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class ListFragment extends Fragment {

    public static final int CHECK_DIALOG_REQUEST_CODE = 0;
    public static final int DELETE_DIALOG_REQUEST_CODE = 1;

    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    private DataLab mDataLab;

    public static ListFragment newInstance(){
        ListFragment fragment = new ListFragment();
        return  fragment;
    }

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onVideoSelected(VideoItem videoItem);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        AlarmReceiver.setAlarm(getActivity(), true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mDataLab = DataLab.get(getActivity());

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                openDeleteDialog((long) viewHolder.itemView.getTag(), viewHolder.getAdapterPosition());
                //mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(mRecyclerView);

        updateVideosInfo();
        updateUI();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_video:
                openDialog();
                return true;
            case R.id.menu_item_update:
                updateVideosInfo();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDialog(){
        VideoDialog dialog = VideoDialog.newInstance();
        dialog.setTargetFragment(this, CHECK_DIALOG_REQUEST_CODE);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    private void openDeleteDialog(long id, int pos){
        DeleteDialog dialog = DeleteDialog.newInstance(id, pos);
        dialog.setCancelable(false);
        dialog.setTargetFragment(this, DELETE_DIALOG_REQUEST_CODE);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    private void checkVideoData(String s, VideoItem v){
        new LongOperation(s, v).execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == CHECK_DIALOG_REQUEST_CODE){
            String link = data.getStringExtra(VideoDialog.LINK);
            checkVideoData(link, null);
        } else if(requestCode == DELETE_DIALOG_REQUEST_CODE){
            boolean delete = data.getBooleanExtra(DeleteDialog.DELETE, false);
            if(delete){
                long id = data.getLongExtra(DeleteDialog.ARGS_ID, -1);
                removeVideo(id);
            } else{
                int pos = data.getIntExtra(DeleteDialog.ARGS_POS, -1);
                mAdapter.notifyItemChanged(pos);
            }
        }
    }

    private void updateVideosInfo(){
        List<VideoItem> videos = mDataLab.getVideos();
        if(videos.size() > 0){
            for (int i = 0; i < videos.size(); i++){
//                Log.i("DEBUG", videos.get(i).getTitle());
//                Log.i("DEBUG", videos.get(i).getId() + "   ID");
                checkVideoData(videos.get(i).getLink(), videos.get(i));
            }
        }
    }

    public void updateUI(){
        List<VideoItem> videos = mDataLab.getVideos();
        if (mAdapter == null) {
            mAdapter = new VideoAdapter(videos);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setVideos(videos);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void removeVideo(long id){
        mDataLab.removeVideo(id);
        updateUI();
    }

    private class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mLikesTextView;
        private TextView mGoalTextView;
        public VideoItem mVideoItem;

        public VideoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = itemView.findViewById(R.id.image_view_list);
            mTitleTextView = itemView.findViewById(R.id.list_item_title_text_view);
            mLikesTextView = itemView.findViewById(R.id.list_item_likes_text_view);
            mGoalTextView = itemView.findViewById(R.id.list_item_likes_goal_text_view);
        }

        public void bindVideo(VideoItem video) {
            mVideoItem = video;
            Picasso.get().load(mVideoItem.getThumbnailUrl()).fit().centerCrop().into(mImageView);
            mTitleTextView.setText(mVideoItem.getTitle());
            mLikesTextView.setText("Likes count: " +String.valueOf(mVideoItem.getLikesCount()));
            if (mVideoItem.getGoal() <= 0) {
                mGoalTextView.setText(getResources().getString(R.string.goal_empty));
            } else {
                mGoalTextView.setText(getResources().getString(R.string.goal) + " " + mVideoItem.getGoal());
            }

        }

        @Override
        public void onClick(View v) {
            mCallbacks.onVideoSelected(mVideoItem);
        }
    }

    private class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
        private List<VideoItem> mVideos;

        public VideoAdapter(List<VideoItem> videos) {
            mVideos = videos;
        }

        @Override
        public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_2, parent, false);
            return new VideoHolder(view);
        }
        @Override
        public void onBindViewHolder(VideoHolder holder, int position) {
            VideoItem video = mVideos.get(position);
            holder.itemView.setTag(video.getId());
            holder.bindVideo(video);
        }

        @Override
        public int getItemCount() {
            return mVideos.size();
        }

        public void setVideos(List<VideoItem> videos) {
            mVideos = videos;
        }

    }

    private class LongOperation extends AsyncTask<Void, Void, VideoItem> {

        String mString;
        VideoItem mItem;

        public LongOperation(String s, VideoItem item){
            mString = s;
            mItem = item;
        }

        @Override
        protected VideoItem doInBackground(Void... params) {
            try {
                return new YouTubeAsync().getVideoByLink(mString);
            } catch (IOException ioe) {
                Log.e("DEBUG", "Failed to fetch items", ioe);
            }
            return null;
        }

        @Override
        protected void onPostExecute(VideoItem videoItem) {

            if(videoItem == null){
                Toast.makeText(getActivity(), "Video was not found", Toast.LENGTH_LONG).show();
                return;
            }

            if(mItem == null){
                mDataLab.addVideo(videoItem);
                mAdapter.notifyItemInserted(mDataLab.getVideos().size() - 1);
                updateUI();
            } else{
                mItem.updateItem(videoItem);
                mDataLab.updateVideo(mItem);
                updateUI();
            }
//            Log.i("DEBUG", videoItem.getTitle() + "     " + videoItem.getLikesCount());
        }
    }

}
