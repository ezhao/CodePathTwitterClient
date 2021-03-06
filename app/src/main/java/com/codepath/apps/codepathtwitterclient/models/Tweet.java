package com.codepath.apps.codepathtwitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Tweets")
public class Tweet extends Model {
    public static final int MAX_TWEET_LENGTH = 140;

    @Column(name = "body")
    private String body;
    @Column(name = "uid")
    private long uid;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "user")
    private User user;
    @Column(name = "retweet_count")
    private int retweetCount;
    @Column(name = "favorite_count")
    private int favoriteCount;
    @Column(name = "entities_url")
    private String entitiesUrl;

    public Tweet(){
        super();
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public String getEntitiesUrl() {
        return entitiesUrl;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            long newUid = jsonObject.getLong("id");
            Tweet existingTweet = Tweet.getTweet(newUid);
            if (existingTweet != null) {
                tweet = existingTweet;
            }
            tweet.uid = newUid;
            tweet.body = jsonObject.getString("text");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");

            JSONObject entities = jsonObject.getJSONObject("entities");
            if (entities.has("media") && entities.getJSONArray("media").length() > 0) {
                tweet.entitiesUrl = entities.getJSONArray("media").getJSONObject(0).getString("media_url");
            }

            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJSON;
            try {
                tweetJSON = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJSON);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public static Tweet getTweet(long uid) {
        List<Tweet> tweets = new Select()
                .from(Tweet.class)
                .where("uid = ?", uid)
                .execute();
        if (tweets != null && !tweets.isEmpty()) {
            return tweets.get(0);
        }
        return null;
    }

    public static ArrayList<Tweet> fromDatabase() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        List<Tweet> cursorTweets = new Select()
                .from(Tweet.class)
                .execute();
        tweets.addAll(cursorTweets);
        return tweets;
    }

    // Clears all the Tweets
    public static void clearTweets() {
        new Delete().from(Tweet.class).execute();
    }

}
