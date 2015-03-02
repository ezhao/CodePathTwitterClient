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
    private long user_id;

    public static UserTimelineFragment newInstance(long user_id) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("user_id", user_id);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_id = getArguments().getLong("user_id");

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
        }, user_id);
    }

    protected void populateTimelineOld(TweetsArrayAdapter aTweets) {
        Log.i("EMILY", "populateTimelineOld UserTimelineFragment: " + user_id);
        if (!Helper.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
            return;
        }
        if (aTweets.isEmpty()) {
            populateTimeline();
            return;
        }
        long max_id = aTweets.getItem(aTweets.getCount() - 1).getUid() - 1;
        client.getTimelineUserMax(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
            }
        }, user_id, max_id);
        Log.d("EMILY", "max_id: " + max_id);
    }
}
