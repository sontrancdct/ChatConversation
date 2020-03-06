package com.example.appchat.utils;

import com.example.appchat.model.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AccountUtils {

    private static final String KEY = "Account";
    private static final String KEY_MyAccount = "MyAccount";

    private static AccountUtils accountUtils;

    public static AccountUtils getInstance(){
        if (accountUtils == null){
            accountUtils = new AccountUtils();
        }
        return accountUtils;
    }
    public boolean putAccount(Account account){
        ArrayList<Account> arrayAccount = getAllAccount();

        for (Account account1 : arrayAccount){
            if (account.getUserName().equals(account1.getUserName())){
                return false;
            }
        }
        arrayAccount.add(account);

        saveData(arrayAccount);

        return true;
    }
    public ArrayList<Account> getAllAccount(){
        String json = SharePUtils.getInstance().get(KEY);
        if (json == null){
            return new ArrayList<>();
        }
        return new Gson().fromJson(json,new TypeToken<ArrayList<Account>>(){}.getType());
    }
    public Account getAccount(String userName){
        ArrayList<Account> arrayAccount = getAllAccount();

        for (Account account1 : arrayAccount){
            if (userName.equals(account1.getUserName())){
                return account1;
            }
        }

        return null;
    }
    public Account getMyAccount(){
        String json = SharePUtils.getInstance().get(KEY_MyAccount);

        return new Gson().fromJson(json,Account.class);
    }

    public void putMyAccount(Account account){
        SharePUtils.getInstance().putData(KEY_MyAccount,new Gson().toJson(account));
    }

    public boolean editAccount(Account account){
        ArrayList<Account> arrayAccount = getAllAccount();

        for (int i=0; i < arrayAccount.size(); i++){
            if (account.getUserName().equals(arrayAccount.get(i).getUserName())){

                arrayAccount.get(i).setAvatar(account.getAvatar());

                saveData(arrayAccount);

                return true;
            }
        }
        return false;
    }
    private void saveData(ArrayList<Account> arrayAccount){
        String json = new Gson().toJson(arrayAccount);
        SharePUtils.getInstance().putData(KEY,json);
    }
}
