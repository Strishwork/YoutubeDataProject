package com.bignerdranch.android.youtubedataapp.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bignerdranch.android.youtubedataapp.R;

public class VideoDialog extends DialogFragment {

    public static final String LINK = "link";

    private EditText editTextLink;

    public static VideoDialog newInstance() {
        VideoDialog fragment = new VideoDialog();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        editTextLink = view.findViewById(R.id.edit_memo);

        builder.setView(view)
                .setTitle("Add video")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = editTextLink.getText().toString();
                        sendResult(Activity.RESULT_OK, s);
                    }
                });
        return builder.create();
    }

    private void sendResult(int result_code, String link) {
        Intent intent = new Intent();
        intent.putExtra(LINK, link);
        getTargetFragment().onActivityResult(getTargetRequestCode(), result_code, intent);
    }

}
