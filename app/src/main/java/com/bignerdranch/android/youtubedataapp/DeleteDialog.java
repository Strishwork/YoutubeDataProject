package com.bignerdranch.android.youtubedataapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class DeleteDialog extends DialogFragment {

    public static final String DELETE = "delete";
    public static final String ARGS_ID = "id";
    public static final String ARGS_POS = "pos";

    public static DeleteDialog newInstance(long id, int pos) {
        DeleteDialog fragment = new DeleteDialog();
        Bundle args = new Bundle();
        args.putLong(ARGS_ID, id);
        args.putInt(ARGS_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setMessage("Do you want to delete this video from your list?")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, false);
                    }
                })
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK, true);
                    }
                });
        return builder.create();
    }

    private void sendResult(int result_code, boolean delete) {
        Intent intent = new Intent();
        long id = getArguments().getLong(ARGS_ID);
        int pos = getArguments().getInt(ARGS_POS);
        intent.putExtra(ARGS_ID, id);
        intent.putExtra(DELETE, delete);
        getTargetFragment().onActivityResult(getTargetRequestCode(), result_code, intent);
    }
}
