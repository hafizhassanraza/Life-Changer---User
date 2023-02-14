package com.bitsnest.lifechanger;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Token = "Token";

    public Utils(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }
    public String getToken(){
        return sharedPreferences.getString(Token,"");
    }
    public void putToken(String token){
        sharedPreferences.edit().putString(Token,token).commit();
    }
    public void logout(){
        sharedPreferences.edit().putString(Token,"logout").commit();
    }
    public boolean isLoggedIn(){
        boolean LoggedIn=true;
        if(getToken().equals("logout")||getToken().isEmpty())LoggedIn=false;
        return LoggedIn;
    }




}
