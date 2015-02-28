package com.codepath.apps.codepathtwitterclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.codepathtwitterclient.R;
import com.codepath.apps.codepathtwitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.codepathtwitterclient.interfaces.EndlessScrollListener;
import com.codepath.apps.codepathtwitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment {
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private SwipeRefreshLayout srlTweets;
    private TweetTapListener listener;

    public interface TweetTapListener {
        void onTweetTapped(Long tweetUid);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TweetTapListener) {
            listener = (TweetTapListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement TweetsListFragment.TweetTapListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        ListView lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        // Setup pull to refresh
        srlTweets = (SwipeRefreshLayout) v.findViewById(R.id.srlTweets);
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
                populateTimelineOld(aTweets);
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet tweet = tweets.get(position);
                listener.onTweetTapped(tweet.getUid());
            }
        });

        return v;
    }

    protected abstract void populateTimeline();
    protected abstract void populateTimelineOld(TweetsArrayAdapter aTweets);

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void clear() {
        aTweets.clear();
    }

    public void addNewTweet(Tweet tweet) {
        if (tweet != null) {
            // Modify tweets invisibly
            tweets.clear();
            tweets.add(tweet);

            // Now show all changes
            populateTimelineOld(aTweets);
        } else {
            populateTimeline();
        }
    }

}
