package com.codepath.apps.codepathtwitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name = "Users")
public class User extends Model {
    @Column(name = "name")
    private String name;
    @Column(name = "uid")
    private long uid;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public User(){
        super();
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            Long newUid = jsonObject.getLong("id");
            User existingUser = User.getUser(newUid);
            if (existingUser != null) {
                user = existingUser;
            }
            user.uid = newUid;
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUser(long uid) {
        List<User> users = new Select()
                .from(User.class)
                .where("uid = ?", uid)
                .execute();
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    // Clears all the Users
    public static void clearUsers() {
        new Delete().from(User.class).execute();
    }

}
