package com.codepath.apps.codepathtwitterclient.activities;

import android.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.codepathtwitterclient.TwitterApplication;
import com.codepath.apps.codepathtwitterclient.TwitterClient;
import com.codepath.apps.codepathtwitterclient.fragments.ComposeDialogFragment;
import com.codepath.apps.codepathtwitterclient.fragments.ComposeDialogFragment.ComposeDialogFragmentListener;
import com.codepath.apps.codepathtwitterclient.helpers.Helper;
import com.codepath.apps.codepathtwitterclient.interfaces.EndlessScrollListener;
import com.codepath.apps.codepathtwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements ComposeDialogFragmentListener {

    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private SwipeRefreshLayout srlTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        client = TwitterApplication.getRestClient();
        populateTimeline();

        // Setup pull to refresh
        srlTweets = (SwipeRefreshLayout) findViewById(R.id.srlTweets);
        srlTweets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("EMILY", "Pull to refresh triggered, length of tweets: " + tweets.size());
                populateTimeline();
                srlTweets.setRefreshing(false);
            }
        });
        srlTweets.setColorSchemeResources(android.R.color.holo_blue_bright);

        // Setup infinite scroll
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                Log.d("EMILY", "Infinite scroll triggered, length of tweets: " + tweets.size());
                populateTimelineOld();
            }
        });
    }

    public void onComposeAction(MenuItem mi) {
        showComposeDialog();
    }

    private void showComposeDialog() {
        FragmentManager fm = getFragmentManager();
        ComposeDialogFragment frag = ComposeDialogFragment.newInstance();
        frag.show(fm, "EMILY");
    }

    private void populateTimeline() {
        if (!Helper.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
            return;
        }
        client.getTimelineHome(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.clear();
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("EMILY", responseString);
            }
        });
    }

    private void populateTimelineOld() {
        if (!Helper.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
            return;
        }
        if (tweets.isEmpty()) {
            populateTimeline();
            return;
        }
        Long max_id = tweets.get(tweets.size()-1).getUid() - 1;
        client.getTimelineMax(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("EMILY", responseString);
            }
        }, max_id);
        Log.d("EMILY", "max_id: " + max_id);
    }

    @Override
    public void onFinishComposeDialog(String tweetText) {
        if (!Helper.isNetworkAvailable(this)) {
            Toast.makeText(this, getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
            return;
        }
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweets.clear();
                tweets.add(Tweet.fromJSON(response));
                populateTimelineOld();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("EMILY", responseString);
            }
        }, tweetText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
