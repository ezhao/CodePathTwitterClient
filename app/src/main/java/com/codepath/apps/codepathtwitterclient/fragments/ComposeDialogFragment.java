package com.codepath.apps.codepathtwitterclient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.models.Tweet;

public class ComposeDialogFragment extends DialogFragment {
    public interface ComposeDialogFragmentListener {
        void onFinishComposeDialog(String tweetText);
    }

    View dialogView;
    ComposeDialogFragmentListener listener;
    EditText etComposeTweet;
    TextView tvCharacterCounter;

    public ComposeDialogFragment() { }

    public static ComposeDialogFragment newInstance() {
        ComposeDialogFragment frag = new ComposeDialogFragment();
        //Bundle args = new Bundle();
        //args.putString("title", title);
        //frag.setArguments(args);
        return frag;
    }

    private void submitTweet() {
        if (etComposeTweet == null) {
            etComposeTweet = (EditText) dialogView.findViewById(R.id.etComposeTweet);
        }
        if (listener == null) {
            listener = (ComposeDialogFragmentListener) getActivity();
        }
        listener.onFinishComposeDialog(etComposeTweet.getText().toString());
        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.dialog_compose, null);

        alertDialogBuilder
            .setView(dialogView)
            .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    submitTweet();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        return alertDialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog myDialog = (AlertDialog) getDialog();
        myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        // Inflate views if we haven't gotten them
        if (etComposeTweet == null) {
            etComposeTweet = (EditText) dialogView.findViewById(R.id.etComposeTweet);
        }
        if (tvCharacterCounter == null) {
            tvCharacterCounter = (TextView) dialogView.findViewById(R.id.tvCharacterCounter);
        }

        etComposeTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int charactersRemaining = Tweet.MAX_TWEET_LENGTH - etComposeTweet.getText().toString().length();
                tvCharacterCounter.setText(String.valueOf(charactersRemaining));

                // Make it red if we go over
                if (charactersRemaining < 0) {
                    tvCharacterCounter.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    tvCharacterCounter.setTextColor(Color.parseColor("#000000"));
                }

                // Enable the button if we have between 1 and 140 chars
                // Otherwise, disable it
                AlertDialog myDialog = (AlertDialog) getDialog();
                Boolean buttonState = charactersRemaining >= 0 && charactersRemaining < 140;
                myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(buttonState);

            }
        });
    }

}
