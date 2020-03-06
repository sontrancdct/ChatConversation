package com.example.appchat.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.Constants;
import com.example.appchat.R;
import com.example.appchat.adapter.GroupsChatAdapter;
import com.example.appchat.adapter.UserAdapter;
import com.example.appchat.dialog.AddNewUserDiglog;
import com.example.appchat.model.Account;
import com.example.appchat.model.Group;
import com.example.appchat.model.Message;
import com.example.appchat.utils.AccountUtils;
import com.example.appchat.utils.MemberGroupUtils;
import com.example.appchat.utils.MessageUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ChatsGrActivity extends AppCompatActivity {

   private static final int REQUEST_PERMISSION = 113;
   private static final int REQUEST_CODE_PICK_PHOTO = 123;

   private Toolbar toolBar;
   private EditText edtMessage;
   private RecyclerView recyclerMessage;
   private GroupsChatAdapter groupsChatAdapter;
   private ArrayList<Message> messages = new ArrayList<>();
   private Group group;
   private AlertDialog alertDialog;
   private Spinner spinner;

   ValueEventListener listener;
   ArrayAdapter adapter;

   ArrayList<String> accounts = new ArrayList<>();

   private RecyclerView recyclerView;
   private UserAdapter userAdapter;



   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_chats_gr);

      //appChatData = AppChatUtil.loadAppChatData();

      init();
      setUpToolBar();
      getIntentData();
      updateToolbar();
      createRecycler();
      updateMessage();

      spinner = (Spinner) findViewById(R.id.spinner);
      adapter = new ArrayAdapter<>(ChatsGrActivity.this,R.layout.support_simple_spinner_dropdown_item,accounts);
      spinner.setAdapter(adapter);
      Retrivedata();
   }
   public void Retrivedata(){
      accounts.clear();
      for (Account account : MemberGroupUtils.getInstance().getAllAccount(group.getId())){
         accounts.add(account.getUserName());
      }
      adapter.notifyDataSetChanged();
   }

   private void updateMessage() {
      messages.clear();
      messages.addAll(MessageUtils.getInstance().getAllMessage(group.getId()));
      groupsChatAdapter.notifyDataSetChanged();
      if (messages.size() > 0) {
         recyclerMessage.smoothScrollToPosition(messages.size() - 1);
      }
   }

   private void createRecycler() {
      recyclerMessage.setHasFixedSize(true);
      recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
      groupsChatAdapter = new GroupsChatAdapter(messages, group);
      recyclerMessage.setAdapter(groupsChatAdapter);
   }

   private void updateToolbar() {
      getSupportActionBar().setTitle(group.getName());
   }

   private void getIntentData() {
      group = (Group) getIntent().getSerializableExtra("Conversation");
   }

   private void setUpToolBar() {
      setSupportActionBar(toolBar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
   }

   private void init() {
      toolBar = findViewById(R.id.toolBar);
      edtMessage      = findViewById(R.id.edtMessage);
      recyclerMessage = findViewById(R.id.recyclerMessage);
   }
   public void onClickSentMessage(View view) {
      String textMessage = edtMessage.getText().toString();

      if (TextUtils.isEmpty(textMessage)){
         return;
      }
      long time = System.currentTimeMillis();

      Message message = new Message();
      message.setDate(time);
      message.setMessage(textMessage);
      message.setUserName(AccountUtils.getInstance().getAccount(spinner.getSelectedItem().toString()).getUserName());
      message.setType(Constants.TYPE_MESSAGE);
      message.setAvatar(AccountUtils.getInstance().getAccount(spinner.getSelectedItem().toString()).getAvatar());

      MessageUtils.getInstance().putMessage(group.getId(),message);

      updateMessage();

      edtMessage.setText("");
   }

   public void onClickAdd(MenuItem item) {
      AddNewUserDiglog addNewUserDiglog = new AddNewUserDiglog(ChatsGrActivity.this,group.getId());
      addNewUserDiglog.show();
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if (item.getItemId() == android.R.id.home){
         finish();
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_add,menu);
      return super.onCreateOptionsMenu(menu);
   }

   public void onClickOpenLibrary(View view) {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
         pickPicture();
         return;
      }
      if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
         pickPicture();
         return;
      }
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
   }

   private void pickPicture() {
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("image/*");
      startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (requestCode == REQUEST_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
         pickPicture();
      }
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == REQUEST_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
         FirebaseStorage.getInstance().getReference()
            .child(System.currentTimeMillis() + ".png")
            .putFile(data.getData())
            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                  String pathPicture = task.getResult().getDownloadUrl().toString();

                  long time = System.currentTimeMillis();

                  Message message = new Message();
                  message.setDate(time);
                  message.setPath(pathPicture);
                  message.setUserName(AccountUtils.getInstance().getAccount(spinner.getSelectedItem().toString()).getUserName());
                  message.setType(Constants.TYPE_PICTURE);
                  message.setAvatar(AccountUtils.getInstance().getAccount(spinner.getSelectedItem().toString()).getAvatar());

                  MessageUtils.getInstance().putMessage(group.getId(),message);

                  updateMessage();
               }
            })
            .addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
               }
            });
      }
   }
}
