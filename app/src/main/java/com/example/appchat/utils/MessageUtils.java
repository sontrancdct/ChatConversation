package com.example.appchat.utils;

import android.util.Log;

import com.example.appchat.model.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MessageUtils {
    private static MessageUtils messageUtils;

    public static MessageUtils getInstance(){
        if (messageUtils == null){
            messageUtils = new MessageUtils();
        }
        return messageUtils;
    }

    public boolean putMessage(String groupId,Message message){
        ArrayList<Message> arrayMessage = getAllMessage(groupId);

        arrayMessage.add(message);

        saveData(groupId,arrayMessage);

        return true;
    }

    public ArrayList<Message> getAllMessage(String groupId){
        String json = SharePUtils.getInstance().get(groupId);
        Log.d("x1x11x1x",json + "");

        if (json == null){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json,new TypeToken<ArrayList<Message>>(){}.getType());
    }

    private void saveData(String groupId,ArrayList<Message> arrayMessage){
        String json = new Gson().toJson(arrayMessage);

        SharePUtils.getInstance().putData(groupId,json);
    }
}
