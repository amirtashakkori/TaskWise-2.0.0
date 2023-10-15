package com.example.taskmanager.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoContainer {
    SharedPreferences userInfoSp;

    public UserInfoContainer(Context context) {
        userInfoSp = context.getSharedPreferences("userInfo" , Context.MODE_PRIVATE);
    }

    public void saveInfo(String name , String family , String expertise){
        SharedPreferences.Editor editor = userInfoSp.edit();
        editor.putString("name" , name);
        editor.putString("family" , family);
        editor.putString("expertise" , expertise);
        editor.apply();
    }

    public String getName(){
        return userInfoSp.getString("name" , "");
    }

    public String getFamily(){
        return userInfoSp.getString("family" , "");
    }

    public String getExpertise(){
        return userInfoSp.getString("expertise" , "");
    }

}
