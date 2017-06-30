package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Feeds;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.ImageCompressor;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class FeedDialog extends DialogFragment implements ImageCompressor.ICompressedImage{


    private View rootView;
    private EditText postText;
    private Button postBtn;
    private ImageView postImage;
    private Bitmap uploadImage;
    private String textData;
    private String encoded_image;
    private ProgressDialog progressDialog;
    private SPData spData;
    private ImageCompressor compressImage;
    private final int MAX_IMAGE_SIZE = 150;
    private Bitmap postImageBitmap;
    private String imageFilePath;
    public FeedDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_feed_dialog, container, false);
        getDialog().setTitle("New Feeds");
        postBtn = (Button) rootView.findViewById(R.id.post_btn);
        postText = (EditText) rootView.findViewById(R.id.post_text);
        postImage = (ImageView) rootView.findViewById(R.id.upload_post_image);
        spData = new SPData();
        progressDialog = new ProgressDialog(getActivity());
        compressImage = new ImageCompressor(getActivity(),this);
        postText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textData = postText.getText().toString().trim();
                progressDialog.setTitle("Uploading Post..");
                if(textData.length()>0 && imageFilePath != null){
                    uploadMultipart(imageFilePath,textData);
                }else if(textData.length() == 0 && imageFilePath.length()>0){
                    uploadMultipart(imageFilePath,"  ");
                }else if(textData.length()>0 && imageFilePath == null){
                    postWithoutImage(textData);
                }
            }
        });
        return rootView;
    }
    private void selectImage() {
        final CharSequence[] items = { "Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), GenericMethods.TEMP_PATH);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 0);

                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            1);
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == getActivity().RESULT_OK){
                    postImageBitmap = GenericMethods.getCameraImage();
                    if(postImageBitmap != null){
                        compressImage.setImg(postImageBitmap);
                        compressImage.execute();
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"Error Loading Image",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 1:
                if(resultCode == getActivity().RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream img = null;
                    try {
                        img  = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(img != null){
                        postImageBitmap = BitmapFactory.decodeStream(img);
                        compressImage.setImg(postImageBitmap);
                        compressImage.execute();
                    }
                }

                break;

        }

    }

    @Override
    public void compressedImageFile(File imageFile) {
        if(imageFile != null){
            postImage.setImageDrawable(Drawable.createFromPath(imageFile.getPath()));
            imageFilePath = imageFile.getPath();
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Some Error Occurred",Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadMultipart(String path,String textData) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();
        if (path == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Please move your image to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, URL.NEW_FEED)
                        .addFileToUpload(path, "jpg")
                        .addParameter(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
                        .addParameter("text",textData)
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(UploadInfo uploadInfo) {
                                progressDialog.setProgress(uploadInfo.getProgressPercent());
                            }

                            @Override
                            public void onError(UploadInfo uploadInfo, Exception exception) {
                                Toast.makeText(getActivity().getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                success(serverResponse.getBodyAsString());
                            }

                            @Override
                            public void onCancelled(UploadInfo uploadInfo) {
                                Toast.makeText(getActivity().getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        })
                        .startUpload();
            } catch (Exception exc) {
                Toast.makeText(getActivity().getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    private void postWithoutImage(String textData){
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                success(result);
            }

            @Override
            public void volleyError() {
                progressDialog.dismiss();
            }
        }).setUrl(URL.NEW_FEED)
        .setParams(SPData.USER_NUMBER,spData.getUserData(SPData.USER_NUMBER))
        .setParams("text", textData)
        .connect();
    }

    private void success(String result){
        if(result.contains("feed_id")){
            Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("NEW_FEED");
            intent.putExtra("data",result);
            getActivity().sendBroadcast(intent);
            getDialog().dismiss();
        }else{
            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }
}
