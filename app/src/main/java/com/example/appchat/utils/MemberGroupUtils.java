package com.example.appchat.utils;

import com.example.appchat.model.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MemberGroupUtils {

    private static String KEY_MyAccount;

    private static MemberGroupUtils memberGroupUtils;

    public static MemberGroupUtils getInstance(){
        if (memberGroupUtils == null){
            memberGroupUtils = new MemberGroupUtils();
        }
        return memberGroupUtils;
    }

    public boolean putAccount(String groupId,Account account){
        ArrayList<Account> arrayAccount = getAllAccount(groupId);

        for (Account account1 : arrayAccount){
            if (account.getUserName().equals(account1.getUserName())){
                return false;
            }
        }

        arrayAccount.add(account);

        saveData(groupId,arrayAccount);

        return true;
    }

    public Account getAccount(String groupId,String userName){
        ArrayList<Account> arrayAccount = getAllAccount(groupId);

        for (Account account1 : arrayAccount){
            if (userName.equals(account1.getUserName())){
                return account1;
            }
        }
        return null;
    }

    public ArrayList<Account> getAllAccount(String groupId){
        String json = SharePUtils.getInstance().get("Member"+ groupId);
        if (json == null){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json,new TypeToken<ArrayList<Account>>(){}.getType());
    }

    private void saveData(String groupId,ArrayList<Account> arrayAccount){
        String json = new Gson().toJson(arrayAccount);

        SharePUtils.getInstance().putData("Member"+ groupId,json);
    }
}
