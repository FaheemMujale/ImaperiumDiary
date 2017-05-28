package com.hubli.imperium.imaperiumdiary.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Interface.IVolleyResponse;
import com.hubli.imperium.imaperiumdiary.Main.MainActivity;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyVolley;
import com.hubli.imperium.imaperiumdiary.Utility.URL;

public class Login extends AppCompatActivity {


    private EditText eUsername, ePassword;
    private SPData spData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eUsername = (EditText) findViewById(R.id.username);
        ePassword = (EditText) findViewById(R.id.password);
        spData = new SPData(getApplicationContext());
    }

    public void loginBtn(View v){
        String username = eUsername.getText().toString().trim();
        String password = ePassword.getText().toString().trim();
        if(username.length() > 0 && password.length() > 0){
            new MyVolley(getApplicationContext(), new IVolleyResponse() {
                @Override
                public void volleyResponse(String volleyResponse) {
                    spData.storeUserData(volleyResponse);
                    if(spData.getIdentification() == SPData.TEACHER) {

                    }
                }

                @Override
                public void volleyError() {

                }
            }).setUrl(URL.LOGIN)
            .setParams("username",username)
            .setParams("password",password)
            .connect();
        }else{
            Toast.makeText(getApplicationContext(),"Please enter Username and Password",Toast.LENGTH_SHORT).show();
        }
    }


}
