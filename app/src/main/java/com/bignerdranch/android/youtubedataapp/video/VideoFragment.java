package com.bignerdranch.android.youtubedataapp.video;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.android.youtubedataapp.R;
import com.bignerdranch.android.youtubedataapp.model.DataLab;
import com.squareup.picasso.Picasso;

public class VideoFragment extends Fragment {

    public static final String ARGS_VIDEO_ITEM_ID = "args_video_item_id";

    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mLikesTextView;
    private TextView mGoalTextView;
    private Button mGoalButton;
    private Button mOriginButton;
    private VideoItem mVideoItem;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onVideoItemUpdated(VideoItem videoItem);
    }

    public static VideoFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ARGS_VIDEO_ITEM_ID, id);
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        long id = getArguments().getLong(ARGS_VIDEO_ITEM_ID);
        mVideoItem = DataLab.get(getActivity()).getVideoById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_masterdetail, container, false);

        mImageView = v.findViewById(R.id.image_view_item);

        mTitleTextView = v.findViewById(R.id.item_title_text_view);

        mLikesTextView = v.findViewById(R.id.item_likes_text_view);

        mGoalTextView = v.findViewById(R.id.item_likes_goal_text_view);

        mGoalButton = v.findViewById(R.id.video_fragment_goal_button);
        mGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoalDialog();
            }
        });

        mOriginButton = v.findViewById(R.id.video_fragment_origin_button);
        mOriginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mVideoItem.getLink())));
            }
        });

        Picasso.get().load(mVideoItem.getThumbnailUrl()).into(mImageView);
        mTitleTextView.setText(mVideoItem.getTitle());
        mLikesTextView.setText(getResources().getString(R.string.currentLikesCount) + " " + mVideoItem.getLikesCount());
        updateGoal();

        return v;
    }

    private void updateGoal(){
        if (mVideoItem.getGoal() <= 0) {
            mGoalTextView.setText(getResources().getString(R.string.goal_empty));
        } else {
            mGoalTextView.setText(getResources().getString(R.string.goal) + " " + mVideoItem.getGoal());
        }
    }

    private void showGoalDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final RelativeLayout layout = new RelativeLayout(getActivity());
        layout.setPadding(32,32,32,32);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        final EditText edittext = new EditText(getActivity());
        edittext.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setHint("Your goal");
        layout.addView(edittext);
        //alert.setMessage("Enter Your Message");
        alert.setTitle("Enter your goal");

        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editTextValue = edittext.getText().toString();
                int n = Integer.parseInt(editTextValue);
                mVideoItem.setGoal(n);
                updateGoal();
                updateVideo();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    private void updateVideo() {
        DataLab.get(getActivity()).updateVideo(mVideoItem);
        mCallbacks.onVideoItemUpdated(mVideoItem);
    }
}
