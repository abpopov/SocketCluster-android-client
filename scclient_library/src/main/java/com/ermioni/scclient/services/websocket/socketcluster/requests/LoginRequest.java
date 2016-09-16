package com.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 09.04.16.
 */
public class LoginRequest extends BaseRequest<LoginRequest.AuthData>
{

    public static class AuthData
    {
        public String login;
        public String pass;

        public AuthData(String login, String pass)
        {
            this.login = login;
            this.pass = pass;
        }
    }

    public LoginRequest(int cid, String user, String password)
    {
        super(cid);
        event = "login";
        data = new AuthData(user, password);
    }
}
