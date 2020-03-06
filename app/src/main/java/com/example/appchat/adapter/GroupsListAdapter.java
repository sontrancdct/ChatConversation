package com.example.appchat.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.R;
import com.example.appchat.login.ProfileActivity;
import com.example.appchat.model.Group;
import com.example.appchat.utils.GroupUtils;
import com.example.appchat.view.ChatsGrActivity;
import com.example.appchat.view.PlayMesageActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsListAdapter extends RecyclerView.Adapter<GroupsListAdapter.ViewHolder> {

   private ArrayList<Group> groups;
   ProfileActivity profileActivity;

   public GroupsListAdapter(ArrayList<Group> groups) {
      this.groups = groups;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_room,parent,false);
      return new ViewHolder(view);
   }
   @Override
   public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
      holder.txtRoomName.setText(groups.get(position).getName());

      holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ChatsGrActivity.class);
            intent.putExtra("Conversation", groups.get(position));
            v.getContext().startActivity(intent);
         }
      });

      if (groups.get(position).isChecked()){
         holder.container.setVisibility(View.VISIBLE);
      }else {
         holder.container.setVisibility(View.GONE);
      }

      holder.circleImageView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            intent.putExtra("Conversation", groups.get(position));
            v.getContext().startActivity(intent);
         }
      });

      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
            if (groups.get(position).isChecked()) {
               groups.get(position).setChecked(false);
               notifyItemChanged(position);
            }else {
               setCheckedOff();
               groups.get(position).setChecked(true);
               notifyItemChanged(position);
            }
            return false;
         }
      });
      holder.playShow.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), PlayMesageActivity.class);
            intent.putExtra("Conversation", groups.get(position));
            v.getContext().startActivity(intent);
            holder.container.setVisibility(View.GONE);
         }
      });

      holder.deleteGroup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Group group = groups.get(position);

            if (GroupUtils.getInstance().deleteGroup(group.getId())){
               groups.remove(position);
               notifyItemRemoved(position);
              // notifyDataSetChanged();

               Toast.makeText(v.getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
            }else {
               Toast.makeText(v.getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
            }
         }
      });
   }
   @Override
   public int getItemCount() {
      return groups.size();
   }

   class ViewHolder extends RecyclerView.ViewHolder{
      LinearLayout container;
      TextView txtRoomName;
      ImageView playShow, deleteGroup;
      CircleImageView circleImageView;
      ViewHolder(@NonNull View itemView) {
         super(itemView);
         txtRoomName = itemView.findViewById(R.id.txtRoomName);
         container = itemView.findViewById(R.id.container_row_gr);
         playShow = itemView.findViewById(R.id.playShow);
         deleteGroup = itemView.findViewById(R.id.deleteGroup);
         circleImageView = itemView.findViewById(R.id.item_Groups_image);
      }
   }
   private void setCheckedOff(){
      for (int i = 0; i< groups.size(); i++){
         if (groups.get(i).isChecked()){
            groups.get(i).setChecked(false);
            notifyItemChanged(i);
         }
      }
   }

}
