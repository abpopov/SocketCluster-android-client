package com.ermioni.scclient.services.websocket;

/**
 * Created by DM on 25.05.2016.
 */

/**
 * {"event":"#publish",
 * "cid":5,
 * "data":
 * {"channel":"spot_talk_2",
 * "data":
 * {"message":
 * {"message_text":"Вововоао",
 * "timestamp":1464166071.73809,
 * "author_user":
 * {"username":"ic",
 * "id":1,
 * "avatar_url":"http:\/\/192.168.1.9:2320\/files\/temporary\/4fad4d5ef023c4b46df0b7a45e2eaaae_thumb_180_180.jpg",
 * "name":"Анатолий"
 * },
 * "uuid":"49ED0916-0D1E-42CA-8DAE-D2BCC8D35E4A"
 * }
 * }
 * }
 * }
 */
public class Message
{
    public static class User
    {
        public Integer id;
        public String username;
        public String name;
        public String avatar_url;
    }

    public static class City
    {
        public Integer id;
        public String title;
        public Double latitude;
        public Double longitude;
    }

    public static class Spot extends City
    {
        public City city;
    }

    public String message_text;
    public double timestamp;
    public User author_user;
    public Spot author_spot;
    public String uuid;
}

