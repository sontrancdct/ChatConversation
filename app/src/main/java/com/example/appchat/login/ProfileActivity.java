package com.example.appchat.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appchat.R;
import com.example.appchat.model.Account;
import com.example.appchat.utils.AccountUtils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

   private CircleImageView imageViewProfile;
   private TextView tvUsername;
   private ProgressDialog loadingBar;
   private Toolbar toolbar;
   StorageReference storageReference;
   private static final int IMAGE_REQUEST = 1;
   public Uri imageUri;
   private Account account;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_profile);

      loadingBar = new ProgressDialog(this);

      setUpToolbar();

      imageViewProfile = findViewById(R.id.set_profile_image);
      tvUsername = findViewById(R.id.set_username);
      storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
      account = (Account) getIntent().getSerializableExtra("Account");

      tvUsername.setText(account.getUserName());
      if(account.getAvatar().equals("default")){
         imageViewProfile.setImageResource(R.drawable.profile_image);
      }else {
         Glide.with(getApplicationContext()).load(account.getAvatar()).into(imageViewProfile);
      }

      imageViewProfile.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            OpenImage();
         }
      });
   }

   @Override
   public void onBackPressed() {
      super.onBackPressed();

   }

   private void setUpToolbar() {
      toolbar = findViewById(R.id.toolBar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setTitle("Profile");
//      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//      getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
   }

   public void OpenImage() {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(intent, IMAGE_REQUEST);
   }
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null) {
         imageUri = data.getData();
         CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1,1)
            .start(this);
      }

      if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
         CropImage.ActivityResult result = CropImage.getActivityResult(data);

         if(resultCode == RESULT_OK) {
            loadingBar.setTitle("Set Profile Image");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Uri resultUri = result.getUri();

            account.setAvatar(resultUri.toString());
            loadingBar.cancel();
            AccountUtils.getInstance().editAccount(account);

            finish();
         }
      }
   }
}
