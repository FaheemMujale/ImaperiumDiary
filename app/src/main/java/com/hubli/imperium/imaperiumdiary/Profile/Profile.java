package com.hubli.imperium.imaperiumdiary.Profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.ImageCompressor;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SPData spData;
    private ImageView proPic;
    private ProgressBar proPicProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        spData = new SPData();
        toolbar.setTitle(spData.getUserData(SPData.FIRST_NAME)+" "+spData.getUserData(SPData.LAST_NAME));

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageButton proPicChange = (ImageButton) findViewById(R.id.change_propic);

        proPicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(Profile.this);
                final View deleteDialogView = factory.inflate(R.layout.propic_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(Profile.this).create();
                dialog.setView(deleteDialogView);
                deleteDialogView.findViewById(R.id.propic_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        prepare(toolbar);

        NestedScrollView container = (NestedScrollView) findViewById(R.id.container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfileContent content = new ProfileContent();
        transaction.add(container.getId(),content,"ProfileContainer");
        transaction.commit();
    }

    private void prepare(final Toolbar toolbar){
        proPic = (ImageView) findViewById(R.id.pro_pic_toolbar);
        if(spData.getUserData(SPData.PROPIC_URL).contains("jpeg")){
            loadProPic(false);
        }
        proPicProgressBar = (ProgressBar) findViewById(R.id.propic_progress);
        proPicProgressBar.setVisibility(View.GONE);

        spData = new SPData();
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int toolBarHeight = toolbar.getMeasuredHeight();
                int appBarHeight = appBarLayout.getTotalScrollRange() + toolBarHeight;
                Float f = ((((float) appBarHeight - toolBarHeight) + verticalOffset) / ( (float) appBarHeight - toolBarHeight)) * 255;
                proPic.getBackground().setAlpha(Math.round(f));
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        ImageCompressor compressor = new ImageCompressor(Profile.this, new ImageCompressor.ICompressedImage() {
            @Override
            public void compressedImageFile(File imageFile) {
                if(imageFile != null){
                    uploadMultipart(imageFile.getPath());
                    proPic.setAlpha(0.3f);
                }else{
                    Toast.makeText(getApplicationContext(),"Some Error Occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
        if(resultCode == Profile.this.RESULT_OK){
            switch(requestCode) {
                case 0:
                    Uri selectedImage = data.getData();
                    crop(selectedImage);
                    break;

                case 99:
                    try {
                        Bitmap newProPic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        if (newProPic != null) {
                            compressor.setImg(newProPic);
                            compressor.execute();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    private void uploadMultipart(final String path) {
        if (path == null) {
            Toast.makeText(getApplicationContext(), "Please move your image to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                String uploadId = UUID.randomUUID().toString();
                proPicProgressBar.setVisibility(View.VISIBLE);
                proPicProgressBar.setMax(100);
                new MultipartUploadRequest(getApplicationContext(), uploadId, URL.PROPIC_CHANGE)
                        .addFileToUpload(path, "jpg")
                        .addParameter(spData.USER_NUMBER,spData.getUserData(spData.USER_NUMBER))
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(UploadInfo uploadInfo) {
                                proPicProgressBar.setProgress(uploadInfo.getProgressPercent());
                            }

                            @Override
                            public void onError(UploadInfo uploadInfo, Exception exception) {
                                proPicProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                proPic.setAlpha(1f);
                            }

                            @Override
                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                proPicProgressBar.setVisibility(View.GONE);
                                proPic.setBackgroundDrawable(Drawable.createFromPath(path));
                                Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                                proPic.setAlpha(1f);

                                loadProPic(true);
                            }

                            @Override
                            public void onCancelled(UploadInfo uploadInfo) {
                                proPicProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                                proPic.setAlpha(1f);
                            }
                        })
                        .startUpload();
            } catch (Exception exc) {
                Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                proPic.setAlpha(1f);
            }
        }
    }

    private void loadProPic(boolean refresh){
        proPic.setMinimumWidth(getWindowManager().getDefaultDisplay().getHeight());
        final Target target2 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                proPic.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                proPic.setBackgroundDrawable(new BitmapDrawable(bitmap));
                Picasso.with(getApplicationContext())
                        .load(URL.PROPIC_BASE_URL+spData.getUserData(SPData.PROPIC_URL))
                        .noPlaceholder()
                        .into(target2);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                proPic.setBackgroundDrawable(placeHolderDrawable);
            }
        };

        if(refresh){
            Picasso.with(getApplicationContext())
                    .invalidate(URL.PROPIC_BASE_URL+spData.getUserData(SPData.PROPIC_URL));
            Picasso.with(getApplicationContext())
                    .invalidate(GenericMethods.getThumbNailURL(URL.PROPIC_BASE_URL+spData.getUserData(SPData.PROPIC_URL)));
            Picasso.with(getApplicationContext())
                    .load(URL.PROPIC_BASE_URL+spData.getUserData(SPData.PROPIC_URL))
                    .placeholder(R.drawable.defaultpropic)
                    .networkPolicy(
                            ServerConnect.checkInternetConenction(this) ?
                                    NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                    .into(target);
        }else{
            Picasso.with(getApplicationContext())
                    .load(GenericMethods.getThumbNailURL(URL.PROPIC_BASE_URL+spData.getUserData(SPData.PROPIC_URL)))
                    .placeholder(R.drawable.defaultpropic)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(target);
        }
    }

    private void crop(Uri uri){
        String appPackageName = "com.android.camera.action.CROP";

        try {
            File file = new File(android.os.Environment.getExternalStorageDirectory(), GenericMethods.TEMP_PATH);
            Intent cropIntent = new Intent(appPackageName);
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 480);
            cropIntent.putExtra("outputY", 480);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(cropIntent, 99);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"You Dont have any image cropping app installed..",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.plus")));
        }
    }

    @Override
    public void onRefresh() {

    }
}
