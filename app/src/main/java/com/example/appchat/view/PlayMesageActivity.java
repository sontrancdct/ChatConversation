package com.example.appchat.view;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.R;
import com.example.appchat.adapter.GroupsChatAdapter;
import com.example.appchat.model.Account;
import com.example.appchat.model.Group;
import com.example.appchat.model.Message;
import com.example.appchat.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Random;

public class PlayMesageActivity extends AppCompatActivity {

   private Toolbar toolBar;
   private Group group;
   private Account myAccount;
   private GroupsChatAdapter groupsChatAdapter;
   private ArrayList<Message> messages = new ArrayList<>();


   private RecyclerView recyclerView;

   int count = 0;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_play_mesage);

      toolBar = findViewById(R.id.toolBar);
      setSupportActionBar(toolBar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
      getIntentData();
      getSupportActionBar().setTitle(group.getName());

      createRecycler();
      getMessage();

   }

   private void getIntentData() {
      group = (Group) getIntent().getSerializableExtra("Conversation");
   }
   @Override
   public boolean onSupportNavigateUp() {
      onBackPressed();
      return true;
   }

   long delay;
   private void getMessage() {
      final Handler handler = new Handler();
      final int MIN = 500;
      final int MAX = 1000;
      final Random r = new Random();
      final int value = r.nextInt((MAX - MIN) + 1) + MIN;
      messages.clear();

      final ArrayList<Message> stringArrayList = new ArrayList<>();

      stringArrayList.addAll(MessageUtils.getInstance().getAllMessage(group.getId()));
      Runnable runnable = new Runnable() {
         @Override
         public void run() {
            messages.add(stringArrayList.get(count));
            groupsChatAdapter.notifyItemInserted(messages.size() - 1);
            if (messages.size() > 0) {
               recyclerView.smoothScrollToPosition(messages.size() - 1);
            }
            count++;
            handler.postDelayed(this, delay+= value);
         }
      };
      handler.post(runnable);
   }
   private void createRecycler() {
      recyclerView = findViewById(R.id.recyclerPlayMessage);
      recyclerView.setHasFixedSize(true);
      LinearLayoutManager ll = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
      ll.setStackFromEnd(true);
      recyclerView.setLayoutManager(ll);
      groupsChatAdapter = new GroupsChatAdapter(messages, group);
      recyclerView.setAdapter(groupsChatAdapter);
   }

}
