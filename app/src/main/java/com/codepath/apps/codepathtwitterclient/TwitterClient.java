package com.codepath.apps.codepathtwitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "jPZY5x84ogkwDRJmyaOXWBlmQ";
	public static final String REST_CONSUMER_SECRET = "btgX2mMAEvqtTBP9X0lWM51Fn7DhnyGN8R3nID1GL64P3V0zOY";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";

    private enum TimelineType {
        HOME, MENTIONS, USER
    }

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getTweet(AsyncHttpResponseHandler handler, Long uid) {
        String apiUrl = getApiUrl("statuses/show.json");
        RequestParams params = new RequestParams();
        params.put("id", uid);
        params.put("include_entities", true);
        getClient().get(apiUrl, params, handler);
    }

    private void getTimeline(AsyncHttpResponseHandler handler, RequestParams params, TimelineType type) {
        String apiUrl = getApiUrl("/statuses/mentions_timeline.json");
        if (type == TimelineType.HOME) {
            apiUrl = getApiUrl("/statuses/home_timeline.json");
        } else if (type == TimelineType.USER) {
            apiUrl = getApiUrl("/statuses/user_timeline.json");
        }
        params.put("count", 25);
        getClient().get(apiUrl, params, handler);
    }

    public void getTimelineHome(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        getTimeline(handler, params, TimelineType.HOME);
    }

    public void getTimelineHomeMax(AsyncHttpResponseHandler handler, long max_id) {
        RequestParams params = new RequestParams();
        params.put("max_id", max_id);
        getTimeline(handler, params, TimelineType.HOME);
    }

    public void getTimelineMentions(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        getTimeline(handler, params, TimelineType.MENTIONS);
    }

    public void getTimelineMentionsMax(AsyncHttpResponseHandler handler, Long max_id) {
        RequestParams params = new RequestParams();
        params.put("max_id", max_id);
        getTimeline(handler, params, TimelineType.MENTIONS);
    }

    public void getTimelineUser(AsyncHttpResponseHandler handler, String screen_name) {
        RequestParams params = new RequestParams();
        params.put("screen_name", screen_name);
        getTimeline(handler, params, TimelineType.USER);
    }

    public void getTimelineUserMax(AsyncHttpResponseHandler handler, String screen_name, Long max_id) {
        RequestParams params = new RequestParams();
        params.put("screen_name", screen_name);
        params.put("max_id", max_id);
        getTimeline(handler, params, TimelineType.USER);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }

    public void postTweet(AsyncHttpResponseHandler handler, String status) {
        String apiUrl = getApiUrl("/statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);
        getClient().post(apiUrl, params, handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}