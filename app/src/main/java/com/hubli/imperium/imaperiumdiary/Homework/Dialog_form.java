package com.hubli.imperium.imaperiumdiary.Homework;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.UUID;

public class Dialog_form extends DialogFragment implements View.OnClickListener {
    private static final int RESULT_OK =-1 ;
    private Button  btn_upload;
    private EditText file_name;
    private View rootview;
    private SPData userdatasp;
    private ImageView btn_choose;
    private String path;
    public static final String APPLICATION_NAME = "application_name";
    public static final String INTENT_FILTER = "dialog_filter";
    public static final String FORM_LINK = "form_link";
    public static final String FORM_NAME = "form_name";

    private int REQ_PDF = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview= inflater.inflate(R.layout.dialog_layout,null);
        getDialog().setTitle("Upload PDF");
        btn_choose=(ImageView) rootview.findViewById(R.id.upload_pdf);
        btn_upload=(Button)rootview.findViewById(R.id.button_post);
        file_name=(EditText)rootview.findViewById(R.id.editText_name);
        btn_choose.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        userdatasp=new SPData();
        return rootview;
    }

    public void uploadMultipart(String path) {
        final String name = file_name.getText().toString().trim();

        if (path == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            if (name.length()>3) {
                try {
                    String uploadId = UUID.randomUUID().toString();
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, Utils.APPLICATION_FORMS)
                            .addFileToUpload(path, "pdf")
                            .addParameter(APPLICATION_NAME, name)
                            .addParameter(SPData.INSTITUTE_NUMBER,userdatasp.getUserData(SPData.INSTITUTE_NUMBER))
                            .addParameter(SPData.USER_NUMBER,userdatasp.getUserData(SPData.USER_NUMBER))
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(UploadInfo uploadInfo) {

                                }

                                @Override
                                public void onError(UploadInfo uploadInfo, Exception exception) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    getDialog().dismiss();
                                }

                                @Override
                                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    Intent intent = new Intent(INTENT_FILTER);
                                    intent.putExtra(FORM_NAME,name);
                                    intent.putExtra(FORM_LINK,serverResponse.getBodyAsString().replaceAll("DONE",""));
                                    getActivity().sendBroadcast(intent);
                                    progressDialog.dismiss();
                                    getDialog().dismiss();
                                }

                                @Override
                                public void onCancelled(UploadInfo uploadInfo) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .startUpload();

                } catch (Exception exc) {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getActivity().getApplicationContext(),"File name too short",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf/Attach image"), REQ_PDF);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            path = FilePath.getPath(getActivity().getApplicationContext(), filePath);
            String[] s = path.split("/");
            String fileName = s[s.length - 1].replace(".pdf", "");
            file_name.setText(fileName);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_choose) {
            showFileChooser();
        }
        if (v == btn_upload) {
            uploadMultipart(path);
        }
    }
}
