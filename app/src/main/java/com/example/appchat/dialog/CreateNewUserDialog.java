package com.example.appchat.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.appchat.R;
import com.example.appchat.model.Account;
import com.example.appchat.utils.AccountUtils;

public class CreateNewUserDialog extends Dialog {
   private EditText edtUsername,edtPassword;
   private ProgressDialog loadingBar;
   private Button btnCreate;
   public CreateNewUserDialog(@NonNull final Context context) {
      super(context);

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.dialog_add_new_user);

      edtUsername = findViewById(R.id.edtname);
      edtPassword = findViewById(R.id.edtpass);
      btnCreate = findViewById(R.id.btnCreateMember);


      btnCreate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            loadingBar = new ProgressDialog(getContext());
            loadingBar.setTitle("Creating new Account");
            loadingBar.setMessage("Please wait, while we creating new account for you...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            if (TextUtils.isEmpty(edtUsername.getText()) || TextUtils.isEmpty(edtPassword.getText())){
               Toast.makeText(context, "Please complete all information", Toast.LENGTH_SHORT).show();
               loadingBar.dismiss();
               return;
            }
            String Name = edtUsername.getText().toString();
            String Password = edtPassword.getText().toString();

            Account account = new Account();
            account.setUserName(Name);
            account.setPassword(Password);
            account.setAvatar("default");

            // Add Account vao local
            if (AccountUtils.getInstance().putAccount(account)){
               Toast.makeText(context, "Create Account Success", Toast.LENGTH_SHORT).show();
               edtUsername.setText("");
               edtPassword.setText("");

               loadingBar.dismiss();
            }else {
               Toast.makeText(context, "Account Exists", Toast.LENGTH_SHORT).show();
               loadingBar.dismiss();
            }
         }
      });
   }
}
