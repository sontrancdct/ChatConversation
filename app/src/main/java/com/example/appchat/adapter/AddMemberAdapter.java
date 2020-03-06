package com.example.appchat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.R;
import com.example.appchat.model.Account;
import com.example.appchat.utils.MemberGroupUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.ViewHolder> {

   private Context context;
   private ArrayList<Account> accounts;

   String groupId;

   public AddMemberAdapter(Context context, ArrayList<Account> accounts) {
      this.context = context;
      this.accounts = accounts;
   }
   @NonNull
   @Override
   public AddMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent,false);
      return new AddMemberAdapter.ViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull AddMemberAdapter.ViewHolder holder, final int position) {
      final Account account = accounts.get(position);
      holder.username.setText(account.getUserName());
      Log.d("aaa", account.getUserName()+"");


      if (account.getAvatar().equals("default")) {
         holder.myAvatar.setImageResource(R.drawable.profile_image);
      } else {
         Glide.with(holder.myAvatar.getContext())
            .load(account.getAvatar())
            .into(holder.myAvatar);
      }

      holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Toast.makeText(context, account.getUserName()+"", Toast.LENGTH_SHORT).show();
             MemberGroupUtils.getInstance().putAccount(groupId,accounts.get(position));
         }
      });
   }

   @Override
   public int getItemCount() {
      return accounts.size();
   }

   public class ViewHolder extends RecyclerView.ViewHolder{

      public TextView username;
      public ImageView profile_image;
      CircleImageView myAvatar;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);

         username = itemView.findViewById(R.id.item_user_name);
         myAvatar = itemView.findViewById(R.id.item_profile_image);

      }
   }
}
