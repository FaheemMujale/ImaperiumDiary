package com.hubli.imperium.imaperiumdiary.Leave;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Homework.Dialog_form;
import com.hubli.imperium.imaperiumdiary.Homework.FilePath;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.hubli.imperium.imaperiumdiary.models.LeaveInfo;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static android.R.attr.path;
import static android.R.attr.value;

/**
 * Created by Rafiq Ahmad on 6/16/2017.
 */

public class Leaveapp_form extends Fragment {

    private SPData spdata;
    private View mView;
    private String usertype;
    private ArrayList<LeaveInfo> items ;
    private ProgressBar mProgBar;
    private FloatingActionButton mFab;
    private String path;
    private Calendar maxDate;
    private Calendar minDate;
    String dateString;
    private String[] monthNames;
    private static final String BACK_STACK_MAIN = "main_tag";
    private Button submit;
    TextView todate;
    TextView fromdate;
    EditText detail;
    ArrayList<TextView>arrays = new ArrayList<>();
    EditText filename;
    private int REQ_PDF = 1;
    private static final int RESULT_OK =-1 ;
    public static final String INTENT_FILTER = "dialog_filter";
    public Leaveapp_form(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.leave_application, container, false);
        spdata = new SPData(getActivity().getApplicationContext());
        usertype = spdata.getUserData("USERTYPE");
        todate= (TextView) mView.findViewById(R.id.todate);
        fromdate= (TextView) mView.findViewById(R.id.fromdate);
        detail= (EditText) mView.findViewById(R.id.detail);
        filename= (EditText) mView.findViewById(R.id.filepath);
        submit= (Button) mView.findViewById(R.id.button_submit);
        arrays.add(todate);
        arrays.add(fromdate);
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(0);
            }
        });
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(1);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(todate.getText().toString()!=null && fromdate.getText().toString()!=null && detail.getText().toString()!=null) {
                    if(path!=null)
                    uploadMultipart(path);
                    else
                        serverreq();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all the details",Toast.LENGTH_LONG).show();
                }
            }
        });

        mFab= (FloatingActionButton) mView.findViewById(R.id.addattachment);
        mFab.setVisibility(View.VISIBLE);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showFileChooser();
            }
        });
    return mView;
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf/Attach image"), REQ_PDF);
    }

    private void showDatePicker(final int num) {

        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                arrays.get(num).setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                        + "-" + String.valueOf(year));
            }
        });
        date.show(getFragmentManager(), "Date Picker");
    }





    private void serverreq() {
        new MyVolley(getActivity().getApplicationContext(), new IVolleyResponse() {
            @Override
            public void volleyResponse(String result) {
                Toast.makeText(getActivity().getApplicationContext(),"Submitted",Toast.LENGTH_LONG).show();
//                mProgBar.setVisibility(View.GONE);
                LeaveList_Fragment tabAdaptor = new LeaveList_Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_con,tabAdaptor);
                transaction.addToBackStack(BACK_STACK_MAIN);
                transaction.commit();

            }

            @Override
            public void volleyError() {
                Toast.makeText(getActivity().getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
            }
        }).setUrl(URL.APPLY_LEAVE)
//                .setParams(SPData.USER_NUMBER,spdata.getUserData(SPData.USER_NUMBER))
                .setParams("user_number","1")
        .setParams("status", "Pending")
        .setParams("todate", todate.getText().toString())
        .setParams("fromdate", fromdate.getText().toString())
        .setParams("detail", detail.getText().toString())
        .setParams("cd_id", "1")
                .connect();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            path = FilePath.getPath(getActivity().getApplicationContext(), filePath);
            String[] s = path.split("/");
            String fileName = s[s.length - 1].replace(".pdf", "");
            filename.setText(fileName);
        }
    }

    public void uploadMultipart(String path) {
        final String name = filename.getText().toString().trim();

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
                    new MultipartUploadRequest(getActivity().getApplicationContext(), uploadId, URL.APPLY_LEAVE)
                            .addFileToUpload(path, "attachment")
                            .addParameter("user_number","1")
                            .addParameter("status", "Pending")
                            .addParameter("todate", todate.getText().toString())
                            .addParameter("fromdate", fromdate.getText().toString())
                            .addParameter("detail", detail.getText().toString())
                            .addParameter("cd_id", "1")
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

                                }

                                @Override
                                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(),"Submitted",Toast.LENGTH_LONG).show();
                                    LeaveList_Fragment tabAdaptor = new LeaveList_Fragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.main_con,tabAdaptor);
                                    transaction.addToBackStack(BACK_STACK_MAIN);
                                    transaction.commit();


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

}
