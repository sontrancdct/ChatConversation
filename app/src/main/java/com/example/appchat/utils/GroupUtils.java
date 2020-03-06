package com.example.appchat.utils;

import com.example.appchat.model.Group;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class GroupUtils {
    private static final String KEY = "Group";

    private static GroupUtils groupUtils;

    public static GroupUtils getInstance(){
        if (groupUtils == null){
            groupUtils = new GroupUtils();
        }
        return groupUtils;
    }

    public boolean putGroup(Group group){
        ArrayList<Group> arrayGroup = getAllGroup();

        for (Group group1 : arrayGroup){
            if (group.getId().equals(group1.getId())){
                return false;
            }
        }

        arrayGroup.add(group);

        saveData(arrayGroup);

        return true;
    }

    public Group getGroup(String id){
        ArrayList<Group> arrayGroup = getAllGroup();

        for (Group group : arrayGroup){
            if (id.equals(group.getId())){
                return group;
            }
        }

        return null;
    }

    public boolean deleteGroup(String id){
        ArrayList<Group> arrayGroup = getAllGroup();

        for (int i=0; i < arrayGroup.size(); i++){
            if (id.equals(arrayGroup.get(i).getId())){
                arrayGroup.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Group> getAllGroup(){
        String json = SharePUtils.getInstance().get(KEY);
        if (json == null){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json,new TypeToken<ArrayList<Group>>(){}.getType());
    }

    private void saveData(ArrayList<Group> arrayGroup){
        String json = new Gson().toJson(arrayGroup);

        SharePUtils.getInstance().putData(KEY,json);
    }
}
