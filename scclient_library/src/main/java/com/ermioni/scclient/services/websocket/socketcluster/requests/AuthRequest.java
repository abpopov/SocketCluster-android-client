package com.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by DM on 17.05.2016.
 */
public class AuthRequest extends BaseRequest<AuthRequest.Login>
{
    public static class Login
    {
        public String user_token;
        public String api_token;

        public Login(String user_token, String api_token)
        {
            this.user_token = user_token;
            this.api_token = api_token;
        }
    }

    public AuthRequest(int cid, String user_token, String api_token)
    {
        super(cid);
        event = "login";
        data = new Login(user_token, api_token);
    }
}
