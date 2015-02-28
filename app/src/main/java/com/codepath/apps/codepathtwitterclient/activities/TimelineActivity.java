package com.codepath.apps.codepathtwitterclient.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.fragments.ComposeDialogFragment;
import com.codepath.apps.codepathtwitterclient.fragments.TweetsListFragment;
import com.codepath.apps.codepathtwitterclient.fragments.TweetsListFragment.TweetTapListener;
import com.codepath.apps.codepathtwitterclient.models.Tweet;

public class TimelineActivity extends ActionBarActivity implements ComposeDialogFragment.ComposeTweetListener, TweetTapListener {
    public final int REPLY_REQUEST_CODE = 132;
    private TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        if (savedInstanceState == null) {
            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_tweets_list);
        }
    }

    public void onComposeAction(MenuItem mi) {
        showComposeDialog();
    }

    private void showComposeDialog() {
        FragmentManager fm = getFragmentManager();
        ComposeDialogFragment frag = ComposeDialogFragment.newInstance();
        frag.show(fm, "compose");
    }

    @Override
    public void onFinishCompose(Long tweetUid) {
        Tweet tweet = Tweet.getTweet(tweetUid);
        fragmentTweetsList.addNewTweet(tweet);
    }

    @Override
    public void onTweetTapped(Long tweetUid) {
        Intent i = new Intent(TimelineActivity.this, TweetActivity.class);
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
