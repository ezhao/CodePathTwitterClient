package com.codepath.apps.codepathtwitterclient.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.helpers.Helper;
import com.codepath.apps.codepathtwitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

public class TweetActivity extends ActionBarActivity {
    Long uid;
    ImageView ivProfileImage;
    TextView tvFullName;
    TextView tvUserName;
    TextView tvBody;
    TextView tvCreatedTime;
    ImageView ivTweetMedia;
    TextView tvRetweetFavorites;
    EditText etReply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        uid = getIntent().getLongExtra("uid", 0);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvCreatedTime = (TextView) findViewById(R.id.tvCreatedTime);
        ivTweetMedia = (ImageView) findViewById(R.id.ivTweetMedia);
        tvRetweetFavorites = (TextView) findViewById(R.id.tvRetweetFavorites);
        etReply = (EditText) findViewById(R.id.etReply);

        Tweet myTweet = Tweet.getTweet(uid);
        if (myTweet != null) {
            populateTweet(myTweet);
        } else {
            Toast.makeText(this, getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void populateTweet(Tweet tweet) {
        ivProfileImage.setImageResource(0);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        tvFullName.setText(tweet.getUser().getName());
        tvUserName.setText(String.format("@%s", tweet.getUser().getScreenName()));
        tvBody.setText(tweet.getBody());
        tvCreatedTime.setText(Helper.getRelativeTimeAgo(tweet.getCreatedAt()));
        if (tweet.getEntitiesUrl() != null) {
            Picasso.with(this).load(tweet.getEntitiesUrl()).into(ivTweetMedia);
        } else {
            ivTweetMedia.setVisibility(View.GONE);
        }
        tvRetweetFavorites.setText(Html.fromHtml(String.format(getResources().getString(R.string.retweet_favorites),
                tweet.getRetweetCount(), tweet.getFavoriteCount())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
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
}
