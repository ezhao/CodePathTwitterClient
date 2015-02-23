package com.codepath.apps.codepathtwitterclient.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.TwitterApplication;
import com.codepath.apps.codepathtwitterclient.TwitterClient;
import com.codepath.apps.codepathtwitterclient.helpers.Helper;
import com.codepath.apps.codepathtwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class TweetActivity extends ActionBarActivity {
    Long uid;
    ImageView ivProfileImage;
    TextView tvFullName;
    TextView tvUserName;
    TextView tvBody;
    TextView tvCreatedTime;
    ImageView ivTweetMedia;
    TextView tvRetweetFavorites;
    TextView tvCharacterCounter;
    Button btPost;
    EditText etReply;
    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        uid = getIntent().getLongExtra("uid", 0);

        client = TwitterApplication.getRestClient();

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvCreatedTime = (TextView) findViewById(R.id.tvCreatedTime);
        ivTweetMedia = (ImageView) findViewById(R.id.ivTweetMedia);
        tvRetweetFavorites = (TextView) findViewById(R.id.tvRetweetFavorites);
        tvCharacterCounter = (TextView) findViewById(R.id.tvCharacterCounter);
        btPost = (Button) findViewById(R.id.btPost);
        etReply = (EditText) findViewById(R.id.etReply);

        Tweet myTweet = Tweet.getTweet(uid);
        if (myTweet != null) {
            populateTweet(myTweet);
        } else {
            getTweet();
            Toast.makeText(this, getResources().getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
        }

        etReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int charactersRemaining = Tweet.MAX_TWEET_LENGTH - etReply.getText().toString().length();
                tvCharacterCounter.setText(String.valueOf(charactersRemaining));

                // Make it red if we go over
                if (charactersRemaining < 0) {
                    tvCharacterCounter.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    tvCharacterCounter.setTextColor(Color.parseColor("#000000"));
                }

                // Enable the button if we have between 1 and 140 chars
                // Otherwise, disable it
                Boolean buttonState = charactersRemaining >= 0 && charactersRemaining < 140;
                btPost.setEnabled(buttonState);
            }
        });

        btPost.setEnabled(false);
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Helper.isNetworkAvailable(TweetActivity.this)) {
                    Toast.makeText(TweetActivity.this, getResources().getString(R.string.network_issues), Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent data = new Intent();
                data.putExtra("body", etReply.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void getTweet() {
        Log.i("EMILY", "getTweet");
        client.getTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                populateTweet(Tweet.fromJSON(response));
            }
        }, uid);
    }

    private void populateTweet(Tweet tweet) {
        ivProfileImage.setImageResource(0);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        tvFullName.setText(tweet.getUser().getName());
        tvUserName.setText(String.format("@%s", tweet.getUser().getScreenName()));
        tvBody.setText(tweet.getBody());
        tvCreatedTime.setText(Helper.getRelativeTimeAgo(tweet.getCreatedAt()));
        ivTweetMedia.setImageResource(android.R.color.transparent);
        if (tweet.getEntitiesUrl() != null) {
            Picasso.with(this).load(tweet.getEntitiesUrl()).into(ivTweetMedia);
        } else {
            ivTweetMedia.setVisibility(View.INVISIBLE);
        }
        tvRetweetFavorites.setText(Html.fromHtml(
                String.format(getResources().getString(R.string.retweet_favorites),
                tweet.getRetweetCount(),
                tweet.getFavoriteCount())));
        etReply.append(String.format("@%s ", tweet.getUser().getScreenName()));
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
