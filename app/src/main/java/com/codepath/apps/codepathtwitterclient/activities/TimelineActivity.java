package com.codepath.apps.codepathtwitterclient.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.fragments.ComposeDialogFragment;
import com.codepath.apps.codepathtwitterclient.fragments.ComposeDialogFragment.ComposeTweetListener;
import com.codepath.apps.codepathtwitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.codepathtwitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.codepathtwitterclient.fragments.TweetsListFragment;
import com.codepath.apps.codepathtwitterclient.fragments.TweetsListFragment.TweetTapListener;
import com.codepath.apps.codepathtwitterclient.helpers.SmartFragmentStatePagerAdapter;
import com.codepath.apps.codepathtwitterclient.models.Tweet;

public class TimelineActivity extends ActionBarActivity implements ComposeTweetListener, TweetTapListener {
    public final int REPLY_REQUEST_CODE = 132;
    ViewPager viewPager;
    TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tweetsPagerAdapter);
        tabStrip.setViewPager(viewPager);
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
    public void onFinishCompose(long tweetUid) {
        Tweet tweet = Tweet.getTweet(tweetUid);
        viewPager.setCurrentItem(0);
        TweetsListFragment fragmentTweetsList = (TweetsListFragment) tweetsPagerAdapter.getRegisteredFragment(0);
        if (fragmentTweetsList!= null) {
            fragmentTweetsList.addNewTweet(tweet);
        }
    }

    @Override
    public void onTweetTapped(long tweetUid) {
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

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    // Return order of fragments
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
