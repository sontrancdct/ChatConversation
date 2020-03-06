package com.example.appchat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.R;
import com.example.appchat.adapter.UserAdapter;
import com.example.appchat.appdata.AppChatData;
import com.example.appchat.dialog.CreateNewUserDialog;
import com.example.appchat.model.Account;
import com.example.appchat.utils.AccountUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
   private RecyclerView recyclerView;

   private UserAdapter userAdapter;
   private ArrayList<Account> accounts = new ArrayList<>();
   private Account account;
   private AppChatData appChatData;
   private ImageButton btnCreatenewAccount;
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
//      appChatData = AppChatUtil.loadAppChatData();
//      accounts = appChatData.listAccount;

      View view = inflater.inflate(R.layout.fragment_user, container, false);
      recyclerView = view.findViewById(R.id.recyclerview_users);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      btnCreatenewAccount = view.findViewById(R.id.add_Account);
      btnCreatenewAccount.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            CreateNewUserDialog createNewUserDialog = new CreateNewUserDialog(getActivity());
            createNewUserDialog.show();
         }
      });
      return view;
   }

   @Override
   public void onResume() {
      super.onResume();
      readUser();
   }

   private void readUser() {
      ArrayList<Account> arrayAccount = AccountUtils.getInstance().getAllAccount();
      userAdapter = new UserAdapter(getContext(), arrayAccount);
      recyclerView.setAdapter(userAdapter);
   }

   public void passData() {
   }

}
