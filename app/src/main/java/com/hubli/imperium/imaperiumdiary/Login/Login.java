package com.hubli.imperium.imaperiumdiary.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.Main.MainActivity;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

public class Login extends AppCompatActivity {


    private EditText eUsername, ePassword;
    private SPData spData;
    private boolean backExit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spData = new SPData();

        if(spData.getUserData(SPData.USER_NUMBER) != ""){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        eUsername = (EditText) findViewById(R.id.username);
        ePassword = (EditText) findViewById(R.id.password);
    }

    public void loginBtn(View v){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String deviceIdentification;
        try{
            deviceIdentification = telephonyManager.getDeviceId();
        }catch (Exception e){
            deviceIdentification = telephonyManager.getDeviceId();
        }
        String username = eUsername.getText().toString().trim();
        String password = ePassword.getText().toString().trim();
        if(username.length() > 0 && password.length() > 0){
            spData.setInstituteID(username);
            new MyVolley(getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String volleyResponse) {
                    String response[] = volleyResponse.split("~");
                    spData.storeUserData(response[0]);
                    if(spData.getIdentification() == SPData.TEACHER) {
                        spData.storeTeacherData(response[2]);
                    }else{
                        spData.storeStudentData(response[2]);
                    }
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }

                @Override
                public void volleyError() {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                }
            }).setUrl(URL.LOGIN)
            .setParams("username",username)
            .setParams("password",password)
            .setParams("firebase_token", FirebaseInstanceId.getInstance().getToken())
            .setParams("device_identification",deviceIdentification)
            .connect();
        }else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Please enter Username and Password",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        if(backExit){
            moveTaskToBack(true);
        }else{
            backExit = true;
            Toast.makeText(getApplicationContext(),"Press back button one more time to exit",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backExit = false;
                }
            },1000);
        }
    }

}
