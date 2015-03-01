# Week 3 Assignment - Twitter Client

Preview of application

![Animation of app](https://www.dropbox.com/s/advdpqgc13gbtud/CodePathTwitterClient.gif?dl=1)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Stories

- [x] User can sign in to Twitter using OAuth login
- [x] User can view the tweets from their home timeline
	- [x] User should be displayed the username, name, and body for each tweet
	- [x] User should be displayed the relative timestamp for each tweet "8m", "7h"
	- [x] User can view more tweets as they scroll with infinite pagination
	- [x] Optional: Links in tweets are clickable and will launch the web browser (see autolink)
- [x] User can compose a new tweet
	- [x] User can click a “Compose” icon in the Action Bar on the top right
	- [x] User can then enter a new tweet and post this to twitter
	- [x] User is taken back to home timeline with new tweet visible in timeline
	- [x] Optional: User can see a counter with total number of characters left for tweet
	- [x] Bonus: Compose activity is replaced with a modal overlay
- [x] Advanced: User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
- [x] Advanced: User can open the twitter app offline and see last loaded tweets
	- [x] Tweets are persisted into sqlite and can be displayed from the local DB
- [x] Advanced: User can tap a tweet to display a "detailed" view of that tweet
- [x] Advanced: User can select "reply" from detail view to respond to a tweet
- [x] Advanced: Improve the user interface and theme the app to feel "twitter branded" (though Compose Tweet could use some work)
- [x] Bonus: User can see embedded image media within the tweet detail view
- [x] User can switch between Timeline and Mention views using tabs.
- [x] User can navigate to view their own profile and see picture, tagline, # of followers, # of following, and tweets on their profile.
- [ ] User can click on the profile image in any tweet to see another user's profile and see picture, tagline, # of followers, # of following, and tweets on their profile.
	- [ ] Optional: User can view following / followers list through the profile
- [ ] User can infinitely paginate any of these timelines (home, mentions, user) by scrolling to the bottom
- [ ] Advanced: Robust error handling, check if internet is available, handle error cases, network failures
- [ ] Advanced: When a network request is sent, user sees an indeterminate progress indicator
- [ ] Advanced: User can "reply" to any tweet on their home timeline
	- [ ] The user that wrote the original tweet is automatically "@" replied in compose
- [ ] Advanced: User can click on a tweet to be taken to a "detail view" of that tweet
	- [ ] Advanced: User can take favorite (and unfavorite) or reweet actions on a tweet
- [ ] Advanced: Improve the user interface and theme the app to feel twitter branded
- [ ] Advanced: User can search for tweets matching a particular query and see results
- [ ] Bonus: User can view their direct messages (or send new ones)


Time spent part 1: 15 hours
Time spent part 2: 5 hours

[CodePath Week 3 Assignment](http://courses.codepath.com/courses/intro_to_android/week/3#!assignment)
[CodePath Week 4 Assignment](http://courses.codepath.com/courses/intro_to_android/week/4#!assignment)

Library credits: [CodePath RestClientTemplate](https://github.com/codepath/android-rest-client-template), [Picasso](http://square.github.io/picasso/), [android-async-http](http://loopj.com/android-async-http/), [AndroidStaggeredGrid](https://github.com/f-barth/AndroidStaggeredGrid), [ActiveAndroid](https://github.com/pardom/ActiveAndroid), more linked to from RestClientTemplate, [PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip).

Icon credits: [iconmonstr](http://iconmonstr.com/)
