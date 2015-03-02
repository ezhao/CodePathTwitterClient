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
import android.widget.Toast;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.TwitterApplication;
import com.codepath.apps.codepathtwitterclient.TwitterClient;
import com.codepath.apps.codepathtwitterclient.helpers.Helper;
import com.codepath.apps.codepathtwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeDialogFragment extends DialogFragment {
    public interface ComposeTweetListener {
        void onFinishCompose(long tweetUid);
    }

    private TwitterClient client;
    private View dialogView;
    private ComposeTweetListener listener;
    private EditText etComposeTweet;
    private TextView tvCharacterCounter;

    public ComposeDialogFragment() { }

    public static ComposeDialogFragment newInstance() {
        //Bundle args = new Bundle();
        //args.putString("title", title);
        //frag.setArguments(args);
        return new ComposeDialogFragment();
    }

    private void submitTweet() {
        if (etComposeTweet == null) {
            etComposeTweet = (EditText) dialogView.findViewById(R.id.etComposeTweet);
        }
        if (listener == null) {
            listener = (ComposeTweetListener) getActivity();
        }

        if (!Helper.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
        } else {
            client.postTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Tweet tweet = Tweet.fromJSON(response);
                    listener.onFinishCompose(tweet.getUid());
                }
            }, etComposeTweet.getText().toString());
            dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.dialog_compose, null);

        alertDialogBuilder
            .setView(dialogView)
            .setPositiveButton(getResources().getString(R.string.button_post), new DialogInterface.OnClickListener() {
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
