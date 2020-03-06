package com.example.appchat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.R;
import com.example.appchat.adapter.AddMemberAdapter;
import com.example.appchat.model.Account;
import com.example.appchat.model.Group;
import com.example.appchat.utils.AccountUtils;

import java.util.ArrayList;

public class AddNewUserDiglog extends Dialog {

   private EditText editText;
   private RecyclerView recyclerViewListUser;
   private ArrayList<Account> accounts = new ArrayList<>();
   private AddMemberAdapter addMemberAdapter;
   private Group group;
   Dialog alertDialog;
   private String groupId;

   public AddNewUserDiglog(@NonNull Context context,String groupId) {
      super(context);
      this.groupId = groupId;

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.dialog_add_friend);

      editText = findViewById(R.id.edtUsername);
      accounts.addAll(AccountUtils.getInstance().getAllAccount());

      recyclerViewListUser = findViewById(R.id.recyclerview_listusers);
      recyclerViewListUser.setHasFixedSize(true);
      recyclerViewListUser.setLayoutManager(new LinearLayoutManager(getContext()));
      addMemberAdapter = new AddMemberAdapter(getContext(), accounts);
      recyclerViewListUser.setAdapter(addMemberAdapter);

      editText.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            accounts.clear();
            if (s.length() > 0){
               for (Account account : AccountUtils.getInstance().getAllAccount()){
                  if(account.getUserName().toLowerCase().contains(s.toString().toLowerCase())){
                     accounts.add(account);
                  }
               }
            }else {
               accounts.addAll(AccountUtils.getInstance().getAllAccount());
            }
            addMemberAdapter.notifyDataSetChanged();
         }
         @Override
         public void afterTextChanged(Editable s) {
         }
      });

      ImageButton imageButton = findViewById(R.id.CreateNewAccount);
      imageButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            CreateNewUserDialog createNewUserDialog = new CreateNewUserDialog(getContext());
            createNewUserDialog.show();
         }
      });


      /// Add Account vao group
     // MemberGroupUtils.getInstance().putAccount(groupId,Account);
   }

}
