package com.example.appchat.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.R;
import com.example.appchat.adapter.GroupsListAdapter;
import com.example.appchat.appdata.AppChatData;
import com.example.appchat.model.Group;
import com.example.appchat.utils.AccountUtils;
import com.example.appchat.utils.GroupUtils;
import com.example.appchat.utils.MemberGroupUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {
   private RecyclerView recyclerRoom;
   private GroupsListAdapter groupsListAdapter;

   private AlertDialog alertDialog;
   private ImageButton add_Conversation ;

   private AppChatData appChatData;

   private ArrayList<Group> groups = new ArrayList<>();


//   public ConversationFragment() {
//      appChatData = AppChatUtil.loadAppChatData();
////      appChatData.listAccount;
////      appChatData.listGroup;
//   }
//
//   public void addNewGroup() {
//      Group group = new Group();
//      appChatData.listGroup.add(group);
//      AppChatUtil.saveAppChatData(appChatData);
//   }


   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_conversation, container, false);

      recyclerRoom = view.findViewById(R.id.recyclerConversation);
      setRecyclerConversation();

      add_Conversation = view.findViewById(R.id.add_Conversation);
      add_Conversation.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            createNewGroups();
         }
      });

      return view;
   }

   private void createNewGroups() {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      View container = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_groups,null);
      builder.setView(container);
      alertDialog = builder.create();

      final EditText edtRoomName = container.findViewById(R.id.edtRoomName);
      Button btnCreate = container.findViewById(R.id.btnCreate);

      btnCreate.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v) {
            if (TextUtils.isEmpty(edtRoomName.getText())){
               Toast.makeText(getActivity(), "Please enter name groups...", Toast.LENGTH_SHORT).show();
               return;
            }
            createGroups(edtRoomName.getText().toString());
         }
      });
      alertDialog.show();
   }
   private void createGroups(String groupsName) {
      String id = System.currentTimeMillis() + "";
      final Group group = new Group();
      group.setId(id);
      group.setName(groupsName);
      group.setAdmin(AccountUtils.getInstance().getMyAccount().getUserName());

      if (GroupUtils.getInstance().putGroup(group)){
         alertDialog.cancel();
         Toast.makeText(getActivity(), "Create Group Success", Toast.LENGTH_SHORT).show();
         MemberGroupUtils.getInstance().putAccount(id,AccountUtils.getInstance().getMyAccount());
         updateGroup();
      }
   }
   public void passData() {

   }

   private void setRecyclerConversation() {
      recyclerRoom.setHasFixedSize(true);
      recyclerRoom.setLayoutManager(new LinearLayoutManager(getActivity()));
      groupsListAdapter = new GroupsListAdapter(groups);
      recyclerRoom.setAdapter(groupsListAdapter);
   }

   @Override
   public void onResume() {
      super.onResume();
      updateGroup();
   }

   private void updateGroup(){
      groups.clear();
      groups.addAll(GroupUtils.getInstance().getAllGroup());
      groupsListAdapter.notifyDataSetChanged();
   }
}
