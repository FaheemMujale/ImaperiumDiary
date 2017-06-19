package com.hubli.imperium.imaperiumdiary.Homework;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentManager;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.UUID;

public class StudentDiary_DatePicker extends AppCompatActivity implements IVolleyResponse {
    public static final String INTENTFILTER ="intent_filter_h";
    public static final String HOMEWORK_NUMBER ="homework_number";
    public static final String HOMEWORK_TITLE= "homework_title";
    public static final String HOMEWORK_CONTENTS = "homework_contents";
    public static final String LASTDATE_SUBMISSION= "lastDate_submission";
    public static final String SUBJECT = "subject";
    public String SUBJECTID;
    public static final String HOMEWORKDATE = "homeworkDate";
    public static final String NUMBER_USER = "number_user";
    private String path;
    private int REQ_PDF = 1;
    private String className;
    private String divisionName;
    private String subjectName;
    private SPData userDataSp;
    private int year_h;
    private int month_h;
    private int day_h;
    private static final int DIALOG_ID = 0;
    private Button submitB;
    private ImageView btn;
    private EditText editTitle, editContent;
    private MyVolley myVolley;
    private TextView date_display;
    private EditText fileName;
    private ProgressDialog progressDialog;
    ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_diary__date_picker);
        userDataSp = new SPData();
        Intent intent = getIntent();
        className = intent.getStringExtra("class");
        divisionName = intent.getStringExtra("division");
        subjectName = intent.getStringExtra("subject");
        SUBJECTID = intent.getStringExtra("subjectid");
        editTitle = (EditText)findViewById(R.id.title_home);
        editContent = (EditText)findViewById(R.id.alertText);
        fileName = (EditText)findViewById(R.id.filepath);
        date_display = (TextView) findViewById(R.id.date_display);
        submitB = (Button)findViewById(R.id.submit_home);
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path!=null)
                    uploadMultipart(path);
                else
                submitHomeWork();
            }
        });
        final Calendar cal = Calendar.getInstance();
        year_h = cal.get(Calendar.YEAR);
        month_h = cal.get(Calendar.MONTH)+1;
        day_h = cal.get(Calendar.DAY_OF_MONTH);
        date_display.setText(day_h+"/"+month_h+"/"+year_h);
        myVolley = new MyVolley(getApplicationContext(), this);
        showDialoOnButtonClick();
        button = (ImageView) findViewById(R.id.upload_pdf);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

    }

    public void showDialoOnButtonClick()
    {
        btn = (ImageView)findViewById(R.id.button_date);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showDialog(DIALOG_ID);
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id==DIALOG_ID)
        return new DatePickerDialog(this, dpickerListener, year_h, month_h-1, day_h);
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_h = year;
            month_h = monthOfYear+1;
            day_h = dayOfMonth;
            date_display.setText(day_h+"/"+month_h+"/"+year_h);
        }
    };

    public void submitHomeWork() {
        if(editTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "fill the entries", Toast.LENGTH_SHORT).show();
                }
        else if (editContent.getText().toString().isEmpty()) {
            Toast.makeText(this, "fill the entries", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            myVolley.setUrl(URL.INSERT_HOMEWORK);
            myVolley.setParams("cd_id", "1");
            myVolley.setParams("subject_id", SUBJECTID);
            myVolley.setParams("hw_lastdate", date_display.getText().toString());
            myVolley.setParams("hw_title", editTitle.getText().toString());
            myVolley.setParams("hw_content", editContent.getText().toString());
            myVolley.setParams(SPData.USER_NUMBER, "1");
            myVolley.connect();
    }
}
    @Override
    public void volleyResponse(String result) {
        if(result != null){
            if(!result.contains("ERROR")){
                Toast.makeText(this, "Submited", Toast.LENGTH_SHORT).show();
                try {
                    parseData(result, editTitle.getText().toString(), editContent.getText().toString(),
                            subjectName, date_display.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    progressDialog.dismiss();
    }

    @Override
    public void volleyError() {

    }

    public void parseData(String re,  String homeTitle, String homeContent,  String subject, String lastDate ) throws JSONException {
        JSONArray json = new JSONArray(re);
        String homeNumber = null;
        String homeDate = null;

        for (int i = 0; i <= json.length() - 1; i++) {
            JSONObject jsonobj = json.getJSONObject(i);
            homeNumber = jsonobj.getString(HOMEWORK_NUMBER);
            homeDate = jsonobj.getString(HOMEWORKDATE);
        }
        Intent intent = new Intent(INTENTFILTER);
        intent.putExtra(HOMEWORK_TITLE, homeTitle);
        intent.putExtra(HOMEWORK_CONTENTS,homeContent );
        intent.putExtra(LASTDATE_SUBMISSION, lastDate);
        intent.putExtra(HOMEWORK_NUMBER, homeNumber );
        intent.putExtra(HOMEWORKDATE, homeDate);
        intent.putExtra(SUBJECT, subject);
        intent.putExtra(NUMBER_USER, userDataSp.getUserData(SPData.NUMBER_USER));
        sendBroadcast(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            path = FilePath.getPath(getApplicationContext(), filePath);
            String[] s = path.split("/");
            String filename = s[s.length - 1].replace(".pdf", "");
            fileName.setText(filename);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf/Attach image"), REQ_PDF);
    }

    public void uploadMultipart(String path) {
        final String name = fileName.getText().toString().trim();

        if (path == null) {
            Toast.makeText(getApplicationContext(), "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            if (name.length()>3) {
                try {
                    String uploadId = UUID.randomUUID().toString();
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new MultipartUploadRequest(getApplicationContext(), uploadId, URL.INSERT_HOMEWORK)
                            .addFileToUpload(path, "hwattachments")
                            .addParameter("user_number","1")
                        .addParameter("subject_id", "0")
                    .addParameter("hw_lastdate", date_display.getText().toString())
                    .addParameter("hw_title", editTitle.getText().toString())
                    .addParameter("hw_content", editContent.getText().toString())
                    .addParameter("cd_id", "1")
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(UploadInfo uploadInfo) {

                                }

                                @Override
                                public void onError(UploadInfo uploadInfo, Exception exception) {
                                    Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }

                                @Override
                                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Submited", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(getApplicationContext(),HomeworkList_teacher.class);
                                    startActivity(intent);
                                    finish();


                                }

                                @Override
                                public void onCancelled(UploadInfo uploadInfo) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .startUpload();

                } catch (Exception exc) {
                    Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getApplicationContext(),"File name too short",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
