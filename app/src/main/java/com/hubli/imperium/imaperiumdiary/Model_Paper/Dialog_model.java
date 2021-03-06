package com.hubli.imperium.imaperiumdiary.Model_Paper;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.UUID;

/**
 * Created by Naseem on 17-11-2016.
 */

public class Dialog_model extends DialogFragment implements View.OnClickListener {

    public static final String INTENTFILTER_M ="intent_filter_m";
    public static final String PAPER_NAME = "paper_name";
    public static final String PAPER_LINK = "paper_link";
    public static final String USER_UPLOAD = "user_upload";
    private static final int RESULT_OK =-1 ;
    public Button btn_upload;
    private ImageView btn_choose;
    private EditText file_name;
    private View rootview;
    private SPData userdatasp;
    private String path;
    private String classNumber;
    private int PDF_REQ = 1;
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

    public void setClassNumber(String classNumber){
        this.classNumber  = classNumber;
    }

    public void uploadMultipart(final String path) {
        final String name = file_name.getText().toString().trim();
        if (path == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Please move your .pdf file to internal storage and retry",
                    Toast.LENGTH_LONG).show();
        } else {
            if (name.length()>3) {
                try {
                    if(userdatasp.isStudent()){
                        this.classNumber = userdatasp.getUserData(SPData.CLASS);
                    }
                    String uploadId = UUID.randomUUID().toString();
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, URL.MODEL_PAPER)
                            .addFileToUpload(path, "pdf")
                            .addParameter("name", name)
                            .addParameter(SPData.CLASS,this.classNumber)
                            .addParameter(SPData.USER_NUMBER, userdatasp.getUserData(SPData.USER_NUMBER))
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(UploadInfo uploadInfo) {

                                }

                                @Override
                                public void onError(UploadInfo uploadInfo, Exception exception) {getDialog().dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "Uploaded Failed", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(INTENTFILTER_M);
                                    intent.putExtra(PAPER_NAME, name);
                                    intent.putExtra(PAPER_LINK, serverResponse.getBodyAsString().replaceAll("DONE",""));
                                    intent.putExtra(USER_UPLOAD, userdatasp.getUserData(SPData.USER_NUMBER));
                                    getActivity().sendBroadcast(intent);getDialog().dismiss();
                                    // parseDataModel(name, path, userdatasp.getUserData(UserDataSP.NUMBER_USER) );
                                }

                                @Override
                                public void onCancelled(UploadInfo uploadInfo) {
                                    progressDialog.dismiss();
                                    getDialog().dismiss();
                                }
                            })
                            .startUpload();
                } catch (Exception exc) {
                    Toast.makeText(getActivity().getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getActivity().getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getActivity().getApplicationContext(),"File Name is too short",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            path = FilePath.getPath(getActivity().getApplicationContext(), filePath);
            if(path.contains(".pdf")) {
                String[] s = path.split("/");
                String fileName = s[s.length - 1].replace(".pdf", "");
                file_name.setText(fileName);
            }
            else{
                path = "null";
                Toast.makeText(getActivity().getApplicationContext(), "This File is not pdf file", Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
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


    private void parseDataModel(String paper_name, String paper_link, String number_user) {
        Intent intent = new Intent(INTENTFILTER_M);
        intent.putExtra(PAPER_NAME, paper_name);
        intent.putExtra(PAPER_LINK, paper_link);
        intent.putExtra(USER_UPLOAD, number_user);
        getActivity().sendBroadcast(intent);
    }


}
