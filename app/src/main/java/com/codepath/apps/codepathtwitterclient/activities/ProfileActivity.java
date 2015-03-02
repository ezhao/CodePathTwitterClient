package com.codepath.apps.codepathtwitterclient.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.TwitterApplication;
import com.codepath.apps.codepathtwitterclient.TwitterClient;
import com.codepath.apps.codepathtwitterclient.fragments.ComposeDialogFragment.ComposeTweetListener;
import com.codepath.apps.codepathtwitterclient.fragments.TweetsListFragment.TweetTapListener;
import com.codepath.apps.codepathtwitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.codepathtwitterclient.helpers.Helper;
import com.codepath.apps.codepathtwitterclient.models.Tweet;
import com.codepath.apps.codepathtwitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends ActionBarActivity implements ComposeTweetListener, TweetTapListener {
    public final int REPLY_REQUEST_CODE = 12;
    private User user;
    UserTimelineFragment userTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TwitterClient client = TwitterApplication.getRestClient();

        long user_id = getIntent().getLongExtra("user_id", 0);
        Log.i("EMILY", "user_id: " + user_id);
        //user_id = 10;

        // Populate header
        if (user_id > 0) {
            user = User.getUser(user_id);
            if (user == null) {
                Log.e("EMILY", "Couldn't find user, that's weird.");
                if (!Helper.isNetworkAvailable(this)) {
                    Toast.makeText(this, getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
                } else {
                    client.getUserInfo(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            ArrayList<User> users = User.fromJSONArray(response);
                            if (users.size() > 0) {
                                user = users.get(0);
                                populateProfileHeader(user);
                            }
                        }
                    }, user_id);
                }
            } else {
                populateProfileHeader(user);
            }
        } else {
            if (!Helper.isNetworkAvailable(this)) {
                Toast.makeText(this, getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
            } else {
                client.getUserInfo(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        user = User.fromJSON(response);
                        populateProfileHeader(user);
                    }
                });
            }
        }

        // Populate timeline
        if (savedInstanceState == null) {
            userTimelineFragment = UserTimelineFragment.newInstance(user_id);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserTimeline, userTimelineFragment);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        getSupportActionBar().setTitle("@" + user.getScreenName());

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvFullName = (TextView) findViewById(R.id.tvFullName);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvCountTweets = (TextView) findViewById(R.id.tvCountTweets);
        TextView tvCountFollowing = (TextView) findViewById(R.id.tvCountFollowing);
        TextView tvCountFollowers = (TextView) findViewById(R.id.tvCountFollowers);

        tvFullName.setText(user.getName());
        tvUserName.setText(String.format("@%s", user.getScreenName()));
        tvTagline.setText(user.getTagline());
        tvCountTweets.setText(String.format(getResources().getString(R.string.count_tweets), user.getTweetCount()));
        tvCountFollowing.setText(String.format(getResources().getString(R.string.count_following), user.getFollowingCount()));
        tvCountFollowers.setText(String.format(getResources().getString(R.string.count_followers), user.getFollowerCount()));

        ivProfileImage.setImageResource(0);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTweetTapped(long tweetUid) {
        Intent i = new Intent(ProfileActivity.this, TweetActivity.class);
        i.putExtra("uid", tweetUid);
        startActivityForResult(i, REPLY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REPLY_REQUEST_CODE) {
            onFinishCompose(data.getLongExtra("tweetUid", 0));
        }
    }

    @Override
    public void onFinishCompose(long tweetUid) {
        if (userTimelineFragment != null) {
            Tweet tweet = Tweet.getTweet(tweetUid);
            if (user.getUid() == tweet.getUser().getUid()) {
                userTimelineFragment.addNewTweet(tweet);
            }
        }
    }
}
