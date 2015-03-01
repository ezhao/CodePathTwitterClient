package com.codepath.apps.codepathtwitterclient.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.TwitterApplication;
import com.codepath.apps.codepathtwitterclient.TwitterClient;
import com.codepath.apps.codepathtwitterclient.fragments.TweetsListFragment.TweetTapListener;
import com.codepath.apps.codepathtwitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.codepathtwitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity implements TweetTapListener {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TwitterClient client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }
        });

        String screen_name = getIntent().getStringExtra("screen_name");

        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screen_name);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserTimeline, userTimelineFragment);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
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
    public void onTweetTapped(Long tweetUid) {
        // TODO(emily) implement something when tweets are tapped!
    }
}
