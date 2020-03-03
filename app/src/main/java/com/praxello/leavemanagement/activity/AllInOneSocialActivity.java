package com.praxello.leavemanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.praxello.leavemanagement.R;
import com.praxello.leavemanagement.adapter.SocialMediaAdapter;
import com.praxello.leavemanagement.model.SocialMediaData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllInOneSocialActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rvSocialMedia)
    RecyclerView rvSocialMedia;
    ArrayList<SocialMediaData> socialMediaDataArrayList=new ArrayList<>();
    @BindView(R.id.btn_camera)
    AppCompatButton btnCamera;
    @BindView(R.id.btn_gallery)
    AppCompatButton btnGallery;
    @BindView(R.id.ivimage)
    ImageView ivImage;
    @BindView(R.id.btn_post)
    AppCompatButton btnPost;
    private int GALLERY = 1, CAMERA = 2;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_all_in_one_social);
        ButterKnife.bind(this);

        ///basic intialisation..
        initViews();

        //loadData to recyclerview..
        loadData();

        //initFb...
        callbackManager=CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);
    }

    private void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_all_in_one);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("All In Social");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnPost.setOnClickListener(this);

        rvSocialMedia.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadData(){
        socialMediaDataArrayList.add(new SocialMediaData(R.drawable.ic_facebook,"FaceBook"));
       //socialMediaDataArrayList.add(new SocialMediaData(R.drawable.ic_gmail,"Gmail"));
        socialMediaDataArrayList.add(new SocialMediaData(R.drawable.ic_instagram_sketched,"Instagram"));
        socialMediaDataArrayList.add(new SocialMediaData(R.drawable.ic_twitter,"Twitter"));

        rvSocialMedia.setAdapter(new SocialMediaAdapter(AllInOneSocialActivity.this,socialMediaDataArrayList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera:
                takePhotoFromCamera();
                break;

            case R.id.btn_gallery:
                choosePhotoFromGallary();
                break;

            case R.id.btn_post:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
                //fb post
                shareFaceBookPost();
                break;
        }

    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);

                    byte[] byteArray = out.toByteArray();
                    bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                    ivImage.setImageBitmap(bitmap);
                    //ivProfilePic.setImageBitmap(compressedBitmap);

                  //  selectedImagePath =  getRealPathFromURIForGallery(contentURI);
                    //etMediaLink.setText(selectedImagePath);

                    //Toast.makeText(DashBoardActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AllInOneSocialActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, out);
            bitmap= BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            ivImage.setImageBitmap(bitmap);

            byte[] byteArray = out .toByteArray();

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            //Uri tempUri = getImageUri(AddNewQuestionActivity.this, thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
           // File finalFile = new File(getRealPathFromURI(tempUri));
           // imageBase64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
           // selectedImagePath=finalFile.getAbsolutePath();
           // etMediaLink.setText(selectedImagePath);

            //uploadImageRetrofit(finalFile.getAbsolutePath());

            // Log.e(TAG, "onActivityResult:decoded bitmap "+imageBase64String );

            //Toast.makeText(DashBoardActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    //Facebook SHaring
    public void shareFaceBookPost(){
        SharePhoto sharePhoto=new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        if(ShareDialog.canShow(SharePhotoContent.class)){
            SharePhotoContent content=new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            shareDialog.show(content);

            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(AllInOneSocialActivity.this, "Photo share successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(AllInOneSocialActivity.this, "Sharing cancel", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(AllInOneSocialActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }


}
