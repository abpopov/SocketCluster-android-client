package com.ermioni.scclient.services.websocket;

/**
 * Created by DM on 26.05.2016.
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;

/**
 * {"event":"#publish","data":{"channel":"spot_talk_20","data":
 * {"notification":
 * {"code":1001,"user":
 * {"id":35,"username":"ic","major_type":0,"following_status":false,"is_ready_to_help":false,"avatar_url":"https://www-test.ermioni.me/files/temporary/38246396d187902fb99334ffc6da45bb_thumb_180_180.jpg","name":"qwerty"}
 * }}},"cid":4}
 */
public class UserNotification
{
    public static class User
    {
        public int id;
        public String name;
        public String username;
        public String avatar_url;
        public int major_type;
        public boolean following_status;
        public boolean is_ready_to_help;
    }

    public int code;
    public int userId;
    public User userData;

    public static UserNotification fromJsonObject(JsonObject obj) throws JSONException
    {
        if (obj == null || !obj.has("code") || !obj.has("user")) return null;
        UserNotification result = new UserNotification();
        result.code = obj.get("code").getAsInt();

        switch (result.code)
        {
            case 1001:
                result.userData = (new Gson()).fromJson(obj.getAsJsonObject("user"), UserNotification.User.class);
                break;
            case 1002:
                result.userId = obj.get("user").getAsInt();
                break;
        }
        return result;
    }
}

