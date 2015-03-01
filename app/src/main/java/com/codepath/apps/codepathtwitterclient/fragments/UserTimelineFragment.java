package com.codepath.apps.codepathtwitterclient.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.TwitterApplication;
import com.codepath.apps.codepathtwitterclient.TwitterClient;
import com.codepath.apps.codepathtwitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.codepathtwitterclient.helpers.Helper;
import com.codepath.apps.codepathtwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private String screen_name;

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screen_name = getArguments().getString("screen_name");

        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    protected void populateTimeline() {
        if (!Helper.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
            clear();
            addAll(Tweet.fromDatabase());
            return;
        }
        client.getTimelineUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                clear();
                addAll(Tweet.fromJSONArray(response));
            }
        }, screen_name);
    }

    protected void populateTimelineOld(TweetsArrayAdapter aTweets) {
        if (!Helper.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
            return;
        }
        if (aTweets.isEmpty()) {
            populateTimeline();
            return;
        }
        Long max_id = aTweets.getItem(aTweets.getCount() - 1).getUid() - 1;
        client.getTimelineUserMax(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
            }
        }, screen_name, max_id);
        Log.d("EMILY", "max_id: " + max_id);
    }
}
