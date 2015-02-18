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

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    private void getTimeline(AsyncHttpResponseHandler handler, RequestParams params) {
        params.put("count", 25);
        String apiUrl = getApiUrl("/statuses/home_timeline.json");
        getClient().get(apiUrl, params, handler);
    }

    public void getTimelineHome(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        getTimeline(handler, params);
    }

    public void getTimelineMax(AsyncHttpResponseHandler handler, long max_id) {
        RequestParams params = new RequestParams();
        params.put("max_id", max_id);
        getTimeline(handler, params);
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